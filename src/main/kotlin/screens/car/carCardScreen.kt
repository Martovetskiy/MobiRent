package screens.car

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import api.car.CarResponse
import carMakes
import carModels
import components.car.CarCardScreenComponent
import icons.CarSvgrepoCom
import isColorHexValid
import isNumberPlateValid
import isPricePerDayValid
import isYearValid
import widgets.PopupNotification
import java.time.OffsetDateTime

@Composable
fun carCardScreen(component: CarCardScreenComponent)
{
    val make = remember { mutableStateOf(component.car.value?.make ?: "") }
    val model = remember { mutableStateOf(component.car.value?.model ?: "")}
    val year = remember { mutableStateOf(component.car.value?.year.toString())}
    val colorHex = remember { mutableStateOf(component.car.value?.colorHex?: "")}
    val pricePerDay = remember { mutableStateOf(component.car.value?.pricePerDay.toString())}
    val numberPlate = remember { mutableStateOf(component.car.value?.numberPlate?: "")}
    val status = remember { mutableStateOf(component.car.value?.status?: "")}

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

    val isEdit = remember { mutableStateOf(false) }

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
                text = "Карточка машины с номером ${component.car.value?.numberPlate ?: "Number"}",
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
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth(0.3f),
                onClick = { isEdit.value = !isEdit.value }
            )
            {
                Text(
                    text = if (isEdit.value) "Перейти в режим редактирования" else "Перейти в режим просмотра",
                    style = TextStyle(
                        textDirection = TextDirection.Content,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.3f)) { // Добавляем отступ для Divider
                Box (modifier = Modifier.fillMaxWidth()
                    .background(color = Color.LightGray, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .clickable {
                       if (isEdit.value) expandedMake = true
                    }.border(1.dp, Color.LightGray, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                    contentAlignment = Alignment.CenterStart) {
                    Text(
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
                    .clickable {
                        if (isEdit.value) expandedModel = true
                    }.border(1.dp, Color.LightGray, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                    contentAlignment = Alignment.CenterStart) {
                    Text(
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
                    enabled = isEdit.value,
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
                                enabled = isEdit.value,
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

            if (isEdit.value) {
                Column(Modifier.fillMaxWidth(0.3f)) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colorScheme.primary
                        ),
                        enabled =
                        make.value.isNotEmpty()
                                && model.value.isNotEmpty()
                                && isNumberPlateValid(numberPlate.value)
                                && isYearValid(year.value)
                                && isColorHexValid(colorHex.value)
                                && isPricePerDayValid(pricePerDay.value),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            component.carTemp.value = CarResponse(
                                carId = component.car.value?.carId ?: 1,
                                make = make.value,
                                model = model.value,
                                year = year.value.toInt(),
                                colorHex = colorHex.value,
                                pricePerDay = pricePerDay.value.toDouble(),
                                numberPlate = numberPlate.value,
                                status = status.value,
                                createAt = component.car.value?.createAt ?: OffsetDateTime.now()
                            )
                            component.putData(component.carTemp.value!!)
                        }
                    ) {
                        Text(
                            text = "Сохранить изменения",
                            style = TextStyle(
                                textDirection = TextDirection.Content,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        )
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red
                        ),
                        onClick = {
                            component.deleteDate()
                            component.goBack()
                        }
                    ) {
                        Text(
                            text = "Удалить",
                            style = TextStyle(
                                textDirection = TextDirection.Content,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
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
    }

    PopupNotification(
        showPopup = component.showPopup,
        text = component.textPopup.value
    )


}