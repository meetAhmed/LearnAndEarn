package aust.fyp.learn.and.earn.StoreRoom

object URLs {

    val IP_ADDRESS = "192.168.43.213"
    val PORT = ":3010"
    val PROTOCOL = "http://"

    val CREATE_ACCOUNT = PROTOCOL + IP_ADDRESS + PORT + "/users/create"
    val LOGIN = PROTOCOL + IP_ADDRESS + PORT + "/users/login"
    val VERIFY_CODE = PROTOCOL + IP_ADDRESS + PORT + "/users/verify"

}