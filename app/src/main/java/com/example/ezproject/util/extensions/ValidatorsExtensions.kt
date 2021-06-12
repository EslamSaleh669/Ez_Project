package com.example.ezproject.util.extensions
//
//import android.widget.EditText
//import cloud.touch.ecommerce.R
//import cloud.touch.ecommerce.util.Constants
//
//fun EditText.validateEmail(): Boolean {
//    val emailStr = this.text.trim()
//    if (emailStr.length < Constants.EMAIL_MIN_LENGTH) {
//        this.error = "Email length should be more than ${Constants.EMAIL_MIN_LENGTH} characters"
//        return false
//    }
//    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
//        this.error = "Type a valid email address"
//        return false
//    }
//    return true
//}
//
//fun EditText.validatePassword(): Boolean {
//    val passwordStr = this.text.trim()
//    if (passwordStr.length < Constants.PASSWORD_MIN_LENGTH) {
//        this.setError(
//            "Password length should be more than ${Constants.PASSWORD_MIN_LENGTH} characters",
//            context?.getDrawable(
//                R.drawable.transparent_icon
//            )
//        )
//        return false
//    }
//    return true
//}
//
//fun EditText.validateUsername(): Boolean {
//    val usernameStr = this.text.trim()
//    if (usernameStr.length < Constants.USERNAME_MIN_LENGTH) {
//        this.error = "username length must be more than ${Constants.USERNAME_MIN_LENGTH}"
//        return false
//    }
//    return true
//}
//
//
//fun EditText.validatePhone(): Boolean {
//    val phoneStr = this.text.trim()
//    if (phoneStr.length < Constants.PHONE_MIN_LENGTH || phoneStr.length > Constants.PHONE_MAX_LENGTH ) {
//        this.error = "username length must be more than ${Constants.USERNAME_MIN_LENGTH}"
//        return false
//    }
//    return true
//}
//
