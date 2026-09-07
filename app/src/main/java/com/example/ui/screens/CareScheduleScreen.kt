package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.CareTaskEntity
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CareScheduleScreen(viewModel: MainViewModel) {
    val tasks by viewModel.allTasks.collectAsStateWithLifecycle()
    var selectedCategoryFilter by remember { mutableStateOf("الكل") }
    var showAddTaskDialog by remember { mutableStateOf(false) }

    val filteredTasks = tasks.filter {
        when (selectedCategoryFilter) {
            "حديقة" -> it.category == "حديقة"
            "تنظيف" -> it.category == "تنظيف"
            else -> true
        }
    }

    val completedCount = tasks.count { it.isCompleted }
    val totalCount = tasks.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    // New task dialog state
    var newTitle by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("حديقة") }
    var newFrequency by remember { mutableStateOf("أسبوعي") }
    var newDueDate by remember { mutableStateOf("كل سبت") }
    var newNotes by remember { mutableStateOf("") }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddTaskDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "إضافة مهمة")
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Progress Card
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "متابعة جداول العناية بالحدائق والدار",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "أنجزت $completedCount من أصل $totalCount مهام دورية",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${(progress * 100).toInt()}%",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }

                // Periodic Notifications Demo Action
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "نظام الإشعارات والتنبيهات الدورية",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = "يرسل تنبيهات بمواعيد الري وتقلبات الطقس في الأردن",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    viewModel.sendTestNotification(
                                        title = "تذكير دوري: ري نباتات الشرفة والتراس",
                                        message = "لا تنس تفقد رطوبة التربة في الأصص الفخارية وسقاية الياسمين والميرمية قبل اشتداد شمس الظهيرة."
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("إرسال تذكير", fontSize = 11.sp)
                            }
                        }
                    }
                }

                // Filter Tabs
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("الكل", "حديقة", "تنظيف").forEach { cat ->
                            val isSelected = selectedCategoryFilter == cat
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedCategoryFilter = cat }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cat,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                // Task Items
                if (filteredTasks.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "لا توجد مهام في هذا التصنيف حالياً. اضغط + لإضافة مهمة.",
                                color = MaterialTheme.colorScheme.outline,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    items(filteredTasks, key = { it.id }) { task ->
                        TaskItemCard(
                            task = task,
                            onToggle = { isChecked -> viewModel.toggleTask(task.id, isChecked) },
                            onDelete = { viewModel.deleteTask(task.id) }
                        )
                    }
                }
            }

            // Add Task Dialog
            if (showAddTaskDialog) {
                AlertDialog(
                    onDismissRequest = { showAddTaskDialog = false },
                    title = { Text("إضافة مهمة عناية جديدة", fontWeight = FontWeight.Bold) },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = newTitle,
                                onValueChange = { newTitle = it },
                                label = { Text("اسم المهمة (مثال: تقليم الميرمية)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Category Radio
                            Text("التصنيف:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { newCategory = "حديقة" }
                                ) {
                                    RadioButton(
                                        selected = newCategory == "حديقة",
                                        onClick = { newCategory = "حديقة" }
                                    )
                                    Text("حديقة", fontSize = 13.sp)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { newCategory = "تنظيف" }
                                ) {
                                    RadioButton(
                                        selected = newCategory == "تنظيف",
                                        onClick = { newCategory = "تنظيف" }
                                    )
                                    Text("تنظيف", fontSize = 13.sp)
                                }
                            }

                            // Frequency
                            OutlinedTextField(
                                value = newFrequency,
                                onValueChange = { newFrequency = it },
                                label = { Text("التكرار (أسبوعي، شهري، موسمي...)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Due Date
                            OutlinedTextField(
                                value = newDueDate,
                                onValueChange = { newDueDate = it },
                                label = { Text("الموعد (مثال: كل جمعة صباحاً)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Notes
                            OutlinedTextField(
                                value = newNotes,
                                onValueChange = { newNotes = it },
                                label = { Text("ملاحظات إضافية (اختياري)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (newTitle.isNotBlank()) {
                                    viewModel.addTask(
                                        title = newTitle,
                                        category = newCategory,
                                        frequency = newFrequency,
                                        dueDate = newDueDate,
                                        notes = newNotes
                                    )
                                    showAddTaskDialog = false
                                    newTitle = ""
                                    newNotes = ""
                                }
                            }
                        ) {
                            Text("إضافة المهمة")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddTaskDialog = false }) {
                            Text("إلغاء")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItemCard(
    task: CareTaskEntity,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (task.category == "حديقة") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = task.category,
                            fontSize = 11.sp,
                            color = if (task.category == "حديقة") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = "📅 ${task.dueDate} • ${task.frequency}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                if (task.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.notes,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "حذف المهمة",
                    tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
