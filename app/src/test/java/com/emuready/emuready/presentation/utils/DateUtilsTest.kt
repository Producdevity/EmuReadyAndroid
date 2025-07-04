package com.emuready.emuready.presentation.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class DateUtilsTest {

    @Test
    fun `formatDateTime should format timestamp correctly`() {
        // Given
        val calendar = Calendar.getInstance().apply {
            set(2024, Calendar.JANUARY, 15, 14, 30, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val timestamp = calendar.timeInMillis

        // When
        val result = DateUtils.formatDateTime(timestamp)

        // Then
        assertEquals("Jan 15, 2024 14:30", result)
    }

    @Test
    fun `formatDate should format timestamp correctly`() {
        // Given
        val calendar = Calendar.getInstance().apply {
            set(2024, Calendar.DECEMBER, 25, 10, 15, 30)
            set(Calendar.MILLISECOND, 0)
        }
        val timestamp = calendar.timeInMillis

        // When
        val result = DateUtils.formatDate(timestamp)

        // Then
        assertEquals("Dec 25, 2024", result)
    }

    @Test
    fun `formatTime should format timestamp correctly`() {
        // Given
        val calendar = Calendar.getInstance().apply {
            set(2024, Calendar.JUNE, 10, 9, 45, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val timestamp = calendar.timeInMillis

        // When
        val result = DateUtils.formatTime(timestamp)

        // Then
        assertEquals("09:45", result)
    }

    @Test
    fun `formatRelativeTime should return 'Just now' for recent timestamps`() {
        // Given
        val now = System.currentTimeMillis()
        val recentTimestamp = now - 30_000 // 30 seconds ago

        // When
        val result = DateUtils.formatRelativeTime(recentTimestamp)

        // Then
        assertEquals("Just now", result)
    }

    @Test
    fun `formatRelativeTime should return minutes for timestamps within an hour`() {
        // Given
        val now = System.currentTimeMillis()
        val minutesAgo = now - 15 * 60_000 // 15 minutes ago

        // When
        val result = DateUtils.formatRelativeTime(minutesAgo)

        // Then
        assertEquals("15 minutes ago", result)
    }

    @Test
    fun `formatRelativeTime should return hours for timestamps within a day`() {
        // Given
        val now = System.currentTimeMillis()
        val hoursAgo = now - 3 * 3_600_000 // 3 hours ago

        // When
        val result = DateUtils.formatRelativeTime(hoursAgo)

        // Then
        assertEquals("3 hours ago", result)
    }

    @Test
    fun `formatRelativeTime should return days for timestamps within a week`() {
        // Given
        val now = System.currentTimeMillis()
        val daysAgo = now - 2 * 86_400_000 // 2 days ago

        // When
        val result = DateUtils.formatRelativeTime(daysAgo)

        // Then
        assertEquals("2 days ago", result)
    }

    @Test
    fun `formatRelativeTime should return formatted date for old timestamps`() {
        // Given
        val now = System.currentTimeMillis()
        val oldTimestamp = now - 10 * 86_400_000 // 10 days ago

        // When
        val result = DateUtils.formatRelativeTime(oldTimestamp)

        // Then
        // Should return a formatted date, not relative time
        assert(result.contains("202")) // Should contain year
    }
}