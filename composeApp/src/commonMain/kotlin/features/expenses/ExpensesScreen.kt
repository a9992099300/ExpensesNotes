package features.expenses

import NavigationScreens
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.End
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import features.expenses.models.DateText
import features.expenses.models.ExpensesAction
import features.expenses.models.ExpensesContentState
import features.expenses.models.ExpensesEvent
import features.tags.models.ExpensesTag
import features.expenses.models.TypeData
import features.expenses.models.TypePicker
import features.expenses.viewmodel.ExpensesViewModel
import navigation.LocalNavHost
import navigation.NavigationState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import presentation.components.CommonFilterChip
import presentation.components.CommonText
import presentation.components.DateItem
import presentation.components.ExpensesItem
import presentation.components.SumItem
import presentation.models.ActionDate
import presentation.models.CategoryUiModel
import presentation.models.ExpensesStateScreen
import presentation.models.TypePeriod
import presentation.models.TypeTab
import presentation.themes.AppTheme

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
        },
        onDeleteItem = {
            viewModel.obtainEvent(ExpensesEvent.OnDeleteItem(it))
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
    onDeleteItem: (Long) -> Unit
) {
    var stateVisibleFloatingButton by remember { mutableStateOf(true) }
    var stateVisibleSum by remember { mutableStateOf(true) }

    Surface(
        modifier = modifier.fillMaxSize().padding(bottom = 56.dp),
        color = AppTheme.colors.primaryBackground,
    ) {
        Box {
            Column {
                TabSelector(viewState.currentTabs, onChangeTab)
                ChooseCategory(
                    viewState.currentCategory
                ) {
                    onClickPeriod(it)
                }
                DatePicker(
                    onChangeDate,
                    viewState.dateText,
                    viewState.currentCategory,
                    TypePicker.LIST
                )
                AnimatedVisibility(stateVisibleSum) {
                    SumView(viewState.expensesSum, viewState.incomesSum, viewState.currentTabs)
                }
                ListExpensesContent(
                    viewState,
                    visibleFloatingButton = {
                        stateVisibleFloatingButton = it
                    },
                    deleteItem = { onDeleteItem(it) },
                    visibleSumView = {
                        stateVisibleSum = it
                    }
                )
            }
            Column(
                modifier = Modifier.align(Alignment.BottomEnd).fillMaxWidth()
            ) {
                AnimatedVisibility(stateVisibleFloatingButton) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.weight(1f))

                        FloatingActionButton(
                            modifier = Modifier
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
        }
    }
}

@Composable
private fun SumView(sumExpenses: Long, sumIncomes: Long, type: TypeTab) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (type == TypeTab.EXPENSES || type == TypeTab.ALL) {
            SumItem(sumExpenses, Icons.Outlined.KeyboardArrowDown, AppTheme.colors.tagColorRed)
        }

        if (type == TypeTab.INCOMES || type == TypeTab.ALL) {
            SumItem(sumIncomes, Icons.Outlined.KeyboardArrowUp, AppTheme.colors.tagColorGreen)
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun TabSelector(
    currentTabs: TypeTab,
    onChangeTab: (TypeTab) -> Unit
) {
    TabRow(
        selectedTabIndex = currentTabs.index,
        backgroundColor = AppTheme.colors.navbarBackground,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[currentTabs.index]),
                color = AppTheme.colors.activeBorder
            )
        }
    ) {
        Tab(
            selected = currentTabs == TypeTab.EXPENSES,
            onClick = { onChangeTab(TypeTab.EXPENSES) },
            text = { Text(text = stringResource(Res.string.expenses)) }
        )
        Tab(
            selected = currentTabs == TypeTab.INCOMES,
            onClick = { onChangeTab(TypeTab.INCOMES) },
            text = { Text(text = stringResource(Res.string.incomes)) },
        )
        Tab(
            selected = currentTabs == TypeTab.ALL,
            onClick = { onChangeTab(TypeTab.ALL) },
            text = { Text(text = stringResource(Res.string.all)) }
        )
    }
}

@Composable
fun DatePicker(
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
        Icon(
            painter = painterResource(tag.icon),
            contentDescription = null
        )
        Text(
            if (tag.name != null) stringResource(tag.name!!) else tag.nameString
        )
    }
}

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
)
@Composable
fun ListExpensesContent(
    expenses: ExpensesContentState,
    visibleFloatingButton: (Boolean) -> Unit,
    visibleSumView: (Boolean) -> Unit,
    deleteItem: (Long) -> Unit
) {
    val state = rememberLazyListState()
    var isDeleteRequested by remember { mutableStateOf(false) }
    var isCancelDismiss by remember { mutableStateOf(false) }

    if (state.isScrollInProgress) {
        visibleFloatingButton(false)
        DisposableEffect(Unit) {
            onDispose {
                visibleFloatingButton(true)
            }
        }
    }

    LaunchedEffect(expenses.currentCategory, expenses.currentTabs) {
        snapshotFlow { state.firstVisibleItemIndex }
            .collect {
                // Скрол наверх если сменили тип или период
                if (it > 0) {
                    state.animateScrollToItem(index = 0)
                } else {
                }
            }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { state.firstVisibleItemIndex }
            .collect {
                if (it > 0) {
                    visibleSumView(false)
                } else {
                    visibleSumView(true)
                }
            }
    }
    LazyColumn(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 16.dp),
        state = state
    ) {
        items(
            items = expenses.items,
            key = { it.id }
        ) { model ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                deleteItem(model.id)
            }
            if (model.typeData == TypeData.DATA) {
                SwipeToDismiss(
                    modifier = Modifier.animateItemPlacement(),
                    state = dismissState,
                    background = {
                        Card(
                            modifier = Modifier.padding(16.dp, 4.dp).height(48.dp).fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            backgroundColor = AppTheme.colors.tagColorRedSlow,
                        ) {
                            Row(
                                modifier = Modifier.height(10.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = End
                            ) {
                                Image(
                                    imageVector = Icons.Outlined.Delete,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    contentDescription = "Delete Icon",
                                    alignment = Alignment.CenterEnd,
                                )
                            }

                        }
                    },
                    directions = setOf(DismissDirection.EndToStart)
                ) {
                    ExpensesItem(model)
                }
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

//                if (isCancelDismiss) {
//                    LaunchedEffect(Unit) {
//                        dismissState.reset()
//                    }
//                    isCancelDismiss = false
//                }
//                LaunchedEffect(Unit) {
//                    isDeleteRequested = true
//                }

//    if (isDeleteRequested) {
//        CommonDialog(
//            text = null,
//            title = Res.string.delete_item,
//            onCanceled = {
//                isDeleteRequested = false
//                isCancelDismiss = true
//            },
//            onSuccess = {
//                isDeleteRequested = false
//                // component.deleteChapter()
//            }
//        )
//    }

