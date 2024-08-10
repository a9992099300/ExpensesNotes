package features.expenses.addexpenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import expensenotes.composeapp.generated.resources.Res
import expensenotes.composeapp.generated.resources.add
import expensenotes.composeapp.generated.resources.back_to_list
import expensenotes.composeapp.generated.resources.choose_category
import expensenotes.composeapp.generated.resources.comment
import expensenotes.composeapp.generated.resources.expenses
import expensenotes.composeapp.generated.resources.incomes
import expensenotes.composeapp.generated.resources.input_sum
import features.components.CommonButton
import features.components.CommonText
import features.components.CommonTextFieldOutline
import features.expenses.ItemTag
import features.expenses.addexpenses.models.AddExpensesAction
import features.expenses.addexpenses.models.AddExpensesEvent
import features.expenses.addexpenses.models.AddExpensesViewState
import features.expenses.addexpenses.viewmodel.AddExpensesViewModel
import features.expenses.getDateText
import features.expenses.models.ExpensesTag
import features.expenses.models.TypePicker
import features.models.ActionDate
import features.models.TypePeriod
import features.models.TypeTab
import navigation.LocalNavHost
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.stringResource
import themes.AppTheme

@Composable
internal fun AddExpensesScreen(
    tab: String,
    viewModel: AddExpensesViewModel = viewModel { AddExpensesViewModel(tab = tab) }
) {
    val viewState = viewModel.viewStates().collectAsState()
    val viewAction by viewModel.viewActions().collectAsState(null)
    val outerNavigation = LocalNavHost.current

    when (viewAction) {
        AddExpensesAction.ActionBack -> outerNavigation.popBackStack()
        null -> {}
    }

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
        onSumChanged = {
            viewModel.obtainEvent(AddExpensesEvent.OnSumChange(it))
        },
        onClickCategory = {
            viewModel.obtainEvent(AddExpensesEvent.OnClickCategory(it))
        },
        onCommentChanged = {
            viewModel.obtainEvent(AddExpensesEvent.OnCommentChanged(it))
        },
        onAddExpensesItem = {
            viewModel.obtainEvent(AddExpensesEvent.OnAddExpensesItem)
        }
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
    onSumChanged: (String) -> Unit,
    onClickCategory: (ExpensesTag) -> Unit,
    onCommentChanged: (String) -> Unit,
    onAddExpensesItem: () -> Unit,
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
                CommonText(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .height(40.dp)
                        .fillMaxWidth(),
                    text = getDateText(
                        TypePicker.ADD,
                        viewState.currentCategory,
                        viewState.dateText,
                        SpanStyle(fontWeight = FontWeight.W700),
                        SpanStyle(fontWeight = FontWeight.W400)
                    )
                )

                AddExpensesContent(
                    viewState.sum.toString(),
                    viewState.comment,
                    viewState.currentTag,
                    viewState.tags,
                    onSumChanged,
                    onClickCategory,
                    onCommentChanged,
                    onAddExpensesItem
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class, InternalResourceApi::class, ExperimentalLayoutApi::class)
@Composable
fun AddExpensesContent(
    sum: String,
    comment: String,
    currentCategory: ExpensesTag,
    tags: List<ExpensesTag>,
    onSumChanged: (String) -> Unit,
    onClickCategory: (ExpensesTag) -> Unit,
    onCommentChanged: (String) -> Unit,
    onAddExpensesItem: () -> Unit,
) {

    val outerNavigation = LocalNavHost.current

    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
    ) {
        CommonTextFieldOutline(
            text = sum,
            hint = stringResource(Res.string.input_sum),
            keyboardType = KeyboardType.Number
        ) {
            onSumChanged(it)
        }

        Spacer(modifier = Modifier.height(8.dp))

        CommonTextFieldOutline(
            text = comment,
            hint = stringResource(Res.string.comment),
            keyboardType = KeyboardType.Text
        ) {
            onCommentChanged(it)
        }

        CommonText(
            modifier = Modifier.padding(0.dp, 24.dp, 0.dp, 8.dp),
            text = stringResource(Res.string.choose_category),
            size = 16
        )
        FlowRow(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            tags.forEach {
                ItemTag(it, currentCategory == it) { tag ->
                    onClickCategory(tag)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CommonButton(
            stringResource(Res.string.add),
            onClickButton = {
                onAddExpensesItem()
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        CommonButton(stringResource(Res.string.back_to_list),
            onClickButton = { outerNavigation.popBackStack() }
        )
    }
}



