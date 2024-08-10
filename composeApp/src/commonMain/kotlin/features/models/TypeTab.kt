package features.models

enum class TypeTab(val index: Int) {
    EXPENSES(0),
    INCOMES(1),
    ALL(2);
}
fun getTab(index: Int) =
    when (index) {
        0 -> TypeTab.EXPENSES
        1 -> TypeTab.INCOMES
        else -> TypeTab.ALL
    }