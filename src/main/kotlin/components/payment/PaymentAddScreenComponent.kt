package components.payment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.payment.PaymentRequest
import api.payment.postPayment
import api.rental.RentalResponseViewDto
import api.rental.getAllRental
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import navigation.DecomposeNav.Configuration

class PaymentAddScreenComponent (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>
) : ComponentContext by componentContext
{
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")
    private val _payment: MutableState<PaymentRequest?> = mutableStateOf(null)
    private val _rentals: MutableState<List<RentalResponseViewDto>> = mutableStateOf(listOf())
    private val _amount: MutableState<String> = mutableStateOf("")
    private val _step: MutableState<String> = mutableStateOf("")
    private val _paymentDate: MutableState<String> = mutableStateOf("")
    private val _paymentMethod: MutableState<String> = mutableStateOf("card")

    val showPopup = _showPopup
    val textPopup = _textPopup
    val payment = _payment
    val rentals = _rentals

    val amount = _amount
    val step = _step
    val paymentDate = _paymentDate
    val paymentMethod = _paymentMethod


    fun goBack() {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.PaymentsViewScreen)
    }
    init {
        getData()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getData(){
        GlobalScope.launch {
            try {
                _rentals.value = getAllRental()
            }
            catch (e: Exception){
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun postData(){
        GlobalScope.launch {
            try {
                postPayment(_payment.value!!)
                _textPopup.value = "Платеж добавлен"
                _showPopup.value = true
            }
            catch (e: Exception){
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }
}