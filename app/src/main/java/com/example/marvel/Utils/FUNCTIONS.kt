package com.example.marvel.Utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.marvel.Models.ComicsResponse
import java.math.BigInteger
import java.security.MessageDigest
import java.time.DayOfWeek
import java.time.LocalDate

object Functions {

    fun getHash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun getTimeStamp() = (System.currentTimeMillis() / 1000).toString()

    fun ComicsResponse.getDate(): String {
        for (i in this.dates)
            if (i.type=="onsaleDate")
                return i.date
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isInThisWeek(str: String): Boolean {

        val today: LocalDate = LocalDate.now()

        var sunday: LocalDate = today
        while (sunday.dayOfWeek != DayOfWeek.SUNDAY) {
            sunday = sunday.minusDays(1)
        }

        var saturday: LocalDate = today
        while (saturday.dayOfWeek != DayOfWeek.SATURDAY) {
            saturday = saturday.plusDays(1)
        }

        return str>=sunday.toString() && str<=saturday.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isInLastWeek(str: String): Boolean {

        val today: LocalDate = LocalDate.now()

        var sunday: LocalDate = today.minusDays(7)
        while (sunday.dayOfWeek !== DayOfWeek.SUNDAY) {
            sunday = sunday.minusDays(1)
        }

        var saturday: LocalDate = today
        while (saturday.dayOfWeek !== DayOfWeek.SATURDAY) {
            saturday = saturday.plusDays(1)
        }

        return str>=sunday.toString() && str<=saturday.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isInNextWeek(str: String): Boolean {

        val today: LocalDate = LocalDate.now()

        var sunday: LocalDate = today
        while (sunday.dayOfWeek !== DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1)
        }

        var saturday: LocalDate = sunday
        while (saturday.dayOfWeek !== DayOfWeek.SATURDAY) {
            saturday = saturday.plusDays(1)
        }

        return str>=sunday.toString() && str<=saturday.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isInThisMonth(str: String): Boolean {
        val today = LocalDate.now().toString()
        return str[5]==today[5] && str[6]==today[6]
    }
}