package br.edu.ifpb.pdm.oriymenu.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "menu")
data class MenuEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firestoreId: String = "", // Para mapear com Firestore
    val date: Date? = null,
    var dishes: List<String> = emptyList() // Usar TypeConverter para persistir
)

