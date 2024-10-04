package br.edu.ifpb.pdm.oriymenu.model.data

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class FeedbackDAO {

    private val db = Firebase.firestore

    /**
     * Save a feedback in the database
     * @param feedback the feedback to be saved
     * @param callback function that will receive the saved feedback
     */
    fun save(feedback: Feedback, callback: (DocumentReference?) -> Unit) {
        db.collection("feedback").add(feedback)
            .addOnSuccessListener { documentReference ->
                if (documentReference != null) {
                    callback(documentReference)
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
        val uniqueImageName = "${dishName}_${UUID.randomUUID()}.jpg"
        val imageRef = storage.child("images/$uniqueImageName")

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

    fun getFeedbacksForDish(dish: Dish, callback: (List<Feedback>) -> Unit) {
        val feedbackIds = dish.feedback // Supondo que dish tenha uma lista de feedbackIds
        val feedbacks = mutableListOf<Feedback>()

        if (feedbackIds.isEmpty()) {
            callback(emptyList())
            return
        }

        feedbackIds.forEach { feedbackId ->
            db.collection("feedback").document(feedbackId).get()
                .addOnSuccessListener { document ->
                    document.toObject(Feedback::class.java)?.let { feedback ->
                        feedbacks.add(feedback)
                    }

                    // Quando tiver obtido todos os feedbacks
                    if (feedbacks.size == feedbackIds.size) {
                        callback(feedbacks)
                    }
                }
                .addOnFailureListener {
                    // Caso haja falha, você pode decidir como tratar (ex: retornar lista parcial)
                    callback(emptyList())
                }
        }
    }

}
