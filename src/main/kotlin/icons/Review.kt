package icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val ShieldCheckSvgrepoCom: ImageVector
    get() {
        if (_ShieldCheckSvgrepoCom != null) {
            return _ShieldCheckSvgrepoCom!!
        }
        _ShieldCheckSvgrepoCom = ImageVector.Builder(
            name = "ShieldCheckSvgrepoCom",
            defaultWidth = 800.dp,
            defaultHeight = 800.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(8f, 16f)
                lineTo(4.35f, 13.393f)
                curveTo(2.248f, 11.891f, 1f, 9.467f, 1f, 6.883f)
                verticalLineTo(3f)
                lineTo(8f, 0f)
                lineTo(15f, 3f)
                verticalLineTo(6.883f)
                curveTo(15f, 9.467f, 13.752f, 11.891f, 11.65f, 13.393f)
                lineTo(8f, 16f)
                close()
                moveTo(12.207f, 5.707f)
                lineTo(10.793f, 4.293f)
                lineTo(7f, 8.086f)
                lineTo(5.207f, 6.293f)
                lineTo(3.793f, 7.707f)
                lineTo(7f, 10.914f)
                lineTo(12.207f, 5.707f)
                close()
            }
        }.build()

        return _ShieldCheckSvgrepoCom!!
    }

@Suppress("ObjectPropertyName")
private var _ShieldCheckSvgrepoCom: ImageVector? = null
