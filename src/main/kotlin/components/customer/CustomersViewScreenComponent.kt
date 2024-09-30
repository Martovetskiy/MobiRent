package components.customer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.customer.CustomerResponse
import api.customer.getAllCustomer
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import navigation.DecomposeNav.Configuration

class CustomersViewScreenComponent (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>
) : ComponentContext by componentContext
{
    private val _listCustomers: MutableState<List<CustomerResponse>> = mutableStateOf(listOf())
    private val _firstName: MutableState<String?> = mutableStateOf(null)
    private val _lastName: MutableState<String?> = mutableStateOf(null)
    private val _email: MutableState<String?> = mutableStateOf(null)
    private val _phoneNumber: MutableState<String?> = mutableStateOf(null)
    private val _driverLicense: MutableState<String?> = mutableStateOf(null)
    private val _isBanned: MutableState<Boolean?> = mutableStateOf(null)
    private val _sortBy: MutableState<String> = mutableStateOf("firstName")
    private val _sortDirection: MutableState<String> = mutableStateOf("ASC")
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")

    val showPopup = _showPopup
    val textPopup = _textPopup

    val listCustomers = _listCustomers
    val firstName = _firstName
    val lastName = _lastName
    val email = _email
    val phoneNumber = _phoneNumber
    val driverLicense = _driverLicense
    val isBanned = _isBanned
    val sortBy = _sortBy
    val sortDirection = _sortDirection

    init {
        getData()

    }

    fun goToCard(customer: CustomerResponse)
    {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.CustomerCardScreen(customer))
    }

    fun goToAdd()
    {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.CustomerAddScreen)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getData(){
        GlobalScope.launch {
            try {
                _listCustomers.value = getAllCustomer(
                    firstName = _firstName.value,
                    lastName = _lastName.value,
                    email = _email.value,
                    phoneNumber = _phoneNumber.value,
                    driverLicense = _driverLicense.value,
                    isBanned = _isBanned.value,
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