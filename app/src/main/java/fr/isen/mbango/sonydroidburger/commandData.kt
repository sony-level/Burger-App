package fr.isen.mbango.sonydroidburger


data class CommandData(
    val firstName: String,
    val lastName: String,
    val address: String,
    val phoneNumber: String,
    val selectedBurger: String,
    val deliveryTime: String
)