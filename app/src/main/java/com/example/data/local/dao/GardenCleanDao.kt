package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.AppNotificationEntity
import com.example.data.local.entity.CareTaskEntity
import com.example.data.local.entity.FavoriteArticleEntity
import com.example.data.local.entity.SavedGardenEntity
import com.example.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GardenCleanDao {

    // Care Tasks
    @Query("SELECT * FROM care_tasks ORDER BY isCompleted ASC, id DESC")
    fun getAllTasks(): Flow<List<CareTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: CareTaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<CareTaskEntity>)

    @Update
    suspend fun updateTask(task: CareTaskEntity)

    @Query("UPDATE care_tasks SET isCompleted = :completed WHERE id = :id")
    suspend fun setTaskCompleted(id: Int, completed: Boolean)

    @Query("DELETE FROM care_tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    @Query("SELECT COUNT(*) FROM care_tasks")
    suspend fun getTaskCount(): Int

    // Saved Gardens
    @Query("SELECT * FROM saved_gardens ORDER BY createdAt DESC")
    fun getAllGardens(): Flow<List<SavedGardenEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGarden(garden: SavedGardenEntity): Long

    @Query("DELETE FROM saved_gardens WHERE id = :id")
    suspend fun deleteGardenById(id: Int)

    // Favorites
    @Query("SELECT * FROM favorite_articles")
    fun getAllFavorites(): Flow<List<FavoriteArticleEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_articles WHERE articleId = :articleId)")
    fun isFavorite(articleId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(fav: FavoriteArticleEntity)

    @Query("DELETE FROM favorite_articles WHERE articleId = :articleId")
    suspend fun removeFavorite(articleId: String)

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)

    // Notifications
    @Query("SELECT * FROM app_notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<AppNotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: AppNotificationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<AppNotificationEntity>)

    @Query("UPDATE app_notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: Int)

    @Query("UPDATE app_notifications SET isRead = 1")
    suspend fun markAllNotificationsAsRead()

    @Query("SELECT COUNT(*) FROM app_notifications")
    suspend fun getNotificationCount(): Int
}
