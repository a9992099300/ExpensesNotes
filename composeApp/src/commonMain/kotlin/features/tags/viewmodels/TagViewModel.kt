package features.tags.viewmodels

import di.Inject.instance
import features.expenses.models.ExpensesAction
import features.expenses.models.ExpensesContentState
import features.expenses.models.ExpensesEvent
import features.expenses.repository.ExpensesRepository
import presentation.base.BaseViewModel

class TagViewModel(
    private val expensesRepository: ExpensesRepository = instance(),
) : BaseViewModel<ExpensesContentState, ExpensesAction, ExpensesEvent>(
    initialState = ExpensesContentState(),
) {
    override fun obtainEvent(viewEvent: ExpensesEvent) {
        TODO("Not yet implemented")
    }


}