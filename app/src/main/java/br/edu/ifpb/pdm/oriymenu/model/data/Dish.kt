package br.edu.ifpb.pdm.oriymenu.model.data

import com.google.firebase.firestore.DocumentId

class Dish(
    @DocumentId
    val id: String = "",
    val name: String,
    val mealType: MealType,
    val description: String,
    val pathToImage: String,
    var feedbacks: List<Feedback>
) {
    enum class MealType { VEGETARIAN, VEGAN, WITH_MEAT }
}
