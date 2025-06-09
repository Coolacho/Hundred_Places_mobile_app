package com.example.hundredplaces.util

import android.util.Patterns
import java.util.regex.Pattern

fun validateEmail(email: CharSequence): Boolean {
    return if (email.isNotEmpty()) {
        !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    else {
        false
    }
}

fun validatePassword(password: CharSequence): Boolean {
    return if (password.isNotEmpty()) {
        !Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,50}$")
            .matcher(password)
            .matches()
    }
    else {
        false
    }
}

fun validateName(name: CharSequence): Boolean {
    return if (name.isNotEmpty()) {
        !Pattern.compile("^([A-ZÀ-ÖØ-ÞŸŽŒŠ][a-zß-öø-ÿƒšœž]*[-,.']? ?){1,4}$")
            .matcher(name)
            .matches()
    }
    else {
        false
    }
}