package components.payment

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import navigation.DecomposeNav.Configuration

class PaymentsViewScreenComponent  (
    componentContext: ComponentContext,
    private val _navigation: StackNavigation<Configuration>
) : ComponentContext by componentContext
