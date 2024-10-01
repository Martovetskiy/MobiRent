package components.payment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.payment.PaymentResponseViewDto
import api.payment.getAllPayment
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import navigation.DecomposeNav.Configuration

class PaymentsViewScreenComponent  (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>
) : ComponentContext by componentContext
{
    private val _listPayments: MutableState<List<PaymentResponseViewDto>> = mutableStateOf(listOf())
    private val _email: MutableState<String?> = mutableStateOf(null)
    private val _amount: MutableState<String?> = mutableStateOf(null)
    private val _step: MutableState<String?> = mutableStateOf(null)
    private val _paymentDate: MutableState<String?> = mutableStateOf(null)
    private val _paymentMethod: MutableState<String?> = mutableStateOf(null)
    private val _createAt: MutableState<String?> = mutableStateOf(null)
    private val _sortBy: MutableState<String> = mutableStateOf("email")
    private val _sortDirection: MutableState<String> = mutableStateOf("ASC")
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")

    val showPopup = _showPopup
    val textPopup = _textPopup

    val listPayments = _listPayments
    val email = _email
    val amount = _amount
    val step = _step
    val paymentDate = _paymentDate
    val paymentMethod = _paymentMethod

    val sortBy = _sortBy
    val sortDirection = _sortDirection

    init {
        getData()

    }

    fun goToCard(id: Long)
    {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.PaymentCardScreen(id))
    }

    fun goToAdd()
    {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.PaymentAddScreen)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getData(){
        GlobalScope.launch {
            try {
                _listPayments.value = getAllPayment(
                    email = _email.value,
                    amount = _amount.value,
                    step = _step.value,
                    paymentDate = _paymentDate.value,
                    paymentMethod = _paymentMethod.value,
                    createAt = _createAt.value,
                    sortBy = _sortBy.value,
                    sortDirection = _sortDirection.value
                )
            }
            catch (e: Exception){
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }
}