package screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.sharp.Person
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
import api.customer.CustomerResponse
import components.customer.CustomerCardScreenComponent
import isValidDriverLicense
import isValidEmail
import isValidName
import isValidPhoneNumber
import isValidSurname
import widgets.PopupNotification
import java.time.OffsetDateTime

@Composable
fun customerCardScreen(component: CustomerCardScreenComponent)
{
    val firstName = remember { mutableStateOf(component.customer.value?.firstName ?: "") }
    val lastName = remember { mutableStateOf(component.customer.value?.lastName ?: "")}
    val email = remember { mutableStateOf(component.customer.value?.email?: "")}
    val phoneNumber = remember { mutableStateOf(component.customer.value?.phoneNumber?: "")}
    val driverLicense = remember { mutableStateOf(component.customer.value?.driverLicense?: "")}
    val isBanned = remember { mutableStateOf(component.customer.value?.isBanned?: false) }
    val associativeMapRu = mapOf(
        "firstName" to "Имя",
        "lastName" to "Фамилия",
        "email" to "Эл. почта",
        "phoneNumber" to "Телефон",
        "driverLicense" to "Номер в. д.",
        "isBanned" to "Блокирован?",
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
                text = "Карточка пользователя ${component.customer.value?.email ?: "Email"}",
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
                    imageVector = Icons.Sharp.Person,
                    contentDescription = "Profile Image",
                    tint = Color.Black,
                    modifier = Modifier.size(150.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
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

            associativeMapRu.keys.toList().slice(0..4).forEach { key ->
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
                    isError = !(when (key) {
                        "firstName" -> isValidName(firstName.value)
                        "lastName" -> isValidSurname(lastName.value)
                        "email" -> isValidEmail(email.value)
                        "phoneNumber" -> isValidPhoneNumber(phoneNumber.value)
                        "driverLicense" -> isValidDriverLicense(driverLicense.value)
                        else -> false
                    }),
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
                        androidx.compose.material.Text(
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

                    )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.3f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    enabled = isEdit.value,
                    checked = isBanned.value,
                    onCheckedChange = { isBanned.value = it }
                )
                Text(
                    text = if (isBanned.value) "Забанен" else "Активен",
                    modifier = Modifier.padding(start = 8.dp),
                    style = TextStyle(
                        textDirection = TextDirection.Content,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
            }

            if (isEdit.value) {
                Column(Modifier.fillMaxWidth(0.3f)) {
                    Button(
                        enabled = (isValidName(firstName.value)
                                && isValidSurname(lastName.value)
                                && isValidEmail(email.value)
                                && isValidPhoneNumber(phoneNumber.value)
                                && isValidDriverLicense(driverLicense.value)),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            component.customerTemp.value = CustomerResponse(
                                customerId = component.customer.value?.customerId ?: 1,
                                firstName = firstName.value,
                                lastName = lastName.value,
                                email = email.value,
                                phoneNumber = phoneNumber.value,
                                driverLicense = driverLicense.value,
                                isBanned = isBanned.value,
                                createAt = component.customer.value?.createAt ?: OffsetDateTime.now()
                            )
                            component.putData(component.customerTemp.value!!)
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