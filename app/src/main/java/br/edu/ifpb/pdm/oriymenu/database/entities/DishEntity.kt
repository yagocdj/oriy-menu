package br.edu.ifpb.pdm.oriymenu.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dish")
data class DishEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firestoreId: String = "",
    val name: String = "",
    val meal: String = "",
    val description: String = "",
    val pathToImage: String = "",
    var feedback: List<String> = emptyList() // Usar TypeConverter
)