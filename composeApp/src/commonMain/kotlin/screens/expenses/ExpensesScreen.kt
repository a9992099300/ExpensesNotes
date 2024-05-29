package screens.expenses

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import expensenotes.composeapp.generated.resources.Res
import expensenotes.composeapp.generated.resources.add
import expensenotes.composeapp.generated.resources.all
import expensenotes.composeapp.generated.resources.back_to_list
import expensenotes.composeapp.generated.resources.choose_category
import expensenotes.composeapp.generated.resources.comment
import expensenotes.composeapp.generated.resources.day
import expensenotes.composeapp.generated.resources.expenses
import expensenotes.composeapp.generated.resources.incomes
import expensenotes.composeapp.generated.resources.input_sum
import expensenotes.composeapp.generated.resources.month
import expensenotes.composeapp.generated.resources.year
import navigation.LocalNavHost
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import screens.components.CommonButton
import screens.components.CommonFilterChip
import screens.components.CommonText
import screens.components.CommonTextFieldOutline
import screens.expenses.models.ExpensesContentState
import screens.expenses.models.ExpensesEvent
import screens.expenses.models.ExpensesTag
import screens.expenses.models.getIncomesTags
import screens.expenses.viewmodel.ExpensesViewModel
import screens.models.ActionDate
import screens.models.CategoryUiModel
import screens.models.ExpensesStateScreen
import screens.models.TypePeriod
import screens.models.TypeTab
import themes.AppTheme

@Composable
internal fun ExpensesScreen(
    viewModel: ExpensesViewModel = viewModel { ExpensesViewModel() }
) {
    val outerNavController = LocalNavHost.current
    val viewState = viewModel.viewStates().collectAsState()
    val viewAction by viewModel.viewActions().collectAsState(null)
    ContentExpensesScreen(
        viewState = viewState.value,
        onChangeStateScreen = {
            viewModel.obtainEvent(ExpensesEvent.OnStateScreenChange(it))
        },
        onClickPeriod = {
            viewModel.obtainEvent(ExpensesEvent.OnPeriodClick(it))
        },
        onChangeDate = {
            viewModel.obtainEvent(ExpensesEvent.OnDateChange(it))
        },
        onChangeTab = {
            viewModel.obtainEvent(ExpensesEvent.OnTabChange(it))
        },
        onSumChanged = {
            viewModel.obtainEvent(ExpensesEvent.OnSumChange(it))
        },
        onClickCategory = {
            viewModel.obtainEvent(ExpensesEvent.OnClickCategory(it))
        },
        onCommentChanged = {
            viewModel.obtainEvent(ExpensesEvent.OnCommentChanged(it))
        }
    )
}

@Composable
fun ContentExpensesScreen(
    modifier: Modifier = Modifier,
    viewState: ExpensesContentState,
    onChangeStateScreen: (ExpensesStateScreen) -> Unit,
    onClickPeriod: (TypePeriod) -> Unit,
    onChangeDate: (ActionDate) -> Unit,
    onChangeTab: (TypeTab) -> Unit,
    onSumChanged: (String) -> Unit,
    onClickCategory: (ExpensesTag) -> Unit,
    onCommentChanged: (String) -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize().padding(0.dp, 0.dp, 0.dp, 56.dp),
        color = AppTheme.colors.primaryBackground,
    ) {
        Box {
            Column {
                TabSelector(viewState, onChangeTab)
                ChooseCategory(
                    viewState.currentCategory
                ) {
                    onClickPeriod(it)
                }
                datePicker(onChangeDate, viewState.dateText)
                if (viewState.stateScreen == ExpensesStateScreen.EXPENSES_LIST) {
                    ListExpensesContent()
                } else {
                    AddExpensesContent(
                        viewState.sum.toString(),
                        viewState.comment,
                        viewState.currentTag,
                        viewState.tags,
                        onSumChanged,
                        onClickCategory,
                        onChangeStateScreen,
                        onCommentChanged
                    )
                }
            }

            if (viewState.stateScreen == ExpensesStateScreen.EXPENSES_LIST) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomEnd)
                        .padding(20.dp, 20.dp),
                    backgroundColor = AppTheme.colors.primaryBackground,
                    onClick = {
                        onChangeStateScreen.invoke(ExpensesStateScreen.ADD_EXPENSES)
                    }
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = null)
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun TabSelector(
    viewState: ExpensesContentState,
    onChangeTab: (TypeTab) -> Unit
) {
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
        if (viewState.stateScreen == ExpensesStateScreen.EXPENSES_LIST) {
            Tab(
                selected = viewState.currentTabs == TypeTab.ALL,
                onClick = { onChangeTab(TypeTab.ALL) },
                text = { Text(text = stringResource(Res.string.all)) }
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun datePicker(
    onChangeDate: (ActionDate) -> Unit,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            Icons.Outlined.KeyboardArrowLeft,
            contentDescription = null,
            modifier = Modifier
                .padding(20.dp, 2.dp, 0.dp, 0.dp)
                .clickable {
                    onChangeDate(ActionDate.REDUCE)
                })
        CommonText(
            modifier = Modifier.weight(1f),
            text = text
        )
        Image(
            Icons.Outlined.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier
                .padding(0.dp, 2.dp, 20.dp, 0.dp)
                .clickable {
                    onChangeDate(ActionDate.INCREASE)
                })
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseCategory(currentCategory: TypePeriod, onClickCategory: (TypePeriod) -> Unit) {
    Column {
        FlowRow(
            Modifier
                .fillMaxWidth(1f)
                .wrapContentHeight(align = Alignment.Top),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Top
        ) {
            categories.forEach {
                CommonFilterChip(it, currentCategory, onClickCategory)
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
    onChangeStateScreen: (ExpensesStateScreen) -> Unit,
    onCommentChanged: (String) -> Unit,
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

        CommonButton(
            stringResource(Res.string.add),
            onClickButton = {

            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        CommonButton(stringResource(Res.string.back_to_list),
            onClickButton = {
                onChangeStateScreen(ExpensesStateScreen.EXPENSES_LIST)
            }
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ItemTag(
    tag: ExpensesTag,
    isCurrent: Boolean,
    onClickCategory: (ExpensesTag) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .size(80.dp)
            .clip(CircleShape)
            .background(
                color =
                if (isCurrent) {
                    AppTheme.colors.activeBackground
                } else {
                    AppTheme.colors.secondaryBackground
                }
            ).clickable {
                onClickCategory.invoke(tag)
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Icon(painter = painterResource(tag.icon), contentDescription = stringResource(tag.name))
        Text(stringResource(tag.name))
    }
}

@Composable
fun ListExpensesContent() {

}

@OptIn(ExperimentalResourceApi::class)
val categories: List<CategoryUiModel> = listOf(
    CategoryUiModel(TypePeriod.DAY, Res.string.day),
    // CategoryUiModel(TypeCategory.PERIOD, Res.string.all),
    CategoryUiModel(TypePeriod.MONTH, Res.string.month),
    CategoryUiModel(TypePeriod.YEAR, Res.string.year)
)
