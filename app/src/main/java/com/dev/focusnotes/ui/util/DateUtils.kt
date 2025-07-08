package com.dev.focusnotes.ui.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    private val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy • HH:mm")

    fun formatDateTime(dateTime: LocalDateTime): String {
        return dateTime.format(formatter)
    }
}
