package aust.fyp.learn.and.earn.StoreRoom

object URLs {

    val IP_ADDRESS = "192.168.43.169"
    val PORT = ":3011"
    val PROTOCOL = "http://"

    val CREATE_ACCOUNT = PROTOCOL + IP_ADDRESS + PORT + "/users/create"
    val LOGIN = PROTOCOL + IP_ADDRESS + PORT + "/users/login"
    val VERIFY_CODE = PROTOCOL + IP_ADDRESS + PORT + "/users/verify"
    val RESEND_EMAIL = PROTOCOL + IP_ADDRESS + PORT + "/users/resend_email"
    val FORGOT_PASSWORD = PROTOCOL + IP_ADDRESS + PORT + "/users/forgot_password"
    val EDIT_PROFILE = PROTOCOL + IP_ADDRESS + PORT + "/users/edit_profile"

}