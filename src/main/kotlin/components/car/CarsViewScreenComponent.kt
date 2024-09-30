package components.car

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.car.CarResponse
import api.car.getAllCar
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import navigation.DecomposeNav.Configuration

class CarsViewScreenComponent (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>
) : ComponentContext by componentContext
{
    private val _listCars: MutableState<List<CarResponse>> = mutableStateOf(listOf())

    private val _make: MutableState<String?> = mutableStateOf(null)
    private val _model: MutableState<String?> = mutableStateOf(null)
    private val _year: MutableState<String?> = mutableStateOf(null)
    private val _colorHex: MutableState<String?> = mutableStateOf(null)
    private val _pricePerDay: MutableState<String?> = mutableStateOf(null)
    private val _numberPlate: MutableState<String?> = mutableStateOf(null)
    private val _status: MutableState<String?> = mutableStateOf(null)

    private val _sortBy: MutableState<String> = mutableStateOf("make")
    private val _sortDirection: MutableState<String> = mutableStateOf("ASC")
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")

    val showPopup = _showPopup
    val textPopup = _textPopup

    val listCars = _listCars
    val make = _make
    val model = _model
    val year = _year
    val colorHex = _colorHex
    val pricePerDay = _pricePerDay
    val numberPlate = _numberPlate
    val status = _status
    val sortBy = _sortBy
    val sortDirection = _sortDirection

    init {
        getData()

    }

    fun goToCard(car: CarResponse)
    {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.CarCardScreen(car))
    }

    fun goToAdd()
    {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.CarAddScreen)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getData(){
        GlobalScope.launch {
            try {
                _listCars.value = getAllCar(
                    make = _make.value,
                    model = _model.value,
                    year = _year.value,
                    colorHex = _colorHex.value,
                    pricePerDay = _pricePerDay.value,
                    numberPlate = _numberPlate.value,
                    status = _status.value,
                    sortBy = _sortBy.value,
                    sortDirection = _sortDirection.value,
                )
            }
            catch (e: Exception){
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }
}
