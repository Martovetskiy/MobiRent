package screens.rental

import androidx.compose.foundation.*
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
import api.rental.RentalResponse
import components.rental.RentalsViewScreenComponent
import icons.FilterSvgrepoCom
import widgets.BottomSheet
import widgets.PopupNotification
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun rentalsViewScreen(component: RentalsViewScreenComponent)
{
    val associativeMapRu = mapOf(
        "email" to "Электронная почта пользователя",
        "make" to "Марка",
        "model" to "Модель",
        "startDate" to "Начало аренды",
        "endDate" to "Конец аренды",
        "totalPrice" to "Цена",
        "createAt" to "Дата создания карточки"
    )

    val fMap = mapOf(
        "email" to 1.5f,
        "make" to 1f,
        "model" to 1f,
        "startDate" to 1.5f,
        "endDate" to 1.5f,
        "totalPrice" to 0.7f,
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
                    text = "dbo.Rentals",
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
                            component.goToAdd() },
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
            CustomersTable(
                component.listRentals.value,
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
                        .fillMaxHeight(0.5f)
                        .background(color = Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    associativeMapRu = associativeMapRu,
                    onClick = {email, make, model, startDate, endDate, totalPrice, sortBy, sortDirection ->
                        component.email.value = email.ifEmpty { null }
                        component.make.value = make.ifEmpty { null }
                        component.model.value = model.ifEmpty { null }
                        component.startDate.value = startDate.ifEmpty { null }
                        component.endDate.value = endDate.ifEmpty { null }
                        component.totalPrice.value = totalPrice.ifEmpty { null }
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
private fun CustomersTable(
    customers: List<RentalResponse>,
    associativeMapRu: Map<String, String>,
    fMap: Map<String, Float>,
    onClick: (RentalResponse) -> Unit = {}
)
{
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
        items(customers)
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
                                "email" -> row.customer?.email ?: "Unknown"
                                "make" -> row.car?.make ?: "Unknown"
                                "model" -> row.car?.model ?: "Unknown"
                                "startDate" -> row.startDate?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))) ?: "Unknown"
                                "endDate" -> row.endDate?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))) ?: "Unknown"
                                "totalPrice" -> (row.totalPrice ?: "Unknown").toString()
                                "createAt" -> row.createAt!!.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru")))
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
    onClick: (String, String, String, String, String, String, String, String) -> Unit
){
    val email = remember { mutableStateOf("") }
    val make = remember { mutableStateOf("") }
    val model = remember { mutableStateOf("") }
    val startDate = remember { mutableStateOf("") }
    val endDate = remember { mutableStateOf("") }
    val totalPrice = remember { mutableStateOf("") }
    val sortBy = remember { mutableStateOf("email") }
    val sortDirection = remember { mutableStateOf("ASC") }

    var expandedSortBy by remember { mutableStateOf(false) }
    var expandedSortDirection by remember { mutableStateOf(false) }

    val mapSrtDirRU = mapOf(
        "DESC" to "возрастания",
        "ASC" to "убывания"
    )


    Box (modifier = modifier) {
        Row(modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())) {
            Column {
                associativeMapRu.keys.toList().slice(0..5).forEach { key ->
                    TextField(
                        value = when (key) {
                            "email" -> email.value
                            "make" -> make.value
                            "model" -> model.value
                            "startDate" -> startDate.value
                            "endDate" -> endDate.value
                            "totalPrice" -> totalPrice.value
                            else -> ""

                        },
                        onValueChange = {
                            when (key) {
                                "email" -> email.value = it
                                "make" -> make.value = it
                                "model" -> model.value = it
                                "startDate" -> startDate.value = it
                                "endDate" -> endDate.value = it
                                "totalPrice" -> totalPrice.value = it
                            }

                        },
                        label = {
                            androidx.compose.material3.Text(
                                text = when (key) {
                                    "startDate" -> associativeMapRu[key] ?: ""
                                    "email" -> associativeMapRu[key] ?: ""
                                    "make" -> associativeMapRu[key] ?: ""
                                    "model" -> associativeMapRu[key] ?: ""
                                    "totalPrice" -> associativeMapRu[key] ?: ""
                                    "endDate" -> associativeMapRu[key] ?: ""
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


                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onClick(
                                email.value,
                                make.value,
                                model.value,
                                DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.parse(startDate.value + "T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)),
                                DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.parse(endDate.value + "T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)),
                                totalPrice.value,
                                sortBy.value,
                                sortDirection.value
                            )
                        }
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
}

