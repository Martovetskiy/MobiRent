package components.rental

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.car.CarResponse
import api.car.getAllCar
import api.customer.CustomerResponse
import api.customer.getAllCustomer
import api.rental.RentalRequest
import api.rental.postRental
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import navigation.DecomposeNav.Configuration

class RentalAddScreenComponent (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>
) : ComponentContext by componentContext
{
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")
    private val _rental: MutableState<RentalRequest?> = mutableStateOf(null)
    private val _customers: MutableState<List<CustomerResponse>> = mutableStateOf(listOf())
    private val _cars: MutableState<List<CarResponse>> = mutableStateOf(listOf())

    val showPopup = _showPopup
    val textPopup = _textPopup
    val rental = _rental

    val customers = _customers
    val cars = _cars

    fun goBack() {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.RentalsViewScreen)
    }
    init {
        getData()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getData(){
        GlobalScope.launch {
            try {
                _customers.value = getAllCustomer()
                _cars.value = getAllCar()
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
                postRental(_rental.value!!)
                _textPopup.value = "Аренда добавлена"
                _showPopup.value = true
            }
            catch (e: Exception){
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }
}