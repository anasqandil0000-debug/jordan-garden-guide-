package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.datasource.SampleData
import com.example.data.local.entity.SavedGardenEntity
import com.example.data.model.Article
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ProfileScreen(viewModel: MainViewModel) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val savedGardens by viewModel.savedGardens.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteArticleIds.collectAsStateWithLifecycle()

    val favoriteArticles = SampleData.articles.filter { favoriteIds.contains(it.id) }

    var selectedTab by remember { mutableIntStateOf(0) }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    // Edit profile dialog inputs
    var editName by remember { mutableStateOf(userProfile.name) }
    var editEmail by remember { mutableStateOf(userProfile.email) }
    var editCity by remember { mutableStateOf(userProfile.city) }
    var editGardenType by remember { mutableStateOf(userProfile.gardenType) }
    var editNotifs by remember { mutableStateOf(userProfile.notificationsEnabled) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Header Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🇯🇴",
                                    fontSize = 32.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = userProfile.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(
                                            text = "نشمي موثق",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = userProfile.email,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "📍 ${userProfile.city} • ${userProfile.gardenType}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            IconButton(
                                onClick = {
                                    editName = userProfile.name
                                    editEmail = userProfile.email
                                    editCity = userProfile.city
                                    editGardenType = userProfile.gardenType
                                    editNotifs = userProfile.notificationsEnabled
                                    showEditProfileDialog = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "تعديل الملف الشخصي",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ProfileStatBox(
                                title = "الحدائق المحفوظة",
                                value = "${savedGardens.size}",
                                modifier = Modifier.weight(1f)
                            )
                            ProfileStatBox(
                                title = "المقالات المفضلة",
                                value = "${favoriteArticles.size}",
                                modifier = Modifier.weight(1f)
                            )
                            ProfileStatBox(
                                title = "تقييم العناية",
                                value = "${userProfile.gardenCareScore}%",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Tab Navigation
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("حدائقي (${savedGardens.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("المفضلة (${favoriteArticles.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("الأوسمة 🏆", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> {
                    // Saved Gardens
                    if (savedGardens.isEmpty()) {
                        item {
                            EmptyProfileSection(
                                message = "لم تقم بحفظ أي مخطط حديقة بعد.",
                                actionText = "انتقل إلى مخطط الحديقة لإنشاء أول مخطط!",
                                icon = Icons.Default.Landscape
                            )
                        }
                    } else {
                        items(savedGardens, key = { it.id }) { garden ->
                            SavedGardenItemCard(
                                garden = garden,
                                onDelete = { viewModel.deleteGarden(garden.id) }
                            )
                        }
                    }
                }

                1 -> {
                    // Favorite Articles
                    if (favoriteArticles.isEmpty()) {
                        item {
                            EmptyProfileSection(
                                message = "قائمة مقالاتك المفضلة فارغة حالياً.",
                                actionText = "تصفح دليل المقالات وأضف مقالاتك المهمة بضغطة زر.",
                                icon = Icons.Default.Bookmark
                            )
                        }
                    } else {
                        items(favoriteArticles, key = { it.id }) { article ->
                            SavedArticleCard(
                                article = article,
                                onSelect = { viewModel.selectArticle(article) },
                                onRemove = { viewModel.toggleFavorite(article.id) }
                            )
                        }
                    }
                }

                2 -> {
                    // Jordanian Badges & Achievements
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            BadgeItemCard(
                                title = "حارس مياه الأردن 💧",
                                description = "قمت بإنشاء شبكة ري بالتنقيط وترشيد استهلاك بئر الجمع.",
                                isUnlocked = true
                            )
                            BadgeItemCard(
                                title = "خبير زيتون عجلون 🫒",
                                description = "التزمت بجدول تقليم الزيتون والتسميد الشتوي ومكافحة الآفات.",
                                isUnlocked = true
                            )
                            BadgeItemCard(
                                title = "مهندس الحجر المعاني 🏛️",
                                description = "طبقت طرق تنظيف حجر الواجهات وإزالة الكلس بدون مواد ضارة.",
                                isUnlocked = true
                            )
                            BadgeItemCard(
                                title = "عاشق الياسمين والورد السلطاني 🌸",
                                description = "رعاية شجيرات الياسمين البلدي والعراقي في التراس والمدخل.",
                                isUnlocked = false
                            )
                        }
                    }
                }
            }
        }

        // Edit Profile Dialog
        if (showEditProfileDialog) {
            AlertDialog(
                onDismissRequest = { showEditProfileDialog = false },
                title = { Text("تعديل الملف الشخصي", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("الاسم الكامل") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = editEmail,
                            onValueChange = { editEmail = it },
                            label = { Text("البريد الإلكتروني") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = editCity,
                            onValueChange = { editCity = it },
                            label = { Text("المدينة / المحافظة") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = editGardenType,
                            onValueChange = { editGardenType = it },
                            label = { Text("طبيعة الحديقة أو السكن") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("تفعيل التنبيهات الدورية:", fontSize = 13.sp)
                            Switch(
                                checked = editNotifs,
                                onCheckedChange = { editNotifs = it }
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.updateProfile(
                                name = editName,
                                email = editEmail,
                                city = editCity,
                                gardenType = editGardenType,
                                notifs = editNotifs
                            )
                            showEditProfileDialog = false
                        }
                    ) {
                        Text("حفظ التعديلات")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditProfileDialog = false }) {
                        Text("إلغاء")
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileStatBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun SavedGardenItemCard(
    garden: SavedGardenEntity,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
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
                    text = garden.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "حذف المخطط",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${garden.lengthMeters.toInt()}م × ${garden.widthMeters.toInt()}م (${garden.areaMeters.toInt()} م²)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "📍 ${garden.city}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "🌱 النباتات المقترحة: ${garden.recommendedPlants}",
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "💧 الري: ${garden.analysisSummary}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun SavedArticleCard(
    article: Article,
    onSelect: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val img = article.imageRes ?: R.drawable.img_jordan_garden
            Image(
                painter = painterResource(id = img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = article.category.title,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "إزالة",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun BadgeItemCard(
    title: String,
    description: String,
    isUnlocked: Boolean
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MilitaryTech,
                    contentDescription = null,
                    tint = if (isUnlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            if (isUnlocked) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "مكتمل ✓",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "قيد الإنجاز",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyProfileSection(
    message: String,
    actionText: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = actionText,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
