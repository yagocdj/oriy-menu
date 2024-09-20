package br.edu.ifpb.pdm.oriymenu.model.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import java.util.Date

class MenuDAO {

    private val db = Firebase.firestore
    private val menuRef = db.collection("menu")

    /**
     * Save a menu in the database
     * @param menu the menu to be saved
     * @param callback function that will receive the saved menu
     */
    fun save(menu: Menu, callback: (Menu?) -> Unit) {
        menuRef.add(menu)
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
        menuRef.document(menu.id).set(menu)
            .addOnSuccessListener {
                callback(menu)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    /**
     * Get all menus and their dishes from the database
     * @param callback function that will receive the list of menus
     */
    fun findAll(callback: (List<Menu>) -> Unit) {
        menuRef.get()
            .addOnSuccessListener { menuDoc ->
                val menus = menuDoc.toObjects<Menu>()
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
    fun findByDate(date: Date, callback: (Menu?) -> Unit) {
        menuRef.whereEqualTo("date", date).get()
            .addOnSuccessListener { document ->
                val menu = document.toObjects<Menu>().firstOrNull()
                Log.d("MenuDAO", "Menu: ${menu?.date}")
                callback(menu)  // it can be null
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    /**
     * Search for a menu by its id
     * @param id the id of the menu
     * @param callback function that will receive the menu
     */
    fun findById(id: String, callback: (Menu?) -> Unit) {
        menuRef.document(id).get()
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
        menuRef.document(menu.id).delete()
            .addOnSuccessListener {
                callback(menu)
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}
