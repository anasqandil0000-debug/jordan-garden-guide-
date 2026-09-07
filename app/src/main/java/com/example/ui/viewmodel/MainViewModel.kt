package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.SampleData
import com.example.data.local.AppDatabase
import com.example.data.local.entity.AppNotificationEntity
import com.example.data.local.entity.CareTaskEntity
import com.example.data.local.entity.SavedGardenEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.AgriculturalSeason
import com.example.data.model.Article
import com.example.data.model.ArticleCategory
import com.example.data.model.GardenAnalysisResult
import com.example.data.repository.GardenCleanRepository
import com.example.network.GeminiGardenAdvisor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

enum class NavTab(val title: String, val iconName: String) {
    HOME("الرئيسية", "home"),
    ARTICLES("الدليل والمقالات", "menu_book"),
    PLANNER("مخطط الحديقة", "architecture"),
    SCHEDULE("جداول العناية", "event_note"),
    PROFILE("حسابي", "person")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GardenCleanRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = GardenCleanRepository(db.gardenCleanDao())
        viewModelScope.launch {
            repository.initializeDefaultsIfNeeded()
        }
    }

    // Navigation State
    private val _currentTab = MutableStateFlow(NavTab.HOME)
    val currentTab: StateFlow<NavTab> = _currentTab.asStateFlow()

    fun selectTab(tab: NavTab) {
        _currentTab.value = tab
    }

    // Active Article Reader
    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    fun selectArticle(article: Article?) {
        _selectedArticle.value = article
    }

    // Articles Search & Filter
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ArticleCategory.ALL)
    val selectedCategory: StateFlow<ArticleCategory> = _selectedCategory.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: ArticleCategory) {
        _selectedCategory.value = category
    }

    // Seasonal Advisor State
    private val _selectedSeason = MutableStateFlow(getCurrentSeason())
    val selectedSeason: StateFlow<AgriculturalSeason> = _selectedSeason.asStateFlow()

    fun selectSeason(season: AgriculturalSeason) {
        _selectedSeason.value = season
    }

    private fun getCurrentSeason(): AgriculturalSeason {
        val month = Calendar.getInstance().get(Calendar.MONTH) // 0-based
        return when (month) {
            Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY -> AgriculturalSeason.SPRING
            Calendar.JUNE, Calendar.JULY, Calendar.AUGUST -> AgriculturalSeason.SUMMER
            Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER -> AgriculturalSeason.AUTUMN
            else -> AgriculturalSeason.WINTER
        }
    }

    // Data from Repository
    val allTasks: StateFlow<List<CareTaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedGardens: StateFlow<List<SavedGardenEntity>> = repository.allGardens
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteArticleIds: StateFlow<Set<String>> = repository.favoriteArticles
        .map { list -> list.map { it.articleId }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val userProfile: StateFlow<UserProfileEntity> = repository.userProfile
        .map { it ?: UserProfileEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfileEntity())

    val notifications: StateFlow<List<AppNotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadCount: StateFlow<Int> = notifications
        .map { list -> list.count { !it.isRead } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Garden Planner Inputs & Output
    val plannerLength = MutableStateFlow("8")
    val plannerWidth = MutableStateFlow("5")
    val plannerCity = MutableStateFlow(SampleData.jordanianCities[0])
    val plannerSoil = MutableStateFlow(SampleData.soilTypes[0])
    val plannerSun = MutableStateFlow(SampleData.sunExposures[0])
    val plannerWater = MutableStateFlow(SampleData.waterSources[0])
    val plannerImageBitmap = MutableStateFlow<Bitmap?>(null)

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analysisResult = MutableStateFlow<GardenAnalysisResult?>(null)
    val analysisResult: StateFlow<GardenAnalysisResult?> = _analysisResult.asStateFlow()

    fun runGardenAnalysis() {
        viewModelScope.launch {
            _isAnalyzing.value = true
            val l = plannerLength.value.toDoubleOrNull() ?: 8.0
            val w = plannerWidth.value.toDoubleOrNull() ?: 5.0
            val result = GeminiGardenAdvisor.analyzeGardenWithAi(
                length = l,
                width = w,
                city = plannerCity.value,
                soilType = plannerSoil.value,
                sunExposure = plannerSun.value,
                waterSource = plannerWater.value,
                imageBitmap = plannerImageBitmap.value
            )
            _analysisResult.value = result
            _isAnalyzing.value = false
        }
    }

    fun saveCurrentGarden(name: String) {
        val result = _analysisResult.value ?: return
        val l = plannerLength.value.toDoubleOrNull() ?: 8.0
        val w = plannerWidth.value.toDoubleOrNull() ?: 5.0
        viewModelScope.launch {
            repository.saveGarden(
                name = name,
                length = l,
                width = w,
                city = plannerCity.value,
                soilType = plannerSoil.value,
                sunExposure = plannerSun.value,
                waterSource = plannerWater.value,
                analysisResult = result
            )
            // Send in-app notification
            repository.sendSimulatedNotification(
                title = "تم حفظ حديقتك بنجاح! 🌿",
                message = "تمت إضافة مخطط الحديقة إلى ملفك الشخصي مع خطة الري والنباتات المقترحة.",
                type = "TASK_REMINDER"
            )
        }
    }

    fun deleteGarden(id: Int) {
        viewModelScope.launch {
            repository.deleteGarden(id)
        }
    }

    // Toggle favorite article
    fun toggleFavorite(articleId: String) {
        val isFav = favoriteArticleIds.value.contains(articleId)
        viewModelScope.launch {
            repository.toggleFavorite(articleId, isFav)
        }
    }

    // Tasks Management
    fun toggleTask(id: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskCompleted(id, isCompleted)
        }
    }

    fun addTask(title: String, category: String, frequency: String, dueDate: String, notes: String) {
        viewModelScope.launch {
            repository.addNewTask(
                CareTaskEntity(
                    title = title,
                    category = category,
                    frequency = frequency,
                    dueDate = dueDate,
                    notes = notes
                )
            )
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    // Profile Management
    fun updateProfile(name: String, email: String, city: String, gardenType: String, notifs: Boolean) {
        viewModelScope.launch {
            val current = userProfile.value
            repository.saveProfile(
                current.copy(
                    name = name,
                    email = email,
                    city = city,
                    gardenType = gardenType,
                    notificationsEnabled = notifs
                )
            )
        }
    }

    // Notification actions
    fun markNotificationRead(id: Int) {
        viewModelScope.launch {
            repository.markNotificationRead(id)
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsRead()
        }
    }

    fun sendTestNotification(title: String, message: String) {
        viewModelScope.launch {
            repository.sendSimulatedNotification(title, message, "EXPERT_TIP")
        }
    }
}
