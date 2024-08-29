package br.edu.ifpb.pdm.oriymenu.model.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AdminDAO {

    private val db = Firebase.firestore

    /**
     * Find an admin by its id
     * @param id the id of the admin
     * @param callback function that will receive the admin
     */
    fun findById(id: String, callback: (Admin?) -> Unit) {
        db.collection("administrador").document(id).get()
            .addOnSuccessListener { document ->
                val admin = document.toObject(Admin::class.java)
                callback(admin)
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}
