package navigation

import api.customer.CustomerResponse
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import components.HomeScreenComponent
import components.car.CarsViewScreenComponent
import components.customer.CustomerAddScreenComponent
import components.customer.CustomerCardScreenComponent
import components.customer.CustomersViewScreenComponent
import components.payment.PaymentsViewScreenComponent
import components.rental.RentalsViewScreenComponent
import components.review.ReviewsViewScreenComponent
import kotlinx.serialization.Serializable

class DecomposeNav(
    componentContext: ComponentContext
): ComponentContext by componentContext

{
    val navigation = StackNavigation<Configuration>()

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.HomeScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when (config) {
            is Configuration.HomeScreen -> Child.HomeScreen(
                component = HomeScreenComponent(
                    componentContext = context,
                    navigation
                )
            )

            //Customer
            is Configuration.CustomersViewScreen -> Child.CustomersViewScreen(
                component = CustomersViewScreenComponent(
                    componentContext = context,
                    navigation
                )
            )

            is Configuration.CustomerCardScreen -> Child.CustomerCardScreen(
                component = CustomerCardScreenComponent(
                    componentContext = context,
                    navigation,
                    customerF = config.customerF
                )
            )

            is Configuration.CustomerAddScreen -> Child.CustomerAddScreen(
                component = CustomerAddScreenComponent(
                    componentContext = context,
                    navigation,
                )
            )

            //Car
            is Configuration.CarViewScreen -> Child.CarViewScreen(
                component = CarsViewScreenComponent(
                    componentContext = context,
                    navigation
                )
            )

            //Rental
            is Configuration.RentalsViewScreen -> Child.RentalsViewScreen(
                component = RentalsViewScreenComponent(
                    componentContext = context,
                    navigation
                )
            )

            //Payment
            is Configuration.PaymentsViewScreen -> Child.PaymentsViewScreen(
                component = PaymentsViewScreenComponent(
                    componentContext = context,
                    navigation
                )
            )

            //Car
            is Configuration.ReviewViewScreen -> Child.ReviewViewScreen(
                component = ReviewsViewScreenComponent(
                    componentContext = context,
                    navigation
                )
            )
        }
    }

    sealed class Child {
        data class HomeScreen(val component: HomeScreenComponent) : Child()

        //Customer
        data class CustomersViewScreen(val component: CustomersViewScreenComponent) : Child()
        data class CustomerCardScreen(val component: CustomerCardScreenComponent) : Child()
        data class CustomerAddScreen(val component: CustomerAddScreenComponent) : Child()
        //Car
        data class CarViewScreen(val component: CarsViewScreenComponent) : Child()

        //Rental
        data class RentalsViewScreen(val component: RentalsViewScreenComponent) : Child()

        //Payment
        data class PaymentsViewScreen(val component: PaymentsViewScreenComponent) : Child()

        //Review
        data class ReviewViewScreen(val component: ReviewsViewScreenComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        data object HomeScreen : Configuration()

        //Customer
        data object CustomersViewScreen : Configuration()

        data class CustomerCardScreen(
            val customerF: CustomerResponse?
        ) : Configuration()

        data object CustomerAddScreen : Configuration()

        //Car
        data object CarViewScreen : Configuration()

        //Rental
        data object RentalsViewScreen : Configuration()

        //Payment
        data object PaymentsViewScreen : Configuration()

        //Review
        data object ReviewViewScreen : Configuration()
    }
}