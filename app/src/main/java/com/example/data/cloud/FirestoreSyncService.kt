package com.example.data.cloud

import android.util.Log
import com.example.data.local.entity.CareTaskEntity
import com.example.data.local.entity.FavoriteArticleEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirestoreSyncService {

    private const val TAG = "FirestoreSyncService"

    private val firestoreInstance: FirebaseFirestore?
        get() = try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.w(TAG, "Firestore instance not available or uninitialized: ${e.message}")
            null
        }

    /**
     * Upload or update a care task in the cloud under the user's specific collection
     */
    suspend fun syncTaskToCloud(userEmail: String, task: CareTaskEntity): Boolean = withContext(Dispatchers.IO) {
        val db = firestoreInstance ?: return@withContext false
        val cleanEmail = userEmail.replace(".", "_")
        try {
            val taskMap = hashMapOf(
                "id" to task.id,
                "title" to task.title,
                "category" to task.category,
                "frequency" to task.frequency,
                "dueDate" to task.dueDate,
                "isCompleted" to task.isCompleted,
                "notes" to task.notes,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("users")
                .document(cleanEmail)
                .collection("care_tasks")
                .document(task.id.toString())
                .set(taskMap, SetOptions.merge())
                .await()
            Log.d(TAG, "Task ${task.id} synced to cloud successfully for $userEmail")
            true
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync task to cloud: ${e.message}")
            false
        }
    }

    /**
     * Sync favorite article status to cloud
     */
    suspend fun syncFavoriteToCloud(userEmail: String, articleId: String, isFavorite: Boolean): Boolean = withContext(Dispatchers.IO) {
        val db = firestoreInstance ?: return@withContext false
        val cleanEmail = userEmail.replace(".", "_")
        try {
            val docRef = db.collection("users")
                .document(cleanEmail)
                .collection("favorite_articles")
                .document(articleId)

            if (isFavorite) {
                val favMap = hashMapOf(
                    "articleId" to articleId,
                    "savedAt" to System.currentTimeMillis()
                )
                docRef.set(favMap).await()
            } else {
                docRef.delete().await()
            }
            Log.d(TAG, "Favorite $articleId updated in cloud (isFavorite=$isFavorite)")
            true
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync favorite to cloud: ${e.message}")
            false
        }
    }

    /**
     * Fetch user's saved favorite article IDs from cloud
     */
    suspend fun fetchCloudFavorites(userEmail: String): List<String> = withContext(Dispatchers.IO) {
        val db = firestoreInstance ?: return@withContext emptyList()
        val cleanEmail = userEmail.replace(".", "_")
        try {
            val snapshot = db.collection("users")
                .document(cleanEmail)
                .collection("favorite_articles")
                .get()
                .await()
            snapshot.documents.mapNotNull { it.getString("articleId") }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to fetch favorites from cloud: ${e.message}")
            emptyList()
        }
    }
}
