package com.example.network

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import com.example.data.datasource.SampleData
import com.example.data.model.GardenAnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

object GeminiGardenAdvisor {

    private const val TAG = "GeminiGardenAdvisor"
    private const val MODEL_ENDPOINT =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun Bitmap.toBase64Jpeg(): String {
        val outputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    suspend fun analyzeGardenWithAi(
        length: Double,
        width: Double,
        city: String,
        soilType: String,
        sunExposure: String,
        waterSource: String,
        imageBitmap: Bitmap? = null
    ): GardenAnalysisResult = withContext(Dispatchers.IO) {
        val baseResult = SampleData.analyzeGarden(length, width, city, soilType, sunExposure, waterSource)

        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d(TAG, "No valid Gemini API key provided, using authentic Jordanian offline smart engine.")
            return@withContext baseResult
        }

        try {
            val promptText = """
                أنت مهندس زراعي وخبير تنسيق حدائق وعناية بالمنازل أردني معتمد، متخصص في البيئة الأردنية والمناخ المحلي (عمان، المرتفعات، الأغوار، والبادية).
                قم بتحليل وتخطيط الحديقة التالية وتقديم توصيات مخصصة باللهجة والأسلوب المهني الأردني:
                - الأبعاد: الطول ${length}م × العرض ${width}م (المساحة: ${length * width} متر مربع)
                - المدينة / المنطقة: $city
                - نوع التربة: $soilType
                - التعرض للشمس: $sunExposure
                - مصدر المياه: $waterSource
                ${if (imageBitmap != null) "مرفق صورة حقيقية للحديقة/النبات؛ تفحصها بدقة وقدم تشخيصاً لحالة المكان وتنسيق الأحواض." else ""}

                أعطني نصيحة وتوصية زراعية وتنظيفية بأسلوب مشجع يتضمن:
                1. نصيحة أردنية أصيلة متوافقة مع الحجر الأردني وشح المياه ونباتات الزيتون والميرمية والياسمين.
                2. خطة ري ذكية بالتنقيط.
                3. أهم 3 نباتات موصى بها.
            """.trimIndent()

            val partsArray = JSONArray()
            val textPart = JSONObject().put("text", promptText)
            partsArray.put(textPart)

            if (imageBitmap != null) {
                val inlineData = JSONObject()
                    .put("mimeType", "image/jpeg")
                    .put("data", imageBitmap.toBase64Jpeg())
                val imagePart = JSONObject().put("inlineData", inlineData)
                partsArray.put(imagePart)
            }

            val contentsArray = JSONArray()
            contentsArray.put(JSONObject().put("parts", partsArray))

            val requestJson = JSONObject()
                .put("contents", contentsArray)

            val requestBody = requestJson.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaType())

            val request = Request.Builder()
                .url("$MODEL_ENDPOINT?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val respBodyString = response.body?.string() ?: ""
                val jsonResponse = JSONObject(respBodyString)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    val aiText = parts?.optJSONObject(0)?.optString("text")
                    if (!aiText.isNullOrBlank()) {
                        return@withContext baseResult.copy(
                            jordanianHeritageAdvice = aiText.trim()
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gemini API call failed, falling back to local engine: ${e.message}")
        }

        return@withContext baseResult
    }
}
