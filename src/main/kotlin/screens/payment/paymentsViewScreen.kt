package screens.payment

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
import api.payment.PaymentResponseViewDto
import components.payment.PaymentsViewScreenComponent
import icons.FilterSvgrepoCom
import widgets.BottomSheet
import widgets.PopupNotification
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun paymentsViewScreen(component: PaymentsViewScreenComponent){
    val associativeMapRu = mapOf(
        "email" to "Электронная почта пользователя",
        "amount" to "Сумма",
        "step" to "Шаг",
        "paymentDate" to "Дата платежа",
        "paymentMethod" to "Тип оплаты",
        "createAt" to "Дата создания карточки"
    )

    val fMap = mapOf(
        "email" to 1.5f,
        "amount" to 1f,
        "step" to 0.5f,
        "paymentDate" to 1.5f,
        "paymentMethod" to 1.5f,
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
                    text = "dbo.Payments",
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
            PaymentsTable(
                component.listPayments.value,
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

        if (component.listPayments.value.isEmpty()) {
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
                        .fillMaxHeight(0.5f)
                        .background(color = Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    associativeMapRu = associativeMapRu,
                    onClick = {email, amount, step, paymentDate, paymentMethod, sortBy, sortDirection ->
                        component.email.value = email.ifEmpty { null }
                        component.amount.value = amount.ifEmpty { null }
                        component.step.value = step.ifEmpty { null }
                        component.paymentDate.value = paymentDate.ifEmpty { null }
                        component.paymentMethod.value = paymentMethod.ifEmpty { null }

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
private fun PaymentsTable(
    payments: List<PaymentResponseViewDto>,
    associativeMapRu: Map<String, String>,
    fMap: Map<String, Float>,
    onClick: (Long) -> Unit = {}
)
{
    val methodsMap = mapOf(
        "card" to "Карта",
        "cash" to "Наличные",
        "gift_card" to "Подарочная карта"
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
        items(payments)
        {row ->
            Row (
                modifier = Modifier.clickable {
                    onClick(row.paymentId)
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
                                "email" -> row.email
                                "amount" -> row.amount.toString()
                                "step" -> row.step.toString()
                                "paymentDate" -> row.paymentDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru")))
                                "paymentMethod" -> methodsMap[row.paymentMethod] ?: "???"
                                "createAt" -> row.createAt.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru")))
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
    onClick: (String, String, String, String, String, String, String) -> Unit
){
    val email = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val step = remember { mutableStateOf("") }
    val paymentDate = remember { mutableStateOf("") }
    val paymentMethod = remember { mutableStateOf("") }
    val sortBy = remember { mutableStateOf("email") }
    val sortDirection = remember { mutableStateOf("ASC") }

    var expandedSortBy by remember { mutableStateOf(false) }
    var expandedSortDirection by remember { mutableStateOf(false) }

    val mapSrtDirRU = mapOf(
        "DESC" to "возрастания",
        "ASC" to "убывания"
    )

    val methodsMap = mapOf(
        "" to "Не выбрано",
        "card" to "Карта",
        "cash" to "Наличные",
        "gift_card" to "Подарочная карта"
    )


    Box (modifier = modifier) {
        Row(modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())) {
            Column {
                associativeMapRu.keys.toList().slice(0..3).forEach { key ->
                    TextField(
                        value = when (key) {
                            "email" -> email.value
                            "amount" -> amount.value
                            "step" -> step.value
                            "paymentDate" -> paymentDate.value
                            else -> ""

                        },
                        onValueChange = {
                            when (key) {
                                "email" -> email.value = it
                                "amount" -> amount.value = it
                                "step" -> step.value = it
                                "paymentDate" -> paymentDate.value = it
                            }

                        },
                        label = {
                            androidx.compose.material3.Text(
                                text = when (key) {
                                    "email" -> associativeMapRu[key] ?: ""
                                    "amount" -> associativeMapRu[key] ?: ""
                                    "step" -> associativeMapRu[key] ?: ""
                                    "paymentDate" -> associativeMapRu[key] ?: ""
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
                                text = "Метод платежа:",
                                fontWeight = FontWeight.Bold
                            )
                            methodsMap.keys.forEach { key ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RadioButton(
                                        selected = paymentMethod.value == key,
                                        onClick = {
                                            paymentMethod.value = key
                                        }
                                    )
                                    androidx.compose.material3.Text(text = "${methodsMap[key]}")
                                }
                            }
                        }
                    }


                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onClick(
                                email.value,
                                amount.value,
                                step.value,
                                if (paymentDate.value.isNotEmpty()) DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
                                    OffsetDateTime.parse(paymentDate.value + "T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)) else "",
                                paymentMethod.value,
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