package components.payment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.payment.*
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import navigation.DecomposeNav.Configuration
import java.time.format.DateTimeFormatter
import java.util.*

class PaymentCardScreenComponent (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>,
    id: Long
) : ComponentContext by componentContext
{
    private val _id = id
    private val _payment: MutableState<PaymentResponse?> = mutableStateOf(null)
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")
    private val _amount: MutableState<String> = mutableStateOf("")
    private val _step: MutableState<String> = mutableStateOf("")
    private val _paymentDate: MutableState<String> = mutableStateOf("")
    private val _paymentMethod: MutableState<String> = mutableStateOf("card")

    val showPopup = _showPopup
    val textPopup = _textPopup
    val payment = _payment

    val amount = _amount
    val step = _step
    val paymentDate = _paymentDate
    val paymentMethod = _paymentMethod

    init {
        getData()
    }

    fun goBack() {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.PaymentsViewScreen)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getData(){
        GlobalScope.launch {
            try {
                _payment.value = getPaymentById(_id)
                _amount.value = _payment.value!!.amount.toString()
                _step.value = _payment.value!!.step.toString()
                _paymentDate.value = _payment.value!!.paymentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale("ru")))
                _paymentMethod.value = _payment.value!!.paymentMethod
            }
            catch (e: Exception){
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun putData(payment: PaymentRequest){
        GlobalScope.launch {
            try {
                var res = putPayment(_id, payment)

                _textPopup.value = "Данные успешно изменены"
                _showPopup.value = true
            }
            catch (e: Exception){
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteDate() {
        GlobalScope.launch {
            try {
                deletePayment(_payment.value!!.paymentId)
                _textPopup.value = "Платеж удалена"
                _showPopup.value = true
            } catch (e: Exception) {
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }


}