package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.datasource.SampleData
import com.example.data.model.GardenAnalysisResult
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenPlannerScreen(viewModel: MainViewModel) {
    val length by viewModel.plannerLength.collectAsStateWithLifecycle()
    val width by viewModel.plannerWidth.collectAsStateWithLifecycle()
    val city by viewModel.plannerCity.collectAsStateWithLifecycle()
    val soil by viewModel.plannerSoil.collectAsStateWithLifecycle()
    val sun by viewModel.plannerSun.collectAsStateWithLifecycle()
    val water by viewModel.plannerWater.collectAsStateWithLifecycle()
    val imageBitmap by viewModel.plannerImageBitmap.collectAsStateWithLifecycle()
    val isAnalyzing by viewModel.isAnalyzing.collectAsStateWithLifecycle()
    val analysisResult by viewModel.analysisResult.collectAsStateWithLifecycle()

    var showSaveDialog by remember { mutableStateOf(false) }
    var gardenSaveName by remember { mutableStateOf("") }
    var saveSuccessMessage by remember { mutableStateOf(false) }

    // Dropdowns expanded states
    var cityExpanded by remember { mutableStateOf(false) }
    var soilExpanded by remember { mutableStateOf(false) }
    var sunExpanded by remember { mutableStateOf(false) }
    var waterExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val lengthNum = length.toDoubleOrNull() ?: 8.0
    val widthNum = width.toDoubleOrNull() ?: 5.0
    val area = lengthNum * widthNum

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Landscape,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "المخطط الذكي للحديقة الأردنية",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "حدد أبعاد الحديقة وموقعها بالأردن للحصول على خطة نباتات وري احترافية",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }

            // Dimensions Input Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "أبعاد الحديقة (بالأمتار):",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Length
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "الطول (متر)", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            val cur = length.toIntOrNull() ?: 8
                                            if (cur > 1) viewModel.plannerLength.value = (cur - 1).toString()
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "تقليل")
                                    }

                                    OutlinedTextField(
                                        value = length,
                                        onValueChange = { viewModel.plannerLength.value = it },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        modifier = Modifier.weight(1f)
                                    )

                                    IconButton(
                                        onClick = {
                                            val cur = length.toIntOrNull() ?: 8
                                            viewModel.plannerLength.value = (cur + 1).toString()
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "زيادة")
                                    }
                                }
                            }

                            // Width
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "العرض (متر)", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            val cur = width.toIntOrNull() ?: 5
                                            if (cur > 1) viewModel.plannerWidth.value = (cur - 1).toString()
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "تقليل")
                                    }

                                    OutlinedTextField(
                                        value = width,
                                        onValueChange = { viewModel.plannerWidth.value = it },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        modifier = Modifier.weight(1f)
                                    )

                                    IconButton(
                                        onClick = {
                                            val cur = width.toIntOrNull() ?: 5
                                            viewModel.plannerWidth.value = (cur + 1).toString()
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "زيادة")
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Area calculation badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "المساحة الإجمالية المقدرة:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "${area.toInt()} م² (متر مربع)",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            // Environment & Jordanian Region Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "الموقع والبيئة الزراعية بالأردن:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // City Selector
                        ExposedDropdownMenuBox(
                            expanded = cityExpanded,
                            onExpandedChange = { cityExpanded = !cityExpanded }
                        ) {
                            OutlinedTextField(
                                value = city,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("المدينة / المحافظة") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = cityExpanded,
                                onDismissRequest = { cityExpanded = false }
                            ) {
                                SampleData.jordanianCities.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item, fontSize = 13.sp) },
                                        onClick = {
                                            viewModel.plannerCity.value = item
                                            cityExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Soil Selector
                        ExposedDropdownMenuBox(
                            expanded = soilExpanded,
                            onExpandedChange = { soilExpanded = !soilExpanded }
                        ) {
                            OutlinedTextField(
                                value = soil,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("نوع التربة") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = soilExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = soilExpanded,
                                onDismissRequest = { soilExpanded = false }
                            ) {
                                SampleData.soilTypes.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item, fontSize = 13.sp) },
                                        onClick = {
                                            viewModel.plannerSoil.value = item
                                            soilExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Sun & Water Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Sun
                            ExposedDropdownMenuBox(
                                expanded = sunExpanded,
                                onExpandedChange = { sunExpanded = !sunExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = sun.take(15) + "...",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("الشمس") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sunExpanded) },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = sunExpanded,
                                    onDismissRequest = { sunExpanded = false }
                                ) {
                                    SampleData.sunExposures.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item, fontSize = 13.sp) },
                                            onClick = {
                                                viewModel.plannerSun.value = item
                                                sunExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Water
                            ExposedDropdownMenuBox(
                                expanded = waterExpanded,
                                onExpandedChange = { waterExpanded = !waterExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = water.take(15) + "...",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("مصدر الماء") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = waterExpanded) },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = waterExpanded,
                                    onDismissRequest = { waterExpanded = false }
                                ) {
                                    SampleData.waterSources.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item, fontSize = 13.sp) },
                                            onClick = {
                                                viewModel.plannerWater.value = item
                                                waterExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Optional Photo Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "صورة الحديقة أو النبات (اختياري):",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (imageBitmap != null) {
                                TextButton(onClick = { viewModel.plannerImageBitmap.value = null }) {
                                    Text("حذف الصورة", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (imageBitmap == null) {
                            OutlinedButton(
                                onClick = {
                                    // Load sample garden bitmap for high fidelity demo
                                    val bmp = BitmapFactory.decodeResource(context.resources, R.drawable.img_jordan_garden)
                                    viewModel.plannerImageBitmap.value = bmp
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("إدراج صورة الحديقة للتحليل البصري")
                            }
                        } else {
                            Image(
                                bitmap = imageBitmap!!.asImageBitmap(),
                                contentDescription = "صورة الحديقة المرفقة",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                }
            }

            // Run Analysis Action Button
            item {
                Button(
                    onClick = { viewModel.runGardenAnalysis() },
                    enabled = !isAnalyzing,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("جاري التحليل واستشارة الخبير الأردني...", fontSize = 14.sp)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تحليل الحديقة وتوليد الخطة الذكية 🌿",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Save confirmation banner
            if (saveSuccessMessage) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "تم حفظ الحديقة بنجاح في ملفك الشخصي!",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Analysis Results Section
            if (analysisResult != null) {
                val result = analysisResult!!
                item {
                    AnalysisResultCard(
                        result = result,
                        onSaveClick = {
                            gardenSaveName = "حديقة $city (${lengthNum.toInt()}×${widthNum.toInt()}م)"
                            showSaveDialog = true
                        }
                    )
                }
            }
        }

        // Save Dialog
        if (showSaveDialog) {
            AlertDialog(
                onDismissRequest = { showSaveDialog = false },
                title = { Text("حفظ المخطط في حسابك", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("أدخل اسماً مميزاً لحديقتك للرجوع إليها في أي وقت وتتبع جداول العناية:")
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = gardenSaveName,
                            onValueChange = { gardenSaveName = it },
                            label = { Text("اسم الحديقة") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.saveCurrentGarden(gardenSaveName)
                            showSaveDialog = false
                            saveSuccessMessage = true
                        }
                    ) {
                        Text("حفظ المخطط")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSaveDialog = false }) {
                        Text("إلغاء")
                    }
                }
            )
        }
    }
}

@Composable
fun AnalysisResultCard(
    result: GardenAnalysisResult,
    onSaveClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Badge & Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "نتائج التحليل الذكي 🇯🇴",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Button(
                    onClick = onSaveClick,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("حفظ الحديقة", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = result.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Water & Area Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "المساحة الكلية", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                        Text(
                            text = "${result.areaMeters.toInt()} م²",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "الاستهلاك المقدر للري", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                        Text(
                            text = "~${result.estimatedWaterLitersWeekly} لتر/أسبوع",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Jordanian Heritage Advice
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🏛️", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "نصيحة المهندس الزراعي الأردني:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = result.jordanianHeritageAdvice,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Zone Distribution
            Text(
                text = "تقسيم المساحات والمناطق المقترحة:",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                result.zones.forEach { zone ->
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = zone.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "${zone.percentage}%", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { zone.percentage / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Text(text = zone.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Recommended Plants
            Text(
                text = "أفضل النباتات والأشجار الملائمة:",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                result.plants.forEach { plant ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🌱", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = plant.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(
                                            text = "ماء: ${plant.waterNeed}",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = plant.jordanHeritageNote,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Steps
            Text(
                text = "خطة العمل والتنفيذ المرحلية:",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                result.executiveSteps.forEachIndexed { idx, step ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "${idx + 1}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = step,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
