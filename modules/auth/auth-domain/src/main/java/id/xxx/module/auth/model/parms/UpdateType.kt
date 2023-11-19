package id.xxx.module.auth.model.parms

sealed interface UpdateType {

    data class ConfirmEmailVerification(
        val oobCode: String
    ) : UpdateType

    data class ChangeEmail(
        val idToken: String,
        val email: String,
        val returnSecureToken: Boolean = true
    ) : UpdateType

    data class ChangePassword(
        val idToken: String,
        val password: String,
        val returnSecureToken: Boolean = true
    ) : UpdateType

    data class Profile(
        val idToken: String,
        val displayName: String,
        val photoUrl: String,
        /*
        * List of attributes to delete, "DISPLAY_NAME" or "PHOTO_URL". This will nullify these values
        */
        val deleteAttribute: List<String>,
        val returnSecureToken: Boolean = true
    ) : UpdateType
}