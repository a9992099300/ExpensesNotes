package screens.expenses.addexpenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import screens.components.CommonText
import screens.components.CommonTextFieldOutline
import androidx.compose.material.MaterialTheme

@Composable
internal fun AddExpensesScreen(
) {

    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonText("Расходы", textStyle =  MaterialTheme.typography.h3, size = 22)
        CommonTextFieldOutline("", "Введите сумму в руб.") {
        }
    }
}


