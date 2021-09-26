package com.example.marvel.Utils

import java.math.BigInteger
import java.security.MessageDigest

object Functions {

    fun getHash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun getTimeStamp() = (System.currentTimeMillis() / 1000).toString()
}