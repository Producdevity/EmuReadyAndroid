package com.emuready.emuready.domain.entities

/**
 * Enumeration of supported gaming device types
 * Used for device categorization and icon selection
 */
enum class DeviceType(val displayName: String) {
    STEAM_DECK("Steam Deck"),
    ROG_ALLY("ROG Ally"),
    LENOVO_LEGION_GO("Lenovo Legion Go"),
    AYA_NEO("Aya Neo"),
    ONEXPLAYER("OneXPlayer"),
    GPD_WIN("GPD Win"),
    GPD_POCKET("GPD Pocket"),
    HANDHELD_PC("Handheld PC"),
    PHONE_TABLET("Phone/Tablet"),
    UNKNOWN("Unknown Device");

    companion object {
        fun fromString(value: String?): DeviceType {
            return entries.find { 
                it.name.equals(value, ignoreCase = true) || 
                it.displayName.equals(value, ignoreCase = true)
            } ?: UNKNOWN
        }
    }
}