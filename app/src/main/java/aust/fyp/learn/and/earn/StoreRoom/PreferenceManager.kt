package aust.fyp.learn.and.earn.StoreRoom

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager private constructor() {

    companion object {
        private var instance_of_pref_manager: PreferenceManager? = null
        private lateinit var pref: SharedPreferences
        private lateinit var prefEditor: SharedPreferences.Editor
        fun getInstance(context: Context): PreferenceManager? {
            if (instance_of_pref_manager == null) {
                instance_of_pref_manager = PreferenceManager()
                pref = context.getSharedPreferences(
                    "aust.fyp.learn.and.earn",
                    Context.MODE_PRIVATE
                )
                prefEditor = pref.edit()
            }
            return instance_of_pref_manager
        }
    }

    fun setActiveUser() {
        prefEditor.putBoolean(Constants.USER_ACTIVE, true)
        prefEditor.commit()
    }

    fun removeActiveUser() {
        prefEditor.putBoolean(Constants.USER_ACTIVE, false)
        prefEditor.putInt(Constants.USER_ID, -99)
        prefEditor.putString(Constants.USER_NAME, Constants.UNDEFINED)
        prefEditor.putString(Constants.USER_EMAIL, Constants.UNDEFINED)
        prefEditor.putString(Constants.USER_ADDRESS, Constants.UNDEFINED)
        prefEditor.putString(Constants.USER_PROFILE, Constants.UNDEFINED)
        prefEditor.putString(Constants.USER_PHONE_NUMBER, Constants.UNDEFINED)
        prefEditor.putString(Constants.USER_PASSWORD, Constants.UNDEFINED)
        prefEditor.putString(Constants.USER_ACCOUNT_TYPE, Constants.UNDEFINED)
        prefEditor.putString(Constants.USER_STAUS, Constants.UNDEFINED)

        prefEditor.commit()
    }


    fun isUserActive(): Boolean {
        return pref.getBoolean(Constants.USER_ACTIVE, false)
    }

    fun setUserId(id: Int) {
        prefEditor.putInt(Constants.USER_ID, id)
        prefEditor.commit()
    }

    fun getUserId(): Int {
        return pref.getInt(Constants.USER_ID, -99)
    }

    fun setUserName(name: String) {
        prefEditor.putString(Constants.USER_NAME, name)
        prefEditor.commit()
    }

    fun getUserName(): String? {
        return pref.getString(Constants.USER_NAME, Constants.UNDEFINED)
    }

    fun setUserAddress(address: String) {
        prefEditor.putString(Constants.USER_ADDRESS, address)
        prefEditor.commit()
    }

    fun getUserAddress(): String? {
        return pref.getString(Constants.USER_ADDRESS, Constants.UNDEFINED)
    }

    fun setUserPhone(phone: String) {
        prefEditor.putString(Constants.USER_PHONE_NUMBER, phone)
        prefEditor.commit()
    }

    fun getUserPhone(): String? {
        return pref.getString(Constants.USER_PHONE_NUMBER, Constants.UNDEFINED)
    }

    fun setUserEmail(email: String) {
        prefEditor.putString(Constants.USER_EMAIL, email)
        prefEditor.commit()
    }

    fun getUserEmail(): String? {
        return pref.getString(Constants.USER_EMAIL, Constants.UNDEFINED)
    }

    fun setUserPassword(password: String) {
        prefEditor.putString(Constants.USER_PASSWORD, password)
        prefEditor.commit()
    }

    fun getUserPassword(): String? {
        return pref.getString(Constants.USER_PASSWORD, Constants.UNDEFINED)
    }

    fun setUserProfile(profile: String) {
        prefEditor.putString(Constants.USER_PROFILE, profile)
        prefEditor.commit()
    }

    fun getUserProfile(): String? {
        return pref.getString(Constants.USER_PROFILE, Constants.UNDEFINED)
    }

    fun setUserAccountType(type: String) {
        prefEditor.putString(Constants.USER_ACCOUNT_TYPE, type)
        prefEditor.commit()
    }

    fun getUserAccountType(): String? {
        return pref.getString(Constants.USER_ACCOUNT_TYPE, Constants.UNDEFINED)
    }

    fun setAccountStatus(status: String) {
        prefEditor.putString(Constants.USER_STAUS, status)
        prefEditor.commit()
    }

    fun getAccountStatus(): String? {
        return pref.getString(Constants.USER_STAUS, Constants.UNDEFINED)
    }

}