package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import components.HomeScreenComponent

@Composable
fun homeScreen(component: HomeScreenComponent){
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFF272727)
            )
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Text(
            text = "Похоже, вы не выбрали ни один метод...",
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
        )
    }

}