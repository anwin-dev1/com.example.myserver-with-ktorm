package com.example.model

import com.example.utils.Constant
import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class LoginDetailsModel(val id: Int? = 0,val email:String, val password: String) {

    fun hashPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun isValie(): Boolean {
        return Regex(Constant.PASSWORD_REGEX_PATTERN).matches(password)
    }

    fun isEmailPatternMatch():Boolean{
        return Regex(Constant.EMAIL_REGEX_PATTERN).matches(email)
    }
}