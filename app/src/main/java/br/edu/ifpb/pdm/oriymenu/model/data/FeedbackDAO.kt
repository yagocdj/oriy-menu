package br.edu.ifpb.pdm.oriymenu.model.data

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

class FeedbackDAO() {

    private val db = Firebase.firestore

    /**
     * Save a feedback in the database
     * @param feedback the feedback to be saved
     * @param callback function that will receive the saved feedback
     */
    fun save(feedback: Feedback, callback: (Feedback?) -> Unit) {
        db.collection("feedback").add(feedback)
            .addOnSuccessListener { documentReference ->
                if (documentReference != null) {
                    callback(feedback)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    private val storage = FirebaseStorage.getInstance().reference
    fun uploadImage(imageUri: Uri, dishName: String, callback: (String?) -> Unit) {
        val imageRef = storage.child("feedback_images/${dishName}.jpg")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    callback(uri.toString())
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

}
