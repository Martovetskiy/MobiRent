package navigation

import api.car.CarResponse
import api.customer.CustomerResponse
import api.rental.RentalResponse
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import components.HomeScreenComponent
import components.car.CarAddScreenComponent
import components.car.CarCardScreenComponent
import components.car.CarsViewScreenComponent
import components.customer.CustomerAddScreenComponent
import components.customer.CustomerCardScreenComponent
import components.customer.CustomersViewScreenComponent
import components.payment.PaymentsViewScreenComponent
import components.rental.RentalAddScreenComponent
import components.rental.RentalCardScreenComponent
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

            is Configuration.CarCardScreen -> Child.CarCardScreen(
                component = CarCardScreenComponent(
                    componentContext = context,
                    navigation,
                    carF = config.carF
                )
            )

            is Configuration.CarAddScreen -> Child.CarAddScreen(
                component = CarAddScreenComponent(
                    componentContext = context,
                    navigation,
                )
            )
            //Rental
            is Configuration.RentalsViewScreen -> Child.RentalsViewScreen(
                component = RentalsViewScreenComponent(
                    componentContext = context,
                    navigation
                )
            )

            is Configuration.RentalCardScreen -> Child.RentalCardScreen(
                component = RentalCardScreenComponent(
                    componentContext = context,
                    navigation,
                    rentalF = config.rentalF
                )
            )

            is Configuration.RentalAddScreen -> Child.RentalAddScreen(
                component = RentalAddScreenComponent(
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
        data class CarCardScreen(val component: CarCardScreenComponent) : Child()
        data class CarAddScreen(val component: CarAddScreenComponent) : Child()

        //Rental
        data class RentalsViewScreen(val component: RentalsViewScreenComponent) : Child()
        data class RentalCardScreen(val component: RentalCardScreenComponent) : Child()
        data class RentalAddScreen(val component: RentalAddScreenComponent) : Child()
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

        data class CarCardScreen(
            val carF: CarResponse?
        ) : Configuration()

        data object CarAddScreen : Configuration()

        //Rental
        data object RentalsViewScreen : Configuration()

        data class RentalCardScreen(
            val rentalF: RentalResponse?
        ) : Configuration()

        data object RentalAddScreen : Configuration()

        //Payment
        data object PaymentsViewScreen : Configuration()

        //Review
        data object ReviewViewScreen : Configuration()
    }
}