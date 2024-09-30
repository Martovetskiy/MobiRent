package components.car

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.car.CarRequest
import api.car.postCar
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import navigation.DecomposeNav.Configuration

class CarAddScreenComponent (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>
) : ComponentContext by componentContext
{
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")
    private val _car: MutableState<CarRequest?> = mutableStateOf(null)

    val showPopup = _showPopup
    val textPopup = _textPopup
    val car = _car

    fun goBack() {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.CarViewScreen)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun postData(){
        GlobalScope.launch {
            try {
                postCar(_car.value!!)
                _textPopup.value = "Автомобиль добавлен"
                _showPopup.value = true
            }
            catch (e: Exception){
                _textPopup.value = e.message.toString()
                _showPopup.value = true
            }
        }
    }
}