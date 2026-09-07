package com.example.data.model

import androidx.annotation.DrawableRes

enum class ArticleCategory(val title: String) {
    ALL("الكل"),
    GARDEN("الحدائق والأشجار"),
    CLEANING("تنظيف المنزل"),
    JORDAN_HERITAGE("نباتات أردنية"),
    SEASONAL("المواسم والتقليم"),
    WATER_SAVING("ترشيد المياه")
}

data class Article(
    val id: String,
    val title: String,
    val category: ArticleCategory,
    val summary: String,
    val content: String,
    val readingTime: String = "4 دقائق",
    val tools: List<String> = emptyList(),
    val jordanTip: String = "",
    @DrawableRes val imageRes: Int? = null,
    val tags: List<String> = emptyList(),
    val author: String = "خبير زراعي أردني"
)

enum class AgriculturalSeason(val arabicTitle: String, val months: String, val icon: String) {
    SPRING("الربيع الأردني", "شباط - أيار", "🌸"),
    SUMMER("الصيف الأردني", "حزيران - آب", "☀️"),
    AUTUMN("موسم الزيتون والخريف", "أيلول - تشرين الثاني", "🍂"),
    WINTER("الشتاء والمربعانية", "كانون الأول - شباط", "🌧️")
}

data class SeasonalAdvisorInfo(
    val season: AgriculturalSeason,
    val summary: String,
    val tempRange: String,
    val agriculturalTasks: List<String>,
    val homeCleaningTasks: List<String>,
    val regionalTips: List<Pair<String, String>>, // Region name to tip
    val activeAlert: String? = null
)

data class ZoneBreakdown(
    val name: String,
    val percentage: Int,
    val description: String
)

data class PlantRecommendation(
    val name: String,
    val scientificName: String,
    val bestSeason: String,
    val waterNeed: String, // "منخفض جداً", "معتدل", "مرتفع"
    val suitableFor: String, // "شمس مباشرة", "ظل جزئي", "مصد رياح"
    val jordanHeritageNote: String
)

data class GardenAnalysisResult(
    val title: String,
    val areaMeters: Double,
    val irrigationPlan: String,
    val estimatedWaterLitersWeekly: Int,
    val zones: List<ZoneBreakdown>,
    val plants: List<PlantRecommendation>,
    val executiveSteps: List<String>,
    val jordanianHeritageAdvice: String
)
