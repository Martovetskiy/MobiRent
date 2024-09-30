package components.rental

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.car.CarResponse
import api.car.getAllCar
import api.customer.CustomerResponse
import api.customer.getAllCustomer
import api.rental.RentalResponse
import api.rental.deleteRental
import api.rental.putRental
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import navigation.DecomposeNav.Configuration

class RentalCardScreenComponent (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>,
    rentalF: RentalResponse? = null
) : ComponentContext by componentContext
{
    private val _rental: MutableState<RentalResponse?> = mutableStateOf(rentalF)
    private val _rentalTemp: MutableState<RentalResponse?> = mutableStateOf(_rental.value)
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")
    private val _customers: MutableState<List<CustomerResponse>> = mutableStateOf(listOf())
    private val _cars: MutableState<List<CarResponse>> = mutableStateOf(listOf())

    val showPopup = _showPopup
    val textPopup = _textPopup
    val rental = _rental

    val customers = _customers
    val cars = _cars
    val rentalTemp = _rentalTemp



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
    fun putData(rental: RentalResponse){
        GlobalScope.launch {
            try {
                _rental.value = putRental(rental)
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
                _rental.value = deleteRental(_rental.value!!.rentalId)
                _textPopup.value = "Аренда удалена"
                _showPopup.value = true
            } catch (e: Exception) {
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }


}