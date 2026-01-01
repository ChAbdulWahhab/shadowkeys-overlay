package com.hackerkeyboardapp

data class CheatCode(
    val name: String,
    val code: String,
    val description: String,
    val category: String
)

object GTASACheats {
    val cheats = listOf(
        CheatCode("Health Pack", "HESOYAM", "Health, Armor, $250k", "Player"),
        CheatCode("Infinite Health", "BAGUVIX", "God mode", "Player"),
        CheatCode("Weapons 1", "LXGIWYL", "Weapon set 1", "Weapons"),
        CheatCode("Weapons 2", "KJKSZPJ", "Weapon set 2", "Weapons"),
        CheatCode("Weapons 3", "UZUMYMW", "Weapon set 3", "Weapons"),
        CheatCode("Jetpack", "ROCKETMAN", "Spawn jetpack", "Special"),
        CheatCode("Hydra", "JUMPJET", "Spawn Hydra", "Vehicles"),
        CheatCode("Tank", "PANZER", "Spawn Rhino tank", "Vehicles"),
        CheatCode("Infinite Ammo", "WANRLTW", "No reload", "Weapons"),
        CheatCode("Lower Wanted", "TURNDOWNTHEHEAT", "0 Stars", "Police"),
        CheatCode("Raise Wanted", "TURNUPTHEHEAT", "+2 Stars", "Police"),
        CheatCode("Super Jump", "KANGAROO", "Very high jump", "Player")
    )
}
