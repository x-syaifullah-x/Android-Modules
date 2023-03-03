package id.xxx.module.auth.utils

object ValidationUtils {

    fun isValidEmail(email: String): String? {
        val pattern = Regex("^[A-Za-z\\d._%+-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,}$")
        val isValid = pattern.matches(email)
        var result: String? = null
        if (!isValid)
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

    fun isValidPhoneNumber(number: String): Boolean {
        val pattern = Regex("^\\+?[\\d-]+$")
        // + is optional, followed by digits and/or hyphens
        // $ at the end ensures that there are no extra characters
        return pattern.matches(number)
    }
}