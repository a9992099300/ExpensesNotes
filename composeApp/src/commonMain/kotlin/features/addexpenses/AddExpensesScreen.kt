package features.addexpenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import expensenotes.composeapp.generated.resources.Res
import expensenotes.composeapp.generated.resources.add
import expensenotes.composeapp.generated.resources.add_new_item
import expensenotes.composeapp.generated.resources.cancel
import expensenotes.composeapp.generated.resources.choose_category
import expensenotes.composeapp.generated.resources.comment
import expensenotes.composeapp.generated.resources.expenses
import expensenotes.composeapp.generated.resources.incomes
import expensenotes.composeapp.generated.resources.input_sum
import features.addexpenses.models.AddExpensesAction
import features.addexpenses.models.AddExpensesEvent
import features.addexpenses.models.AddExpensesViewState
import features.addexpenses.viewmodel.AddExpensesViewModel
import features.expenses.ItemTag
import features.expenses.getDateText
import features.expenses.models.ExpensesTag
import features.expenses.models.TypePicker
import navigation.LocalNavHost
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.components.CommonButton
import ui.components.CommonText
import ui.components.CommonTextFieldOutline
import ui.models.ActionDate
import ui.models.TypePeriod
import ui.models.TypeTab
import ui.themes.AppTheme

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
                CommonText(
                    modifier = Modifier.fillMaxWidth().background(AppTheme.colors.navbarBackground),
                    stringResource = Res.string.add_new_item,
                )
                Divider(modifier = Modifier.fillMaxWidth())
                TabRow(
                    selectedTabIndex = if (TypeTab.EXPENSES.index == viewState.currentTabs.index) 0 else 1,
                    backgroundColor = AppTheme.colors.navbarBackground,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[viewState.currentTabs.index]),
                            color = AppTheme.colors.activeBorder
                        )
                    }
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

        AddActionButton(
            onAddExpensesItem
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AddActionButton(
    onAddExpensesItem: () -> Unit,
) {

    val outerNavigation = LocalNavHost.current

    Row(
        modifier = Modifier
            .background(AppTheme.colors.primaryBackground)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        CommonButton(
            modifier = Modifier.weight(7f),
            text = stringResource(Res.string.cancel),
            onClickButton = {
                outerNavigation.popBackStack()
            },
            backgroundColor = AppTheme.colors.primaryBackground,
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
        )

        Spacer(Modifier.width(8.dp))

        CommonButton(
            modifier = Modifier.weight(10f),
            text = stringResource(Res.string.add),
            onClickButton = {
                onAddExpensesItem()
            }
        )
    }
}





