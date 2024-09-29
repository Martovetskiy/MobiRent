package components.customer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.customer.CustomerResponse
import api.customer.deleteCustomer
import api.customer.putCustomer
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import navigation.DecomposeNav.Configuration

class CustomerCardScreenComponent (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>,
    customerF: CustomerResponse? = null
) : ComponentContext by componentContext
{
    private val _customer: MutableState<CustomerResponse?> = mutableStateOf(customerF)
    private val _customerTemp: MutableState<CustomerResponse?> = mutableStateOf(_customer.value)
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")

    val showPopup = _showPopup
    val textPopup = _textPopup
    val customer = _customer
    val customerTemp = _customerTemp

    fun goBack() {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.CustomersViewScreen)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun putData(customer: CustomerResponse){
        GlobalScope.launch {
            try {
                _customer.value = putCustomer(customer)
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
                _customer.value = deleteCustomer(_customer.value!!.customerId)
                _textPopup.value = "Юзер удален"
                _showPopup.value = true
            } catch (e: Exception) {
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }


}