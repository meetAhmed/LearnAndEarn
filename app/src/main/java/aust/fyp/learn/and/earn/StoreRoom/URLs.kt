package aust.fyp.learn.and.earn.StoreRoom

object URLs {

    val IP_ADDRESS = "192.168.43.213"
    val PORT = ":3011"
    val PROTOCOL = "http://"

    // users
    val CREATE_ACCOUNT = PROTOCOL + IP_ADDRESS + PORT + "/users/create"
    val LOGIN = PROTOCOL + IP_ADDRESS + PORT + "/users/login"
    val VERIFY_CODE = PROTOCOL + IP_ADDRESS + PORT + "/users/verify"
    val RESEND_EMAIL = PROTOCOL + IP_ADDRESS + PORT + "/users/resend_email"
    val FORGOT_PASSWORD = PROTOCOL + IP_ADDRESS + PORT + "/users/forgot_password"
    val EDIT_PROFILE = PROTOCOL + IP_ADDRESS + PORT + "/users/edit_profile"

    // upload
    val UPLOAD = PROTOCOL + IP_ADDRESS + PORT + "/upload"

    // education/history
    val ADD_EDUCATION_HISTORY = PROTOCOL + IP_ADDRESS + PORT + "/education/history/add"

    fun getImageUrl(image_name: String?): String {
        return PROTOCOL + IP_ADDRESS + PORT + "/uploadedFiles/$image_name"
    }

}