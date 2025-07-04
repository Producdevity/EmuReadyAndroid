package com.emuready.emuready.presentation.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    
    private val dateTimeFormatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    private val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    fun formatDateTime(timestamp: Long): String {
        return dateTimeFormatter.format(Date(timestamp))
    }
    
    fun formatDate(timestamp: Long): String {
        return dateFormatter.format(Date(timestamp))
    }
    
    fun formatTime(timestamp: Long): String {
        return timeFormatter.format(Date(timestamp))
    }
    
    fun formatRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        return when {
            diff < 60_000 -> "Just now"
            diff < 3_600_000 -> "${diff / 60_000} minutes ago"
            diff < 86_400_000 -> "${diff / 3_600_000} hours ago"
            diff < 604_800_000 -> "${diff / 86_400_000} days ago"
            else -> formatDate(timestamp)
        }
    }
}