package br.edu.ifpb.pdm.oriymenu

import br.edu.ifpb.pdm.oriymenu.database.entities.DishEntity
import br.edu.ifpb.pdm.oriymenu.database.entities.MenuEntity
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.Menu

// Converter Dish (Firestore) em DishEntity (Room)
fun Dish.toDishEntity(): DishEntity {
    return DishEntity(
        firestoreId = this.id,
        name = this.name,
        meal = this.meal,
        description = this.description,
        pathToImage = this.pathToImage,
        feedback = this.feedback
    )
}

// Converter Menu (Firestore) em MenuEntity (Room)
fun Menu.toMenuEntity(): MenuEntity {
    return MenuEntity(
        firestoreId = this.id,
        date = this.date,
        dishes = this.dishes
    )
}
