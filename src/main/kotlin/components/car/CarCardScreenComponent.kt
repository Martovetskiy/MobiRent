package components.car

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.car.CarResponse
import api.car.deleteCar
import api.car.putCar
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import navigation.DecomposeNav.Configuration

class CarCardScreenComponent (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>,
    carF: CarResponse? = null
) : ComponentContext by componentContext
{
    private val _car: MutableState<CarResponse?> = mutableStateOf(carF)
    private val _carTemp: MutableState<CarResponse?> = mutableStateOf(_car.value)
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")

    val showPopup = _showPopup
    val textPopup = _textPopup
    val car = _car
    val carTemp = _carTemp

    fun goBack() {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.CarViewScreen)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun putData(car: CarResponse){
        GlobalScope.launch {
            try {
                _car.value = putCar(car)
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
                _car.value = deleteCar(_car.value!!.carId)
                _textPopup.value = "Автомобиль удален"
                _showPopup.value = true
            } catch (e: Exception) {
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }


}