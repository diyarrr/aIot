package com.kg.mobilecomapp.common

object IPAddress {
    var IpAddress: String = "http://localhost" // Default placeholder with valid scheme
//        set(value) {
//            if (isValidUrl(value)) {
//                field = value
//            } else {
//                throw IllegalArgumentException("Invalid IP Address: Must include 'http://' or 'https://'")
//            }
//        }
//
//    private fun isValidUrl(url: String): Boolean {
//        return try {
//            val uri = java.net.URI(url)
//            uri.scheme == "http" || uri.scheme == "https"
//        } catch (e: Exception) {
//            false
//        }
//    }
}