package `fun`.agentpump.arena.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import `fun`.agentpump.arena.ui.screens.*
import `fun`.agentpump.arena.ui.theme.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Arena : Screen("arena", "Arena", Icons.Default.Stadium)
    object Token : Screen("token", "Token", Icons.Default.TrendingUp)
    object Agents : Screen("agents", "Agents", Icons.Default.SmartToy)
    object Alerts : Screen("alerts", "Alerts", Icons.Default.Notifications)
}

val bottomNavItems = listOf(
    Screen.Arena,
    Screen.Token,
    Screen.Agents,
    Screen.Alerts
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentPumpNavigation() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = DarkCard,
                contentColor = TextSecondary
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                bottomNavItems.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                screen.icon, 
                                contentDescription = screen.title,
                                tint = if (selected) NeonGreen else TextMuted
                            ) 
                        },
                        label = { 
                            Text(
                                screen.title,
                                color = if (selected) NeonGreen else TextMuted
                            ) 
                        },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = DarkSurface
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Arena.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Arena.route) { ArenaScreen() }
            composable(Screen.Token.route) { TokenScreen() }
            composable(Screen.Agents.route) { AgentsScreen() }
            composable(Screen.Alerts.route) { AlertsScreen() }
        }
    }
}
