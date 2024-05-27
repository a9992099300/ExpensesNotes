package screens.expenses.addexpenses

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import expensenotes.composeapp.generated.resources.Res
import expensenotes.composeapp.generated.resources.all
import expensenotes.composeapp.generated.resources.expenses
import expensenotes.composeapp.generated.resources.incomes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import screens.expenses.ChooseCategory
import screens.expenses.addexpenses.models.AddExpensesEvent
import screens.expenses.addexpenses.models.AddExpensesViewState
import screens.expenses.addexpenses.viewmodel.AddExpensesViewModel
import screens.expenses.datePicker
import screens.expenses.models.ExpensesEvent
import screens.models.ActionDate
import screens.models.TypePeriod
import screens.models.TypeTab
import themes.AppTheme

@Composable
internal fun AddExpensesScreen(
    viewModel: AddExpensesViewModel = viewModel { AddExpensesViewModel() }
) {

    val viewState = viewModel.viewStates().collectAsState()
    val viewAction by viewModel.viewActions().collectAsState(null)

    ContentAddExpensesScreen(
        modifier = Modifier,
        viewState = viewState.value,
        onChangeTab = {
            viewModel.obtainEvent(AddExpensesEvent.OnTabChange(it))
        },
        onClickPeriod = {
            viewModel.obtainEvent(AddExpensesEvent.OnPeriodClick(it))
        },
        onChangeDate = {
            viewModel.obtainEvent(AddExpensesEvent.OnDateChange(it))
        },
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ContentAddExpensesScreen(
    modifier: Modifier = Modifier,
    viewState: AddExpensesViewState,
    onChangeTab: (TypeTab) -> Unit,
    onClickPeriod: (TypePeriod) -> Unit,
    onChangeDate: (ActionDate) -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = AppTheme.colors.primaryBackground,
    ) {
        Box {
            Column {
                TabRow(
                    selectedTabIndex = viewState.currentTabs.index,
                    backgroundColor = AppTheme.colors.primaryBackground
                ) {
                    Tab(
                        selected = viewState.currentTabs == TypeTab.ALL,
                        onClick = { onChangeTab(TypeTab.ALL) },
                        text = { Text(text = stringResource(Res.string.all)) }
                    )
                    Tab(
                        selected = viewState.currentTabs == TypeTab.EXPENSES,
                        onClick = { onChangeTab(TypeTab.EXPENSES) },
                        text = { Text(text = stringResource(Res.string.expenses)) }
                    )
                    Tab(
                        selected = viewState.currentTabs == TypeTab.INCOMES,
                        onClick = { onChangeTab(TypeTab.INCOMES) },
                        text = { Text(text = stringResource(Res.string.incomes)) }
                    )
                }
                ChooseCategory(
                    viewState.currentCategory
                ) {
                    onClickPeriod(it)
                }
                datePicker(onChangeDate = {
                    onChangeDate(it)
                }, viewState.dateText)
            }
        }
    }
}



