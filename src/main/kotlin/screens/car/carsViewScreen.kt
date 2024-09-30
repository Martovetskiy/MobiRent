package screens.car

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import api.car.CarResponse
import components.car.CarsViewScreenComponent
import icons.FilterSvgrepoCom
import widgets.BottomSheet
import widgets.PopupNotification
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun carsViewScreen(
    component: CarsViewScreenComponent
){
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

    val fMap = mapOf(
        "make" to 1f,
        "model" to 1f,
        "year" to 0.7f,
        "colorHex" to 1f,
        "pricePerDay" to 1f,
        "numberPlate" to 1f,
        "status" to  1f,
        "createAt" to 1.5f
    )


    var isBottomSheetVisible by remember { mutableStateOf(false) }
    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "dbo.Cars",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    ),
                )
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(
                        onClick = {
                            component.getData()
                        }
                    ){
                        Icon(
                            imageVector = Icons.Sharp.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier.clickable {
                            component.goToAdd()
                                                      },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            color = Color.White,
                            text = "Добавить",
                            style = TextStyle(fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(8.dp)
                        )
                        Icon(imageVector = Icons.Sharp.Add, contentDescription = "Add", tint = Color.White)
                    }
                }
            }
            CarTable(
                component.listCars.value,
                associativeMapRu,
                fMap,
                onClick = {
                    component.goToCard(it)
                }
            )
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp),
            onClick = {
                isBottomSheetVisible = !isBottomSheetVisible
            },
            backgroundColor = Color(0xFFd6aeff)
        )
        {
            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp),
                imageVector = FilterSvgrepoCom,
                contentDescription = "Filter",
                tint = Color.White
            )
        }
        BottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter),
            isVisible = isBottomSheetVisible,
            onDismiss = { isBottomSheetVisible = false },
            content = {
                Filter(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .fillMaxHeight(0.55f)
                        .background(color = Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    associativeMapRu = associativeMapRu,
                    onClick = {make, model, year, colorHex, pricePerDay, numberPlate, status, sortBy, sortDirection ->
                        component.make.value = make
                        component.model.value = model
                        component.year.value = year
                        component.colorHex.value = colorHex
                        component.pricePerDay.value = pricePerDay
                        component.numberPlate.value = numberPlate
                        component.status.value = status
                        component.sortBy.value = sortBy
                        component.sortDirection.value = sortDirection
                        component.getData()
                    }
                )
            }
        )

        PopupNotification(
            showPopup = component.showPopup,
            text = component.textPopup.value
        )

    }
}

