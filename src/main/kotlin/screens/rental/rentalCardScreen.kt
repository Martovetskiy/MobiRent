package screens.rental

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
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
import api.customer.CustomerResponse
import api.rental.RentalResponse
import components.rental.RentalCardScreenComponent
import icons.MoneyBankCheckPaymentChequeFinanceBusinessSvgrepoCom
import isValidDate
import widgets.PopupNotification
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun rentalCardScreen(component: RentalCardScreenComponent)
{
    val customerP: MutableState<CustomerResponse?> = remember { mutableStateOf(component.rental.value?.customer) }
    val carP: MutableState<CarResponse?> = remember { mutableStateOf(component.rental.value?.car) }
    val startDate = remember { mutableStateOf( component.rental.value?.startDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) }
    val endDate = remember { mutableStateOf(component.rental.value?.endDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) }

    var expandedCustomers by remember { mutableStateOf(false) }
    var expandedCars by remember { mutableStateOf(false) }

    val associativeMapRu = mapOf(
        "email" to "Электронная почта пользователя",
        "make" to "Марка",
        "model" to "Модель",
        "startDate" to "Начало аренды",
        "endDate" to "Конец аренды",
        "totalPrice" to "Цена",
        "createAt" to "Дата создания карточки"
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
                text = "Карточка аренды ${component.rental.value?.rentalId ?: "Email"}",
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
                    imageVector = MoneyBankCheckPaymentChequeFinanceBusinessSvgrepoCom,
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

            Box(modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.3f)) { // Добавляем отступ для Divider
                Box (modifier = Modifier.fillMaxWidth()
                    .background(color = Color.LightGray, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .clickable {
                        if (isEdit.value) expandedCustomers = true
                    }.border(1.dp, Color.LightGray, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                    contentAlignment = Alignment.CenterStart) {
                    androidx.compose.material.Text(
                        text = "Юзер: ${if (customerP.value != null) customerP.value?.firstName + ' ' + customerP.value?.email else "Не выбран" }",
                        modifier = Modifier.padding(16.dp)
                    )
                    DropdownMenu(

                        expanded = expandedCustomers,
                        onDismissRequest = { expandedCustomers = false }
                    ) {
                        component.customers.value.sortedBy { it.email }.forEach { cust ->
                            DropdownMenuItem(onClick = {
                                customerP.value = cust// Reset model when make changes
                                expandedCustomers = false
                            }) {
                                Text(text = cust.email?: "Unknown")
                            }
                        }
                    }
                }

                Divider(
                    color = if (customerP.value != null) Color.Green else Color.Red,
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
                        if(isEdit.value) expandedCars = true
                    }.border(1.dp, Color.LightGray, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                    contentAlignment = Alignment.CenterStart) {
                    androidx.compose.material.Text(
                        text = "Машина: ${if (carP.value != null) '#' + carP.value?.carId.toString() + ' ' + carP.value?.make + ' ' + carP.value?.model else "Не выбран" }",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                DropdownMenu(
                    expanded = expandedCars,
                    onDismissRequest = { expandedCars = false }
                ) {
                    (component.cars.value)
                    component.cars.value.sortedBy { it.make }.forEach { car ->
                        DropdownMenuItem(onClick = {
                            carP.value = car
                            expandedCars = false
                        }) {
                            Text(text = '#' + car.carId.toString() + ' ' + car.make.toString() + ' ' + car.model.toString() )
                        }
                    }
                }

                Divider(
                    color = if (carP.value != null) Color.Green else Color.Red,
                    thickness = 1.dp, // Толщина границы
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter) // Выровнять по центру снизу
                )
            }

            associativeMapRu.keys.toList().slice(3..4).forEach { key ->
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
                        "startDate" -> isValidDate(startDate.value?:"")
                        "endDate" -> isValidDate(endDate.value?:"")
                        else -> false
                    },
                    value = when (key) {
                        "startDate" -> startDate.value?: ""
                        "endDate" -> endDate.value?: ""
                        else -> ""

                    },
                    onValueChange = {
                        when (key) {
                            "startDate" -> startDate.value = it
                            "endDate" -> endDate.value = it
                        }

                    },
                    label = {
                        Text(
                            text = when (key) {
                                "startDate" -> associativeMapRu[key] ?: ""
                                "endDate" -> associativeMapRu[key] ?: ""
                                else -> ""
                            }
                        )
                    },

                    )

            }


            if (isEdit.value) {
                Column(Modifier.fillMaxWidth(0.3f)) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colorScheme.primary
                        ),
                        enabled = (
                                isValidDate(startDate.value?:"") &&
                                        isValidDate(endDate.value?:"")
                                ),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            component.rentalTemp.value = RentalResponse(
                                rentalId = component.rental.value!!.rentalId,
                                customerId = customerP.value!!.customerId,
                                carId = carP.value!!.carId,
                                startDate = OffsetDateTime.parse(startDate.value + "T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                                endDate = OffsetDateTime.parse(endDate.value+ "T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                                totalPrice = null,
                                createAt = component.rental.value!!.createAt
                            )
                            component.putData(component.rentalTemp.value!!)
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