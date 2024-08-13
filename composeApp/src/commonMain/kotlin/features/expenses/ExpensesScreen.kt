package features.expenses

import NavigationScreens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import expensenotes.composeapp.generated.resources.Res
import expensenotes.composeapp.generated.resources.all
import expensenotes.composeapp.generated.resources.day
import expensenotes.composeapp.generated.resources.expenses
import expensenotes.composeapp.generated.resources.incomes
import expensenotes.composeapp.generated.resources.month
import expensenotes.composeapp.generated.resources.year
import ui.components.CommonFilterChip
import ui.components.CommonText
import features.expenses.models.DateText
import features.expenses.models.ExpensesAction
import features.expenses.models.ExpensesContentState
import features.expenses.models.ExpensesEvent
import features.expenses.models.ExpensesTag
import features.expenses.models.TypeData
import features.expenses.models.TypePicker
import features.expenses.viewmodel.ExpensesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ui.components.DateItem
import ui.components.ExpensesItem
import ui.models.ActionDate
import ui.models.CategoryUiModel
import ui.models.ExpensesStateScreen
import ui.models.TypePeriod
import ui.models.TypeTab
import navigation.LocalNavHost
import navigation.NavigationState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.themes.AppTheme

@Composable
internal fun ExpensesScreen(
    navigationState: NavigationState,
    viewModel: ExpensesViewModel = viewModel { ExpensesViewModel() },
) {
    val outerNavController = LocalNavHost.current
    val viewState = viewModel.viewStates().collectAsState()
    val viewAction by viewModel.viewActions().collectAsState(null)

    when (val state = viewAction) {
        is ExpensesAction.OpenAddExpenses -> {
            outerNavController.navigate(NavigationScreens.AddExpenses.getRouteWithArgs(state.currentTab.index))
        }

        else -> {}
    }

    ContentExpensesScreen(
        viewState = viewState.value,
        onChangeStateScreen = {
            viewModel.obtainEvent(ExpensesEvent.OnAddClick)
        },
        onClickPeriod = {
            viewModel.obtainEvent(ExpensesEvent.OnPeriodClick(it))
        },
        onChangeDate = {
            viewModel.obtainEvent(ExpensesEvent.OnDateChange(it))
        },
        onChangeTab = {
            viewModel.obtainEvent(ExpensesEvent.OnTabChange(it))
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
    onChangeTab: (TypeTab) -> Unit
) {
    val stateVisibleFloatingButton = remember { mutableStateOf(false) }

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
                datePicker(
                    onChangeDate,
                    viewState.dateText,
                    viewState.currentCategory,
                    TypePicker.LIST
                )
                ListExpensesContent(viewState) {
                    stateVisibleFloatingButton.value = true
                }

            }

            FloatingActionButton(
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .padding(20.dp, 30.dp),
                backgroundColor = AppTheme.colors.primaryAction,
                onClick = {
                    onChangeStateScreen.invoke(ExpensesStateScreen.ADD_EXPENSES)
                }
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null)
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
            text = { Text(text = stringResource(Res.string.incomes)) },
        )
        Tab(
            selected = viewState.currentTabs == TypeTab.ALL,
            onClick = { onChangeTab(TypeTab.ALL) },
            text = { Text(text = stringResource(Res.string.all)) }
        )
    }
}

@Composable
fun datePicker(
    onChangeDate: (ActionDate) -> Unit,
    dateText: DateText,
    currentCategory: TypePeriod,
    type: TypePicker,
) {
    val ordinaryStyle = SpanStyle(fontWeight = FontWeight.W400)
    val specialStyle = SpanStyle(fontWeight = FontWeight.W700)
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
            text = getDateText(type, currentCategory, dateText, specialStyle, ordinaryStyle)
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

fun getDateText(
    type: TypePicker,
    currentCategory: TypePeriod,
    dateText: DateText,
    specialStyle: SpanStyle,
    ordinaryStyle: SpanStyle
) = if (type == TypePicker.LIST) {
    buildAnnotatedString {
        if (currentCategory == TypePeriod.DAY) {
            append(dateText.day + " ")
            append(dateText.month + " ")
            append(dateText.year)
        }
        if (currentCategory == TypePeriod.MONTH) {
            append(dateText.monthOnly + " ")
            append(dateText.year)
        }
        if (currentCategory == TypePeriod.YEAR) {
            append(dateText.year)
        }
    }
} else {
    buildAnnotatedString {
        withStyle(style = if (currentCategory == TypePeriod.DAY) specialStyle else ordinaryStyle) {
            append(dateText.day + " ")
        }
        withStyle(style = if (currentCategory == TypePeriod.MONTH) specialStyle else ordinaryStyle) {
            append(dateText.month + " ")
        }
        withStyle(style = if (currentCategory == TypePeriod.YEAR) specialStyle else ordinaryStyle) {
            append(dateText.year)
        }
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
fun ListExpensesContent(
    expenses: ExpensesContentState,
    animateFloatingButton: () -> Unit
) {
    val state = rememberLazyListState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
             //  println("available $available source $source")
                animateFloatingButton()
                val delta = available.y
               // println("onPreScroll available $available delta $delta")
                // called when you scroll the content
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
             //   println("onPostScroll consumed $consumed available $available source $source")
                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    LaunchedEffect(expenses.currentCategory, expenses.currentTabs) {
        snapshotFlow { state.firstVisibleItemIndex }
            .collect {
                println("state.firstVisibleItemIndex ${state.firstVisibleItemIndex}")
                // Scroll to the top if a new item is added.
                // (But only if user is scrolled to the top already.)
                if (it > 0) {
                    state.animateScrollToItem(index = 0)
                }
            }
    }
    LazyColumn(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 16.dp)
            .nestedScroll(nestedScrollConnection),
        state = state
//            .verticalScroll(state)

    ) {
        items(
            items = expenses.items,
            key = { it.id }
        ) { model ->
            if (model.typeData == TypeData.DATA) {
                ExpensesItem(model)
            } else {
                DateItem(model)
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
val categories: List<CategoryUiModel> = listOf(
    CategoryUiModel(TypePeriod.DAY, Res.string.day),
    CategoryUiModel(TypePeriod.MONTH, Res.string.month),
    CategoryUiModel(TypePeriod.YEAR, Res.string.year)
)

