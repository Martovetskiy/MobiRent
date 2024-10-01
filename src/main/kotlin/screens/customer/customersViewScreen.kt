package screens.customer

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
import api.customer.CustomerResponse
import components.customer.CustomersViewScreenComponent
import icons.FilterSvgrepoCom
import widgets.BottomSheet
import widgets.PopupNotification
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun customersViewScreen(component: CustomersViewScreenComponent)
{
    val associativeMapRu = mapOf(
        "firstName" to "Имя",
        "lastName" to "Фамилия",
        "email" to "Эл. почта",
        "phoneNumber" to "Телефон",
        "driverLicense" to "Номер в. д.",
        "isBanned" to "Блокирован?",
        "createAt" to "Дата создания карточки"
    )

    val fMap = mapOf(
        "firstName" to 1f,
        "lastName" to 1f,
        "email" to 1.5f,
        "phoneNumber" to 1.5f,
        "driverLicense" to 1.5f,
        "isBanned" to 0.7f,
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
                    text = "dbo.Customers",
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
                        modifier = Modifier.clickable { component.goToAdd() },
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
                component.listCustomers.value,
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

        if (component.listCustomers.value.isEmpty()) {
            Text(
                modifier = Modifier.align(alignment = Alignment.Center),
                text = "Нет данных",
                textAlign = TextAlign.Center,
                color = Color.White,
                style = TextStyle(fontSize = 24.sp)
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
                        .fillMaxHeight(0.4f)
                        .background(color = Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    associativeMapRu = associativeMapRu,
                    onClick = {firstName, lastName, email, phoneNumber, driverLicense, isBanned, sortBy, sortDirection ->
                        component.firstName.value = firstName.ifEmpty { null }
                        component.lastName.value = lastName.ifEmpty { null }
                        component.email.value = email.ifEmpty { null }
                        component.phoneNumber.value = phoneNumber.ifEmpty { null }
                        component.driverLicense.value = driverLicense.ifEmpty { null }
                        component.isBanned.value = if (isBanned.isEmpty()) null else isBanned.toBoolean()
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
    customers: List<CustomerResponse>,
    associativeMapRu: Map<String, String>,
    fMap: Map<String, Float>,
    onClick: (CustomerResponse) -> Unit = {}
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
                            .background(color = Color.Gray.copy(0.3f))
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
                                "firstName" -> row.firstName!!
                                "lastName" -> row.lastName!!
                                "email" -> row.email!!
                                "phoneNumber" -> row.phoneNumber!!
                                "driverLicense" -> row.driverLicense!!
                                "isBanned" -> if (row.isBanned!!) "ДА" else "Нет"
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
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val driverLicense = remember { mutableStateOf("") }
    val isBanned = remember { mutableStateOf("") }
    val sortBy = remember { mutableStateOf("firstName") }
    val sortDirection = remember { mutableStateOf("ASC")}

    var expandedSortBy by remember { mutableStateOf(false) }
    var expandedSortDirection by remember { mutableStateOf(false) }
    var expandedIsBanned by remember { mutableStateOf(false) }

    val mapSrtDirRU = mapOf(
        "DESC" to "возрастания",
        "ASC" to "убывания"
    )

    val mapIsBoolRU = mapOf(
        "" to "Не выбрано",
        "False" to "Нет",
        "True" to "Да"
    )

    Box (modifier = modifier) {
        Row (modifier = Modifier.padding(8.dp)){
            Column {
                associativeMapRu.keys.toList().slice(0..4).forEach { key ->
                    TextField(
                        value = when (key) {
                            "firstName" -> firstName.value
                            "lastName" -> lastName.value
                            "email" -> email.value
                            "phoneNumber" -> phoneNumber.value
                            "driverLicense" -> driverLicense.value
                            else -> ""

                        },
                        onValueChange = {
                            when (key) {
                                "firstName" -> firstName.value = it
                                "lastName" -> lastName.value = it
                                "email" -> email.value = it
                                "phoneNumber" -> phoneNumber.value = it
                                "driverLicense" -> driverLicense.value = it
                            }

                        },
                        label = {
                            Text(
                                text = when (key) {
                                    "firstName" -> associativeMapRu[key] ?: ""
                                    "lastName" -> associativeMapRu[key] ?: ""
                                    "email" -> associativeMapRu[key] ?: ""
                                    "phoneNumber" -> associativeMapRu[key] ?: ""
                                    "driverLicense" -> associativeMapRu[key] ?: ""
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

                    Column(
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(4.dp)
                        ).fillMaxWidth().clickable { expandedIsBanned = true }
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Заблокирован?:",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = mapIsBoolRU[isBanned.value] ?: "",
                            modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownMenu(
                        expanded = expandedIsBanned,
                        onDismissRequest = { expandedIsBanned = false },
                        modifier = Modifier.border(BorderStroke(1.dp, Color.Gray))
                    ) {
                        mapIsBoolRU.keys.forEach { key ->
                            DropdownMenuItem(onClick = {
                                isBanned.value = key
                                expandedIsBanned = false
                            }) {
                                Text(mapIsBoolRU[key] ?: "")
                            }
                        }
                    }
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {onClick(firstName.value, lastName.value, email.value, phoneNumber.value, driverLicense.value, isBanned.value, sortBy.value, sortDirection.value)}
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

