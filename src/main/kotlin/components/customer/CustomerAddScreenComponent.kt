package components.customer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.customer.CustomerRequest
import api.customer.postCustomer
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import navigation.DecomposeNav.Configuration

class CustomerAddScreenComponent (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>
) : ComponentContext by componentContext
{
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")
    private val _customer: MutableState<CustomerRequest?> = mutableStateOf(null)

    val showPopup = _showPopup
    val textPopup = _textPopup
    val customer = _customer
    @OptIn(DelicateCoroutinesApi::class)
    fun postData(){
        GlobalScope.launch {
            try {
                postCustomer(_customer.value!!)
                _textPopup.value = "Пользователь добавлен"
                _showPopup.value = true
            }
            catch (e: Exception){
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }
}