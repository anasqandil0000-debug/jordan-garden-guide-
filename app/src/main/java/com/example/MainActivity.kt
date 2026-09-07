package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppBottomNavigationBar
import com.example.ui.components.AppTopBar
import com.example.ui.components.ArticleDetailModal
import com.example.ui.components.NotificationsBottomSheet
import com.example.ui.screens.ArticlesScreen
import com.example.ui.screens.CareScheduleScreen
import com.example.ui.screens.GardenPlannerScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.NavTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer()
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: MainViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedArticle by viewModel.selectedArticle.collectAsStateWithLifecycle()
    val unreadCount by viewModel.unreadCount.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val favorites by viewModel.favoriteArticleIds.collectAsStateWithLifecycle()

    var showNotificationsSheet by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                AppTopBar(
                    unreadCount = unreadCount,
                    onNotificationsClick = { showNotificationsSheet = true }
                )
            },
            bottomBar = {
                AppBottomNavigationBar(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Crossfade(targetState = currentTab, label = "TabTransition") { tab ->
                    when (tab) {
                        NavTab.HOME -> HomeScreen(
                            viewModel = viewModel,
                            onNavigateTab = { viewModel.selectTab(it) }
                        )
                        NavTab.ARTICLES -> ArticlesScreen(viewModel = viewModel)
                        NavTab.PLANNER -> GardenPlannerScreen(viewModel = viewModel)
                        NavTab.SCHEDULE -> CareScheduleScreen(viewModel = viewModel)
                        NavTab.PROFILE -> ProfileScreen(viewModel = viewModel)
                    }
                }
            }

            // Article Detail Modal
            if (selectedArticle != null) {
                val article = selectedArticle!!
                ArticleDetailModal(
                    article = article,
                    isFavorite = favorites.contains(article.id),
                    onDismiss = { viewModel.selectArticle(null) },
                    onToggleFavorite = { viewModel.toggleFavorite(article.id) }
                )
            }

            // Notifications Bottom Sheet
            if (showNotificationsSheet) {
                NotificationsBottomSheet(
                    notifications = notifications,
                    onDismiss = { showNotificationsSheet = false },
                    onMarkAllRead = { viewModel.markAllNotificationsRead() },
                    onMarkRead = { viewModel.markNotificationRead(it) }
                )
            }
        }
    }
}