@Composable
private fun CarTable(
    car: List<CarResponse>,
    associativeMapRu: Map<String, String>,
    fMap: Map<String, Float>,
    onClick: (CarResponse) -> Unit = {}
)
{
    val statusMap = mapOf(
        "Available" to "Доступна",
        "Rented" to "Забронированна",
        "UnderMaintenance" to "Тех. обслуживание"
    )
    val maxHeight = mutableStateOf(56.dp)
    LazyColumn {
        item {
            Row {
                associativeMapRu.keys.forEach { key ->
                    Box(
                        modifier = Modifier
                            .defaultMinSize(minHeight = maxHeight.value)
                            .weight(fMap[key]?: 1f)
                            .border(width = 1.dp, Color.Gray)
                            .heightIn(max = 64.dp)
                            .onGloballyPositioned { coordinates ->
                                if (coordinates.size.height.dp > maxHeight.value) {
                                    (coordinates.size.height.dp)
                                    maxHeight.value = coordinates.size.height.dp
                                }
                            },
                        contentAlignment = Alignment.Center,

                        )
                    {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = associativeMapRu[key] ?: null.toString(),
                            style = TextStyle(
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                textDirection = TextDirection.Content,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
        items(car)
        {row ->
            Row (
                modifier = Modifier.clickable {
                    onClick(row)
                }
            ){
                associativeMapRu.keys.forEach { key ->
                    Box(
                        modifier = Modifier
                            .defaultMinSize(minHeight = maxHeight.value)
                            .weight(fMap[key]?: 1f)
                            .border(width = 1.dp, Color.Gray)
                            .heightIn(max = 64.dp)
                            .onGloballyPositioned { coordinates ->
                                if (coordinates.size.height.dp > maxHeight.value) {
                                    (coordinates.size.height.dp)
                                    maxHeight.value = coordinates.size.height.dp
                                }
                            },
                        contentAlignment = Alignment.Center,

                        )
                    {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = when (key) {
                                "make" -> row.make ?: "Unknown"
                                "model" -> row.model ?: "Unknown"
                                "year" -> (row.year ?: "Unknown").toString()
                                "colorHex" -> row.colorHex ?: "Unknown"
                                "pricePerDay" -> (row.pricePerDay?: "Unknown").toString()
                                "numberPlate" -> row.numberPlate ?: "Unknown"
                                "status" -> statusMap[row.status] ?: "Unknown"
                                "createAt" -> row.createAt?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))) ?: "Unknown"
                                else -> ""
                            },
                            style = TextStyle(
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                textDirection = TextDirection.Content,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Filter(
    modifier: Modifier = Modifier,
    associativeMapRu: Map<String, String>,
    onClick: (String, String, String, String, String, String, String, String, String) -> Unit
){
    val make = remember { mutableStateOf("") }
    val model = remember { mutableStateOf("")}
    val year = remember { mutableStateOf( "")}
    val colorHex = remember { mutableStateOf("")}
    val pricePerDay = remember { mutableStateOf("")}
    val numberPlate = remember { mutableStateOf("")}
    val status = remember { mutableStateOf("")}
    val sortBy = remember { mutableStateOf("make") }
    val sortDirection = remember { mutableStateOf("ASC")}

    var expandedSortBy by remember { mutableStateOf(false) }
    var expandedSortDirection by remember { mutableStateOf(false) }

    val mapSrtDirRU = mapOf(
        "DESC" to "возрастания",
        "ASC" to "убывания"
    )

    val statusMap = mapOf(
        "" to "Не выбрано",
        "Available" to "Доступна",
        "Rented" to "Забронированна",
        "UnderMaintenance" to "Тех. обслуживание"
    )

    Box (modifier = modifier) {
        Row (modifier = Modifier.padding(8.dp)){
            Column {
                associativeMapRu.keys.toList().slice(0..5).forEach { key ->
                    TextField(
                        value = when (key) {
                            "make" -> year.value
                            "model" -> year.value
                            "year" -> year.value
                            "colorHex" -> colorHex.value
                            "pricePerDay" -> pricePerDay.value
                            "numberPlate" -> numberPlate.value
                            else -> ""
                        },
                        onValueChange = {
                            when (key) {
                                "make" -> year.value = it
                                "model" -> year.value = it
                                "year" -> year.value = it
                                "colorHex" -> colorHex.value = it
                                "pricePerDay" -> pricePerDay.value = it
                                "numberPlate" -> numberPlate.value = it
                            }

                        },
                        label = {
                            Text(
                                text = when (key) {
                                    "make" -> associativeMapRu[key] ?: ""
                                    "model" -> associativeMapRu[key] ?: ""
                                    "year" -> associativeMapRu[key] ?: ""
                                    "colorHex" -> associativeMapRu[key] ?: ""
                                    "pricePerDay" -> associativeMapRu[key] ?: ""
                                    "numberPlate" -> associativeMapRu[key] ?: ""
                                    else -> ""
                                }
                            )
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Column {
                    Column(
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(4.dp)
                        ).fillMaxWidth().clickable { expandedSortBy = true }
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Сортировать по столбцу:",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = associativeMapRu[sortBy.value] ?: "",
                            modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownMenu(
                        expanded = expandedSortBy,
                        onDismissRequest = { expandedSortBy = false },
                        modifier = Modifier.border(BorderStroke(1.dp, Color.Gray))
                    ) {
                        associativeMapRu.keys.forEach { key ->
                            DropdownMenuItem(onClick = {
                                sortBy.value = key
                                expandedSortBy = false
                            }) {
                                Text(associativeMapRu[key] ?: "")
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(4.dp)
                        ).fillMaxWidth().clickable { expandedSortDirection = true }
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Сортировать в порядке:",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = mapSrtDirRU[sortDirection.value] ?: "",
                            modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownMenu(
                        expanded = expandedSortDirection,
                        onDismissRequest = { expandedSortDirection = false },
                        modifier = Modifier.border(BorderStroke(1.dp, Color.Gray))
                    ) {
                        mapSrtDirRU.keys.forEach { key ->
                            DropdownMenuItem(onClick = {
                                sortDirection.value = key
                                expandedSortDirection = false
                            }) {
                                Text(mapSrtDirRU[key] ?: "")
                            }
                        }
                    }


                }
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(bottom = 8.dp),
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Статус:",
                            fontWeight = FontWeight.Bold
                        )
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
                                androidx.compose.material3.Text(text = "${statusMap[key]}")
                            }
                        }
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {onClick(make.value, model.value, year.value, colorHex.value, pricePerDay.value, numberPlate.value, status.value, sortBy.value, sortDirection.value)}
                )
                {
                    Text(
                        text = "Фильтровать",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
