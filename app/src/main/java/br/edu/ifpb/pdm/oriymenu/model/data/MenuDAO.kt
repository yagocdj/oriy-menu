package br.edu.ifpb.pdm.oriymenu.model.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects

class MenuDAO {

    private val db = Firebase.firestore

    /**
     * Save a menu in the database
     * @param menu the menu to be saved
     * @param callback function that will receive the saved menu
     */
    fun save(menu: Menu, callback: (Menu?) -> Unit) {
        db.collection("menus").add(menu)
            .addOnSuccessListener { documentReference ->
                if (documentReference != null) {
                    callback(menu)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    /**
     * Update a menu in the database
     * @param menu the menu to be updated
     * @param callback function that will receive the updated menu
     */
    fun update(menu: Menu, callback: (Menu?) -> Unit) {
        db.collection("menus").document(menu.id).set(menu)
            .addOnSuccessListener {
                callback(menu)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    /**
     * Get all menus from the database
     * @param callback function that will receive the list of menus
     */
    fun findAll(callback: (List<Menu>) -> Unit) {
        db.collection("menus").get()
            .addOnSuccessListener { document ->
                val menus = document.toObjects<Menu>()
                callback(menus)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    /**
     * Search for a menu by its date
     * @param date the date of the menu
     * @param callback function that will receive the menu
     */
    fun findByDate(date: String, callback: (List<Menu>) -> Unit) {
        db.collection("menus").whereEqualTo("data", date).get()
            .addOnSuccessListener { document ->
                val menus = document.toObjects<Menu>()
                callback(menus)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    /**
     * Search for a menu by its id
     * @param id the id of the menu
     * @param callback function that will receive the menu
     */
    fun findById(id: String, callback: (Menu?) -> Unit) {
        db.collection("menus").document(id).get()
            .addOnSuccessListener { document ->
                val menu = document.toObject(Menu::class.java)
                callback(menu)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    /**
     * Delete a menu from the database
     * @param menu the menu to be deleted
     * @param callback function that will receive the deleted menu
     */
    fun delete(menu: Menu, callback: (Menu?) -> Unit) {
        db.collection("menus").document(menu.id).delete()
            .addOnSuccessListener {
                callback(menu)
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}
