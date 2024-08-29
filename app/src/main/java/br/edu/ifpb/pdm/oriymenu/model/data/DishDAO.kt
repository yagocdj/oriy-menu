package br.edu.ifpb.pdm.oriymenu.model.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects

class DishDAO {

    private val db = Firebase.firestore

    /**
     * Save a dish in the database
     * @param dish the dish to be saved
     * @param callback function that will receive a
     * boolean indicating if the dish was saved
     */
    fun save(dish: Dish, callback: (Boolean) -> Unit) {
        db.collection("pratos").add(dish)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    /**
     * Update a dish in the database
     * @param dish the dish to be updated
     * @param callback function that will receive a
     * boolean indicating if the dish was updated
     */
    fun update(dish: Dish, callback: (Boolean) -> Unit) {
        db.collection("pratos").document(dish.id).set(dish)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    /**
     * Get all dishes from the database
     * @param callback function that will receive the list of dishes
     */
    fun findAll(callback: (List<Dish>) -> Unit) {
        db.collection("pratos").get()
            .addOnSuccessListener { document ->
                val dishes = document.toObjects<Dish>()
                callback(dishes)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    /**
     * Get all dishes from the database with their feedbacks
     * @param callback function that will receive the list of dishes
     */
    fun findAllWithFeedbacks(callback: (List<Dish>) -> Unit) {
        db.collection("pratos").get()
            .addOnSuccessListener { document ->
                val dishes = document.toObjects<Dish>()
                dishes.forEach { dish ->
                    // Search for feedbacks of each dish
                    db.collection("pratos").document(dish.id)
                        .collection("feedbacks").get()
                        .addOnSuccessListener { document ->
                            val feedbacks = document.toObjects<Feedback>()
                            dish.feedbacks = feedbacks
                        }
                        .addOnFailureListener {
                            dish.feedbacks = emptyList()
                        }
                }
                callback(dishes)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    /**
     * Search for a dish by its id
     * @param id the id of the dish
     * @param callback function that will receive the dish
     */
    fun searchById(id: String, callback: (Dish?) -> Unit) {
        db.collection("pratos").document(id).get()
            .addOnSuccessListener { document ->
                val dish = document.toObject(Dish::class.java)
                callback(dish)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    /**
     * Delete a dish from the database
     * @param dish the dish to be deleted
     * @param callback function that will receive a
     * boolean indicating if the dish was deleted
     */
    fun delete(dish: Dish, callback: (Boolean) -> Unit) {
        db.collection("pratos").document(dish.id).delete()
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

}
