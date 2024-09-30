import java.util.regex.Pattern

fun isValidName(name: String): Boolean {
    // Имя: только буквы, длина от 1 до 50 символов
    val nameRegex = "^[A-Za-zА-Яа-яЁё]{1,50}$".toRegex()
    return nameRegex.matches(name)
}

fun isValidSurname(surname: String): Boolean {
    // Фамилия: только буквы, длина от 1 до 50 символов
    val surnameRegex = "^[A-Za-zА-Яа-яЁё]{1,50}$".toRegex()
    return surnameRegex.matches(surname)
}

fun isValidEmail(email: String): Boolean {
    // Электронная почта: базовая проверка на формат
    val emailPattern = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    )
    return emailPattern.matcher(email).matches()
}

fun isValidPhoneNumber(phone: String): Boolean {
    // Номер телефона: формат +79953153801
    val phoneRegex = "^\\+7\\d{10}$".toRegex()
    return phoneRegex.matches(phone)
}

fun isValidDriverLicense(license: String): Boolean {
    // Номер водительского удостоверения: формат XXXX XXXXXX
    val licenseRegex = "^[A-Z0-9]{4} [A-Z0-9]{6}$".toRegex()
    return licenseRegex.matches(license)
}

fun isValidDate(dateStr: String): Boolean {
    val dateRegex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$".toRegex()
    return dateRegex.matches(dateStr)
}

fun isYearValid(year: String): Boolean {
    return year.toIntOrNull()?.let { it >= 1700 } ?: false
}

// Функция для проверки шестизначного HEX-кода цвета
fun isColorHexValid(colorHex: String): Boolean {
    return colorHex.matches(Regex("^#?([A-Fa-f0-9]{6})$"))
}

// Функция для проверки дробного числа
fun isPricePerDayValid(price: String): Boolean {
    return price.toDoubleOrNull() != null
}

// Функция для проверки формата гос номера
fun isNumberPlateValid(plate: String): Boolean {
    return plate.matches(Regex("^[А-Я]{1}\\d{3}[А-Я]{2}\\d{2,3}\$"))
}
