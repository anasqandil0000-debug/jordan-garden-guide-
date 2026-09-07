package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "care_tasks")
data class CareTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "حديقة" أو "تنظيف"
    val frequency: String, // "يومي", "أسبوعي", "شهري", "موسمي"
    val dueDate: String,
    val isCompleted: Boolean = false,
    val notes: String = ""
)

@Entity(tableName = "saved_gardens")
data class SavedGardenEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val lengthMeters: Double,
    val widthMeters: Double,
    val areaMeters: Double,
    val city: String,
    val soilType: String,
    val sunExposure: String,
    val waterSource: String,
    val analysisSummary: String,
    val recommendedPlants: String, // Comma or newline separated
    val carePlan: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_articles")
data class FavoriteArticleEntity(
    @PrimaryKey val articleId: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "أبو عمر الأردني",
    val email: String = "anasqandil0000@gmail.com",
    val city: String = "عمان - دابوق",
    val gardenType: String = "حديقة منزلية وتراس حجري",
    val isLoggedIn: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val gardenCareScore: Int = 88
)

@Entity(tableName = "app_notifications")
data class AppNotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String = "EXPERT_TIP"
)
