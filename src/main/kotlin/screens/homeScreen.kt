package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import components.HomeScreenComponent

@Composable
fun homeScreen(component: HomeScreenComponent){
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFF272727)
            )
            .fillMaxSize()
    )
}