package screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import api.payment.PaymentRequest
import components.payment.PaymentCardScreenComponent
import icons.DollarSvgrepoCom
import isValidDate
import widgets.PopupNotification
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun paymentCardScreen(component: PaymentCardScreenComponent)
{

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
                text = "Карточка платежа для аренды ${component.payment.value?.rentalId ?: "Rental"}",
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
                    contentDescription = "Rental Image",
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

            associativeMapRu.keys.toList().slice(1..2).forEach { key ->
                TextField(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(0.3f),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.LightGray,
                        errorIndicatorColor = Color.Red,
                        errorCursorColor = Color.Red
                    ),
                    enabled = isEdit.value,
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
                                enabled = isEdit.value,
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


            if (isEdit.value) {
                Column(Modifier.fillMaxWidth(0.3f)) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colorScheme.primary
                        ),
                        enabled = (
                                component.amount.value.toDoubleOrNull() != null &&
                                component.step.value.toIntOrNull() != null &&
                                isValidDate(component.paymentDate.value)),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            println()
                            val res = PaymentRequest(
                                rentalId = component.payment.value!!.rentalId,
                                amount = component.amount.value.toDouble(),
                                step = component.step.value.toInt(),
                                paymentDate = OffsetDateTime.parse(component.paymentDate.value + "T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                                paymentMethod = component.paymentMethod.value,
                                createAt = component.payment.value!!.createAt
                            )
                            component.putData(res)
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