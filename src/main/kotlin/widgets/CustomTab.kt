package widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CustomTab(
    icon: ImageVector? = null,
    selected: Boolean = false,
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    selectedColor: Color = Color(0xFFd6aeff),
    unselectedColor: Color = Color.White,
){
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable (role = Role.Tab) {
                println(selected)
                onClick() },
        contentAlignment = Alignment.Center
    )
    {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
                .background(
                    if (selected) selectedColor else Color.Transparent
                )
                .align(Alignment.CenterStart)
        )

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            if (icon != null)
            {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = icon,
                    contentDescription = "Tab icon",
                    tint = if (selected) selectedColor else unselectedColor
                )
            }
            Text(
                text = text,
                style = TextStyle(
                    color = if (selected) selectedColor else unselectedColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }

    }

}