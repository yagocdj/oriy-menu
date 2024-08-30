package br.edu.ifpb.pdm.oriymenu.model.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDate
import java.util.Date

data class Menu(
    @DocumentId
    val id: String = "",
    val date: Date? = null,
    var dishes: List<Dish> = emptyList()
)
