package widgets

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = @Composable {},
    isVisible: Boolean,
    onDismiss: () -> Unit) {
    // Анимация для отображения/скрытия Bottom Sheet
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        content()
    }
}