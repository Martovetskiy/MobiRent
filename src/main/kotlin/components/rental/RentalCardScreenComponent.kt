package components.rental

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.car.CarResponse
import api.car.getAllCar
import api.car.getCarById
import api.customer.CustomerResponse
import api.customer.getAllCustomer
import api.customer.getCustomerById
import api.rental.*
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

class RentalCardScreenComponent (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>,
    id: Long
) : ComponentContext by componentContext
{
    private val _id = id
    private val _rental: MutableState<RentalResponse?> = mutableStateOf(null)
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")
    private val _customers: MutableState<List<CustomerResponse>> = mutableStateOf(listOf())
    private val _cars: MutableState<List<CarResponse>> = mutableStateOf(listOf())
    private val _customerP: MutableState<CustomerResponse?> = mutableStateOf(null)
    private val _carP: MutableState<CarResponse?> =  mutableStateOf(null)
    private val _startDate: MutableState<String> = mutableStateOf("")
    private val _endDate: MutableState<String> = mutableStateOf("")

    val showPopup = _showPopup
    val textPopup = _textPopup
    val rental = _rental

    val customers = _customers
    val cars = _cars
    val customerP = _customerP
    val carP = _carP
    val startDate = _startDate
    val endDate = _endDate

    init {
        getData()
    }

    fun goBack() {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.RentalsViewScreen)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getData(){
        GlobalScope.launch {
            try {
                _rental.value = getRentalById(_id)
                _startDate.value = _rental.value!!.startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale("ru")))
                _endDate.value = _rental.value!!.endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale("ru")))
                _customers.value = getAllCustomer()
                _cars.value = getAllCar()
                _customerP.value = getCustomerById(_rental.value!!.customerId)
                _carP.value = getCarById(_rental.value!!.carId)
            }
            catch (e: Exception){
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun putData(rental: RentalRequest){
        GlobalScope.launch {
            try {
                var res = putRental(_id, rental)

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
                deleteRental(_rental.value!!.rentalId)
                _textPopup.value = "Аренда удалена"
                _showPopup.value = true
            } catch (e: Exception) {
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }


}