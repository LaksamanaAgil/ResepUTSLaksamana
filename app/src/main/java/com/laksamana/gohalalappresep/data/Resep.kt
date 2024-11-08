package com.laksamana.gohalalappresep.data

import androidx.annotation.DrawableRes
import com.laksamana.gohalalappresep.R

// I have prepared the following data structures and resources to skip the boring part

data class Resep(
    val title: String,
    val category: String,
    val cookingTime: String,
    val energy: String,
    val rating: String,
    val description: String,
    val reviews: String,
    val ingredients: List<Ingredient>
)

data class Ingredient(@DrawableRes val image: Int, val title: String, val berat: Int)

val strawberryCake = Resep( //TODO: public Value yang berisi informasi dasar mengenai resep kue stroberi
    title = "Kue Strawberry",
    category = "Desserts",
    cookingTime = "50 min",
    energy = "620 kcal",
    rating = "4,9",
    description = "Kue stroberi yang lezat dan mudah dibuat, kue ini juga dapat dibuat dengan buah berrry lainnya.",
    reviews = "84 foto     430 komentar",
    ingredients = listOf(
        Ingredient(R.drawable.flour, "Tepung", 450 ),
        Ingredient(R.drawable.eggs, "Telur", 4),
        Ingredient(R.drawable.juice, "Jus Lemon", 150),
        Ingredient(R.drawable.strawberry, "Strawberry", 200 ),
        Ingredient(R.drawable.suggar, "Gula", 200),
        Ingredient(R.drawable.mind, "Mint", 20),
        Ingredient(R.drawable.vanilla, "Ekstrak Vanilla", 4 ),
    )
)

