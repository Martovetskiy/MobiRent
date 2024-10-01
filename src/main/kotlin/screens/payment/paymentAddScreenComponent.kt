package screens.payment

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
import api.payment.PaymentRequest
import api.rental.RentalResponseViewDto
import components.payment.PaymentAddScreenComponent
import icons.DollarSvgrepoCom
import isValidDate
import widgets.PopupNotification
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun paymentAddScreen(component: PaymentAddScreenComponent)
{
    val rentalP: MutableState<RentalResponseViewDto?> = remember{ mutableStateOf(null) }
    var expandedPayments by remember { mutableStateOf(false) }

    val associativeMapRu = mapOf(
        "email" to "Электронная почта пользователя",
        "amount" to "Сумма",
        "step" to "Шаг",
        "paymentDate" to "Дата платежа",
        "paymentMethod" to "Тип оплаты",
        "createAt" to "Дата создания карточки"
    )
    val methodsMap = mapOf(
        "card" to "Карта",
        "cash" to "Наличные",
        "gift_card" to "Подарочная карта"
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
                text = "Добавить платеж",
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
                    imageVector = DollarSvgrepoCom,
                    contentDescription = "Payment Image",
                    tint = Color.Black,
                    modifier = Modifier.size(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.3f)) { // Добавляем отступ для Divider
                Box (modifier = Modifier.fillMaxWidth()
                    .background(color = Color.LightGray, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .clickable { expandedPayments = true }.border(1.dp, Color.LightGray, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                    contentAlignment = Alignment.CenterStart) {
                    androidx.compose.material.Text(
                        text = "Аренда: ${rentalP.value?.rentalId ?: "???"}",
                        modifier = Modifier.padding(16.dp)
                    )
                    DropdownMenu(

                        expanded = expandedPayments,
                        onDismissRequest = { expandedPayments = false }
                    ) {
                        component.rentals.value.sortedBy { it.rentalId }.forEach { rent ->
                            DropdownMenuItem(onClick = {
                                rentalP.value = rent// Reset model when make changes
                                expandedPayments = false
                            }) {
                                Text(text = (rent.rentalId.toString() + ' ' +rent.email))
                            }
                        }
                    }
                }

                Divider(
                    color = if (rentalP.value != null) Color.Green else Color.Red,
                    thickness = 1.dp, // Толщина границы
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter) // Выровнять по центру снизу
                )
            }

            associativeMapRu.keys.toList().slice(1..3).forEach { key ->
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
                        "amount" -> component.amount.value.toDoubleOrNull() != null
                        "step" -> component.step.value.toIntOrNull() != null
                        "paymentDate" -> isValidDate(component.paymentDate.value)
                        else -> false
                    },
                    value = when (key) {
                        "amount" -> component.amount.value
                        "step" -> component.step.value
                        "paymentDate" -> component.paymentDate.value
                        else -> ""

                    },
                    onValueChange = {
                        when (key) {
                            "amount" -> component.amount.value = it
                            "step" -> component.step.value = it
                            "paymentDate" -> component.paymentDate.value = it
                        }

                    },
                    label = {
                        Text(
                            text = associativeMapRu[key] ?: ""
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
                        .padding(4.dp)
                ) {
                    androidx.compose.material.Text(
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
                                selected = component.paymentMethod.value == key,
                                onClick = {
                                    component.paymentMethod.value = key
                                }
                            )
                            Text(text = "${methodsMap[key]}")
                        }
                    }
                }
            }
            Button(
                enabled = (
                        rentalP.value != null &&
                        component.amount.value.toDoubleOrNull() != null &&
                                component.step.value.toIntOrNull() != null &&
                                isValidDate(component.paymentDate.value)),
                modifier = Modifier.fillMaxWidth(0.3f),
                onClick = {
                    component.payment.value = PaymentRequest(
                        rentalId = rentalP.value!!.rentalId,
                        amount = component.amount.value.toDouble(),
                        step = component.step.value.toInt(),
                        paymentDate = OffsetDateTime.parse(component.paymentDate.value + "T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        paymentMethod = component.paymentMethod.value,
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
