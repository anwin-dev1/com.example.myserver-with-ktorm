package com.example.utils

object Constant {
    const val EMAIL_REGEX_PATTERN = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
    const val PASSWORD_REGEX_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[_,@,#,!])(?=\\S+\$).{8,}\$"
}