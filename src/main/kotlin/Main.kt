import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lock
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import api.isServerConnected
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import icons.*
import kotlinx.coroutines.delay
import navigation.DecomposeNav
import screens.car.carAddScreen
import screens.car.carCardScreen
import screens.car.carsViewScreen
import screens.customer.customerAddScreen
import screens.customer.customerCardScreen
import screens.customer.customersViewScreen
import screens.homeScreen
import screens.payment.paymentsViewScreen
import screens.rental.rentalAddScreen
import screens.rental.rentalCardScreen
import screens.rental.rentalsViewScreen
import screens.review.reviewsViewScreen
import widgets.CustomTab


@Composable
fun mainMenu(
    root: DecomposeNav,
    appBar: @Composable () -> Unit = @Composable {},
    content: @Composable () -> Unit = @Composable {},
    backgroundColor: Color = Color(0xFF0b0b0b)
)
{
    val childStack by root.childStack.subscribeAsState()
    var isConnected by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            isConnected = isServerConnected("http://localhost:5022/")
            delay(2000) // Ждем 10 секунд перед следующей проверкой
        }
    }
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(width = 0.1.dp, color = Color.LightGray.copy(alpha = 0.3f))
        )
        {
            Column(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .background(color = backgroundColor)
                    .padding(bottom = 28.dp)
            )
            {
                Spacer(modifier = Modifier.height(40.dp))

                CustomTab(
                    modifier = Modifier.height(60.dp),
                    selected = childStack.active.configuration == DecomposeNav.Configuration.CustomersViewScreen,
                    icon = Icons.Sharp.Person,
                    text = "Клиенты",
                    onClick = {
                        root.navigation.popTo(0)
                        root.navigation.pushNew(DecomposeNav.Configuration.CustomersViewScreen)
                    }
                )

                CustomTab(
                    modifier = Modifier.height(60.dp),
                    selected = childStack.active.configuration == DecomposeNav.Configuration.CarViewScreen,
                    icon = CarSvgrepoCom,
                    text = "Автомобили",
                    onClick = {
                        root.navigation.popTo(0)
                        root.navigation.pushNew(DecomposeNav.Configuration.CarViewScreen)
                    }
                )

                CustomTab(
                    modifier = Modifier.height(60.dp),
                    selected = childStack.active.configuration == DecomposeNav.Configuration.RentalsViewScreen,
                    icon = MoneyBankCheckPaymentChequeFinanceBusinessSvgrepoCom,
                    text = "Аренды",
                    onClick = {
                        root.navigation.popTo(0)
                        root.navigation.pushNew(DecomposeNav.Configuration.RentalsViewScreen)
                    }
                )

                CustomTab(
                    modifier = Modifier.height(60.dp),
                    selected = childStack.active.configuration == DecomposeNav.Configuration.PaymentsViewScreen,
                    icon = DollarSvgrepoCom,
                    text = "Платежи",
                    onClick = {
                        root.navigation.popTo(0)
                        root.navigation.pushNew(DecomposeNav.Configuration.PaymentsViewScreen)
                    }
                )

                CustomTab(
                    modifier = Modifier.height(60.dp),
                    selected = childStack.active.configuration == DecomposeNav.Configuration.ReviewViewScreen,
                    icon = ShieldCheckSvgrepoCom,
                    text = "Отзывы",
                    onClick = {
                        root.navigation.popTo(0)
                        root.navigation.pushNew(DecomposeNav.Configuration.ReviewViewScreen)
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(color = backgroundColor),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Box(
                    modifier = Modifier
                        .width(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Lock,
                        contentDescription = "Logo",
                        tint = Color.White,
                    )
                }
                appBar()
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(start = 80.dp, top = 40.dp, bottom = 28.dp)
                    .fillMaxSize()
            )
            {
                content()
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()

            )
            {
                ConnectionStatusBar(isConnected)
            }
        }
    }
}

// Composable функция для отображения состояния соединения
@Composable
fun ConnectionStatusBar(isConnected: Boolean) {
    val color = if (isConnected) {
        Color(0xFF039660)// Подключен
    } else {
        Color.Red // Отключен
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .height(28.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        // Текст состояния подключения
        Text(
            text = if (isConnected) "Сервер доступен" else "Сервер недоступен",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(0.dp)
        )
    }
}


@Composable
@Preview
fun App(root: DecomposeNav) {
    val childStack by root.childStack.subscribeAsState()
    Children(
        modifier = Modifier.background(color = Color(0xFF272727)),
        stack = childStack
    )
    { child ->
        when (val instance = child.instance) {
            is DecomposeNav.Child.HomeScreen -> homeScreen(instance.component)

            //Customer
            is DecomposeNav.Child.CustomersViewScreen -> customersViewScreen(instance.component)
            is DecomposeNav.Child.CustomerCardScreen -> customerCardScreen(instance.component)
            is DecomposeNav.Child.CustomerAddScreen -> customerAddScreen(instance.component)

            //Car
            is DecomposeNav.Child.CarViewScreen -> carsViewScreen(instance.component)
            is DecomposeNav.Child.CarCardScreen -> carCardScreen(instance.component)
            is DecomposeNav.Child.CarAddScreen -> carAddScreen(instance.component)

            //Rental
            is DecomposeNav.Child.RentalsViewScreen -> rentalsViewScreen(instance.component)
            is DecomposeNav.Child.RentalCardScreen -> rentalCardScreen(instance.component)
            is DecomposeNav.Child.RentalAddScreen -> rentalAddScreen(instance.component)

            //Payment
            is DecomposeNav.Child.PaymentsViewScreen -> paymentsViewScreen(instance.component)

            //Customer
            is DecomposeNav.Child.ReviewViewScreen -> reviewsViewScreen(instance.component)
        }
    }

}

fun main() = application {
    val root = remember {
        DecomposeNav(DefaultComponentContext(LifecycleRegistry()))
    }
    val state = rememberWindowState(
        position = WindowPosition(Alignment.Center),
        size = DpSize(1635.dp, 920.dp),
    )
    Window(
        state = state,
        resizable = true,
        onCloseRequest = ::exitApplication,
        undecorated = true
    ) {
        mainMenu(
            root,
            appBar = {
                AppBar(
                    state = state,
                    onClose = ::exitApplication
                )
            },
            content = {
                App(root)
            }
        )

    }
}


@Composable
fun WindowScope.AppBar(
    modifier: Modifier = Modifier,
    state: WindowState,
    onClose: () -> Unit,
    onMinimize: () -> Unit = { state.isMinimized = state.isMinimized.not() },
    onMaximize: () -> Unit = {
        state.placement = if (state.placement == WindowPlacement.Maximized)
            WindowPlacement.Floating else WindowPlacement.Maximized
    },
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent),
        contentAlignment = Alignment.Center
    )
    {
        WindowDraggableArea {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            )
            {
                IconButton(onClick = onMinimize) {
                    Icon(
                        modifier = Modifier.size(15.dp),
                        imageVector = Minimize,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                val isFloating = state.placement == WindowPlacement.Floating
                IconButton(onClick = onMaximize) {
                    Icon(
                        modifier = Modifier.size(15.dp),
                        imageVector =  if (isFloating) Expand else Compress ,
                        contentDescription = null,
                        tint = Color.White)
                }
                IconButton(onClick = onClose) {
                    Icon(
                        modifier = Modifier.size(15.dp),
                        imageVector = Close,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}