package br.edu.ifpb.pdm.oriymenu.model.data

import com.google.firebase.firestore.DocumentId

class Dish(
    @DocumentId
    val id: String = "",
    val name: String,
    val meal: String,
    val description: String,
    val pathToImage: String,
    var feedbacks: List<Feedback>
) {
    enum class Meal { BREAKFAST, LUNCH }
}
