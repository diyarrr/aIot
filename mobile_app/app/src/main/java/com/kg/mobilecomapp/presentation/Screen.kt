package com.kg.mobilecomapp.presentation

sealed class Screen(
    val route : String
)
{
    data object AudioScreen : Screen("audio_screen")
    data object IntruderScreen : Screen("intruder_screen")
    data object IPAddressScreen : Screen("ip_address_screen")
}
