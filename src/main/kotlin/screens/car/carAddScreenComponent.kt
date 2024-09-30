package screens.car

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import api.car.CarRequest
import carMakes
import carModels
import components.car.CarAddScreenComponent
import icons.CarSvgrepoCom
import isColorHexValid
import isNumberPlateValid
import isPricePerDayValid
import isYearValid
import widgets.PopupNotification
import java.time.OffsetDateTime

@Composable
fun carAddScreen(component: CarAddScreenComponent)
{
    val make = remember { mutableStateOf("") }
    val model = remember { mutableStateOf("") }
    val year = remember { mutableStateOf("") }
    val colorHex = remember { mutableStateOf("") }
    val pricePerDay = remember { mutableStateOf("") }
    val numberPlate = remember { mutableStateOf("") }
    val status = remember { mutableStateOf("Available") }

    var expandedMake by remember { mutableStateOf(false) }
    var expandedModel by remember { mutableStateOf(false) }

    val associativeMapRu = mapOf(
        "make" to "Марка",
        "model" to "Модель",
        "year" to "Год",
        "colorHex" to "Цвет, hex",
        "pricePerDay" to "Цена за день",
        "numberPlate" to "Гос. номер",
        "status" to "Статус",
        "createAt" to "Дата создания карточки"
    )

    val statusMap = mapOf(
        "Available" to "Доступна",
        "Rented" to "Забронированна",
        "UnderMaintenance" to "Тех. обслуживание"
    )

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Добавить автомобиль",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(200.dp)),

                )
            {
                Icon(
                    imageVector = CarSvgrepoCom,
                    contentDescription = "Car Image",
                    tint = Color.Black,
                    modifier = Modifier.size(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.3f)) { // Добавляем отступ для Divider
                Box (modifier = Modifier.fillMaxWidth()
                    .background(color = Color.LightGray, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .clickable { expandedMake = true }.border(1.dp, Color.LightGray, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                    contentAlignment = Alignment.CenterStart) {
                    androidx.compose.material.Text(
                        text = "Марка: ${make.value.ifEmpty { "Не выбрана" }}",
                        modifier = Modifier.padding(16.dp)
                    )
                    DropdownMenu(

                        expanded = expandedMake,
                        onDismissRequest = { expandedMake = false }
                    ) {
                        carMakes.sorted().forEach { carMake ->
                            DropdownMenuItem(onClick = {
                                make.value = carMake
                                model.value = "" // Reset model when make changes
                                expandedMake = false
                            }) {
                                Text(text = carMake)
                            }
                        }
                    }
                }

                Divider(
                    color = if (make.value.isNotEmpty()) Color.Green else Color.Red,
                    thickness = 1.dp, // Толщина границы
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter) // Выровнять по центру снизу
                )
            }

            Box(modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.3f)) { // Добавляем отступ для Divider
                Box (modifier = Modifier.fillMaxWidth()
                    .background(color = Color.LightGray, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .clickable { expandedModel = true }.border(1.dp, Color.LightGray, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                    contentAlignment = Alignment.CenterStart) {
                    androidx.compose.material.Text(
                        text = "Модель: ${model.value.ifEmpty { "Не выбрана" }}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                DropdownMenu(
                    expanded = expandedModel,
                    onDismissRequest = { expandedModel = false }
                ) {
                    // Show models based on selected make
                    carModels[make.value]?.sorted()?.forEach { carModel ->
                        DropdownMenuItem(onClick = {
                            model.value = carModel
                            expandedModel = false
                        }) {
                            Text(text = carModel)
                        }
                    }
                }

                Divider(
                    color = if (model.value.isNotEmpty()) Color.Green else Color.Red,
                    thickness = 1.dp, // Толщина границы
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter) // Выровнять по центру снизу
                )
            }

            associativeMapRu.keys.toList().slice(2..5).forEach { key ->
                TextField(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(0.3f),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.LightGray,
                        errorIndicatorColor = Color.Red,
                        errorCursorColor = Color.Red
                    ),
                    singleLine = true,
                    maxLines = 1,
                    isError = !when (key) {
                        "year" -> isYearValid(year.value)
                        "colorHex" -> isColorHexValid(colorHex.value)
                        "pricePerDay" -> isPricePerDayValid(pricePerDay.value)
                        "numberPlate" -> isNumberPlateValid(numberPlate.value)
                        else -> false
                    },
                    value = when (key) {
                        "year" -> year.value
                        "colorHex" -> colorHex.value
                        "pricePerDay" -> pricePerDay.value
                        "numberPlate" -> numberPlate.value
                        else -> ""
                    },
                    onValueChange = {
                        when (key) {
                            "year" -> year.value = it
                            "colorHex" -> colorHex.value = it
                            "pricePerDay" -> pricePerDay.value = it
                            "numberPlate" -> numberPlate.value = it
                        }

                    },
                    label = {
                        Text(
                            text = when (key) {
                                "year" -> associativeMapRu[key] ?: ""
                                "colorHex" -> associativeMapRu[key] ?: ""
                                "pricePerDay" -> associativeMapRu[key] ?: ""
                                "numberPlate" -> associativeMapRu[key] ?: ""
                                else -> ""
                            }
                        )
                    },

                    )

            }


            Box (
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(bottom = 8.dp),
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "Статус: ")
                    statusMap.keys.forEach { key ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = status.value == key,
                                onClick = {
                                    status.value = key
                                }
                            )
                            Text(text = "${statusMap[key]}")
                        }
                    }
                }
            }


            Button(
                enabled =
                    make.value.isNotEmpty()
                            && model.value.isNotEmpty()
                            && isNumberPlateValid(numberPlate.value)
                            && isYearValid(year.value)
                            && isColorHexValid(colorHex.value)
                            && isPricePerDayValid(pricePerDay.value),
                modifier = Modifier.fillMaxWidth(0.3f),
                onClick = {
                    component.car.value = CarRequest(
                        make = make.value,
                        model = model.value,
                        year = year.value.toInt(),
                        colorHex = colorHex.value,
                        pricePerDay = pricePerDay.value.toDouble(),
                        numberPlate = numberPlate.value,
                        status = status.value,
                        createAt = OffsetDateTime.now()
                    )
                    component.postData()
                },
            ) {
                Text(
                    text = "Добавить", style = TextStyle(
                        textDirection = TextDirection.Content,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
            }
        }
        IconButton(
            onClick = {
                component.goBack()
            }
        )
        {
            Icon(
                imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                contentDescription = "Back to",
                tint = Color.White
            )
        }
        PopupNotification(
            showPopup = component.showPopup,
            text = component.textPopup.value
        )
    }
}
