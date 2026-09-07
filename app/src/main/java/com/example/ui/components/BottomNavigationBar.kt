package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Architecture
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.NavTab

@Composable
fun AppBottomNavigationBar(
    currentTab: NavTab,
    onTabSelected: (NavTab) -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            NavTab.entries.forEach { tab ->
                val isSelected = currentTab == tab
                val icon = when (tab) {
                    NavTab.HOME -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
                    NavTab.ARTICLES -> if (isSelected) Icons.Filled.MenuBook else Icons.Outlined.MenuBook
                    NavTab.PLANNER -> if (isSelected) Icons.Filled.Architecture else Icons.Outlined.Architecture
                    NavTab.SCHEDULE -> if (isSelected) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth
                    NavTab.PROFILE -> if (isSelected) Icons.Filled.Person else Icons.Outlined.Person
                }

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    icon = { Icon(imageVector = icon, contentDescription = tab.title) },
                    label = {
                        Text(
                            text = tab.title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        }
    }
}
