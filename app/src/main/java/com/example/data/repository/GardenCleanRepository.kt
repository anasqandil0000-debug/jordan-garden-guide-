package com.example.data.repository

import com.example.data.cloud.FirestoreSyncService
import com.example.data.datasource.SampleData
import com.example.data.local.dao.GardenCleanDao
import com.example.data.local.entity.AppNotificationEntity
import com.example.data.local.entity.CareTaskEntity
import com.example.data.local.entity.FavoriteArticleEntity
import com.example.data.local.entity.SavedGardenEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.Article
import com.example.data.model.GardenAnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class GardenCleanRepository(private val dao: GardenCleanDao) {

    val allTasks: Flow<List<CareTaskEntity>> = dao.getAllTasks().flowOn(Dispatchers.IO)
    val allGardens: Flow<List<SavedGardenEntity>> = dao.getAllGardens().flowOn(Dispatchers.IO)
    val favoriteArticles: Flow<List<FavoriteArticleEntity>> = dao.getAllFavorites().flowOn(Dispatchers.IO)
    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile().flowOn(Dispatchers.IO)
    val allNotifications: Flow<List<AppNotificationEntity>> = dao.getAllNotifications().flowOn(Dispatchers.IO)

    suspend fun initializeDefaultsIfNeeded() = withContext(Dispatchers.IO) {
        if (dao.getTaskCount() == 0) {
            dao.insertTasks(SampleData.initialCareTasks)
        }
        if (dao.getNotificationCount() == 0) {
            dao.insertNotifications(SampleData.initialNotifications)
        }
        val profile = dao.getUserProfile().firstOrNull()
        if (profile == null) {
            dao.saveUserProfile(UserProfileEntity())
        }
    }

    suspend fun toggleTaskCompleted(id: Int, completed: Boolean) = withContext(Dispatchers.IO) {
        dao.setTaskCompleted(id, completed)
        val userEmail = dao.getUserProfile().firstOrNull()?.email ?: "user@jordan.jo"
        val allTasks = dao.getAllTasks().firstOrNull()
        val updatedTask = allTasks?.find { it.id == id }?.copy(isCompleted = completed)
        if (updatedTask != null) {
            FirestoreSyncService.syncTaskToCloud(userEmail, updatedTask)
        }
    }

    suspend fun addNewTask(task: CareTaskEntity) = withContext(Dispatchers.IO) {
        val insertedId = dao.insertTask(task)
        val userEmail = dao.getUserProfile().firstOrNull()?.email ?: "user@jordan.jo"
        FirestoreSyncService.syncTaskToCloud(userEmail, task.copy(id = insertedId.toInt()))
    }

    suspend fun deleteTask(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteTaskById(id)
    }

    suspend fun isArticleFavorite(articleId: String): Boolean = withContext(Dispatchers.IO) {
        dao.isFavorite(articleId).firstOrNull() ?: false
    }

    suspend fun toggleFavorite(articleId: String, isFav: Boolean) = withContext(Dispatchers.IO) {
        if (isFav) {
            dao.removeFavorite(articleId)
        } else {
            dao.addFavorite(FavoriteArticleEntity(articleId = articleId))
        }
        val userEmail = dao.getUserProfile().firstOrNull()?.email ?: "user@jordan.jo"
        FirestoreSyncService.syncFavoriteToCloud(userEmail, articleId, !isFav)
    }

    suspend fun saveGarden(
        name: String,
        length: Double,
        width: Double,
        city: String,
        soilType: String,
        sunExposure: String,
        waterSource: String,
        analysisResult: GardenAnalysisResult
    ): Long = withContext(Dispatchers.IO) {
        val plantsStr = analysisResult.plants.joinToString(" • ") { it.name }
        val stepsStr = analysisResult.executiveSteps.joinToString("\n")
        val entity = SavedGardenEntity(
            name = name.ifBlank { "حديقة $city (${length.toInt()}×${width.toInt()}م)" },
            lengthMeters = length,
            widthMeters = width,
            areaMeters = analysisResult.areaMeters,
            city = city,
            soilType = soilType,
            sunExposure = sunExposure,
            waterSource = waterSource,
            analysisSummary = analysisResult.irrigationPlan,
            recommendedPlants = plantsStr,
            carePlan = stepsStr
        )
        dao.insertGarden(entity)
    }

    suspend fun deleteGarden(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteGardenById(id)
    }

    suspend fun saveProfile(profile: UserProfileEntity) = withContext(Dispatchers.IO) {
        dao.saveUserProfile(profile)
    }

    suspend fun markNotificationRead(id: Int) = withContext(Dispatchers.IO) {
        dao.markNotificationAsRead(id)
    }

    suspend fun markAllNotificationsRead() = withContext(Dispatchers.IO) {
        dao.markAllNotificationsAsRead()
    }

    suspend fun sendSimulatedNotification(title: String, message: String, type: String = "EXPERT_TIP") = withContext(Dispatchers.IO) {
        dao.insertNotification(
            AppNotificationEntity(
                title = title,
                message = message,
                type = type
            )
        )
    }

    fun getAllArticles(): List<Article> = SampleData.articles

    fun getArticleById(id: String): Article? = SampleData.articles.find { it.id == id }
}
