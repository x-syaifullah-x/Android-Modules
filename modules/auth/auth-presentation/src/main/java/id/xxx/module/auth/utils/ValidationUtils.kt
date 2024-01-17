package id.xxx.module.auth.utils

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        val pattern = Regex("^[A-Za-z\\d._%+-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,}$")
        return pattern.matches(email)
    }

    fun validateEmail(email: String): String? {
        var result: String? = null
        if (!isValidEmail(email))
            result = "Please enter a valid email address."
        return result
    }

    fun isValidPassword(password: String): String? {
        val minLength = 6
        val isValid = password.length >= minLength
        var result: String? = null
        if (!isValid)
            result = "Password must be at least $minLength characters or more"
        return result
    }

    fun validPhoneNumber(number: String): String? {
        if (number.firstOrNull() != '+')
            return "Please use code area (eg. +62)"
        val pattern = Regex("^\\+?[\\d-]+$")
        // + is optional, followed by digits and/or hyphens
        // $ at the end ensures that there are no extra characters
        return if (!pattern.matches(number)) {
            "Invalid phone number."
        } else {
            null
        }
    }
}