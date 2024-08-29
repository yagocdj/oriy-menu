package br.edu.ifpb.pdm.oriymenu.model.data

import com.google.firebase.firestore.DocumentId
import java.time.LocalDate

class Menu(
    @DocumentId
    val id: String = "",
    val meal: Meal,
    val date: LocalDate,
    val dishes: List<Dish>) {

    enum class Meal { BREAKFAST, LUNCH }
}

