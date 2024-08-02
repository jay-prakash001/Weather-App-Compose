package com.example.weatherappcompose.common

fun formatDate(inputDate: String): String {
    val (year, month, day) = inputDate.split("-")
    return "$day:$month:$year"
}