package screens.expenses

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import navigation.ExpensesScreens
import navigation.LocalNavHost
import screens.components.CommonFilterChip
import screens.components.CommonText
import screens.expenses.models.ExpensesEvent
import screens.expenses.models.ExpensesViewState
import screens.expenses.viewmodel.ExpensesViewModel
import screens.models.ActionDate
import screens.models.CategoryUiModel
import screens.models.TypeCategory
import themes.AppTheme

@Composable
internal fun ExpensesScreen(
    viewModel: ExpensesViewModel = viewModel { ExpensesViewModel() },
) {
    val outerNavController = LocalNavHost.current
    val viewState = viewModel.viewStates().collectAsState()
    val viewAction by viewModel.viewActions().collectAsState(null)
    Text("ExpensesScreen")
    when (val state = viewState.value) {
        ExpensesViewState.Loading -> {}
        ExpensesViewState.NoItems -> {}
        ExpensesViewState.Error -> {}
        is ExpensesViewState.Content -> ContentExpensesScreen(
            viewState = state,
            onAddNewExpenses = {
                outerNavController.navigate(ExpensesScreens.AddExpenses.route)
            },
            onClickCategory = {
                viewModel.obtainEvent(ExpensesEvent.OnCategoryClick(it))
            },
            onChangeDate = {
                viewModel.obtainEvent(ExpensesEvent.OnDateChange(it))
            }
        )
    }
}

@Composable
fun ContentExpensesScreen(
    modifier: Modifier = Modifier,
    viewState: ExpensesViewState.Content,
    onAddNewExpenses: () -> Unit,
    onClickCategory: (TypeCategory) -> Unit,
    onChangeDate: (ActionDate) -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = AppTheme.colors.primaryBackground,
    ) {
        Box {
            Column {
                ChooseCategory(
                    viewState.currentCategory
                ) {
                    onClickCategory(it)
                }
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
                        text = viewState.dateText
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

            FloatingActionButton(
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .padding(20.dp, 80.dp),
                backgroundColor = AppTheme.colors.primaryBackground,
                onClick = {
                    onAddNewExpenses.invoke()
                }
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseCategory(currentCategory: TypeCategory, onClickCategory: (TypeCategory) -> Unit) {
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

val categories: List<CategoryUiModel> = listOf(
    CategoryUiModel(TypeCategory.DAY, "День"),
    // CategoryUiModel(TypeCategory.PERIOD, "Период"),
    CategoryUiModel(TypeCategory.MONTH, "Месяц"),
    CategoryUiModel(TypeCategory.YEAR, "Год")
)

