package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.GardenCleanDao
import com.example.data.local.entity.AppNotificationEntity
import com.example.data.local.entity.CareTaskEntity
import com.example.data.local.entity.FavoriteArticleEntity
import com.example.data.local.entity.SavedGardenEntity
import com.example.data.local.entity.UserProfileEntity

@Database(
    entities = [
        CareTaskEntity::class,
        SavedGardenEntity::class,
        FavoriteArticleEntity::class,
        UserProfileEntity::class,
        AppNotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gardenCleanDao(): GardenCleanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "garden_clean_jordan.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
