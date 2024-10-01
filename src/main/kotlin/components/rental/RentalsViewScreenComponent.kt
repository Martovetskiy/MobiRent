package components.rental

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import api.rental.RentalResponseViewDto
import api.rental.getAllRental
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import navigation.DecomposeNav.Configuration

class RentalsViewScreenComponent  (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>
) : ComponentContext by componentContext
{
    private val _listRentals: MutableState<List<RentalResponseViewDto>> = mutableStateOf(listOf())
    private val _email: MutableState<String?> = mutableStateOf(null)
    private val _make: MutableState<String?> = mutableStateOf(null)
    private val _model: MutableState<String?> = mutableStateOf(null)
    private val _startDate: MutableState<String?> = mutableStateOf(null)
    private val _endDate: MutableState<String?> = mutableStateOf(null)
    private val _totalPrice: MutableState<String?> = mutableStateOf(null)
    private val _createAt: MutableState<String?> = mutableStateOf(null)
    private val _sortBy: MutableState<String> = mutableStateOf("email")
    private val _sortDirection: MutableState<String> = mutableStateOf("ASC")
    private val _showPopup: MutableState<Boolean> = mutableStateOf(false)
    private val _textPopup: MutableState<String> = mutableStateOf("")

    val showPopup = _showPopup
    val textPopup = _textPopup

    val listRentals = _listRentals
    val email = _email
    val make = _make
    val model = _model
    val startDate = _startDate
    val endDate = _endDate
    val totalPrice = _totalPrice

    val sortBy = _sortBy
    val sortDirection = _sortDirection

    init {
        getData()

    }

    fun goToCard(rental: RentalResponseViewDto)
    {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.RentalCardScreen(rental.rentalId))
    }

    fun goToAdd()
    {
        _navigation.popTo(0)
        _navigation.pushNew(Configuration.RentalAddScreen)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getData(){
        GlobalScope.launch {
            try {
                _listRentals.value = getAllRental(
                    email = _email.value,
                    make = _make.value,
                    model = _model.value,
                    startDate = _startDate.value,
                    endDate = _endDate.value,
                    totalPrice = _totalPrice.value,
                    createAt = _createAt.value,
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