package com.example.hundredplaces.util

import java.security.MessageDigest

fun hashPassword(message: ByteArray): ByteArray {
    val md = MessageDigest.getInstance("SHA-256")
    val digest: ByteArray = md.digest(message)
    return digest
}