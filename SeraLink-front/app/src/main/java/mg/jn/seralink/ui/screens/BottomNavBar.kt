package mg.jn.seralink.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SeraLinkBottomBar(
    navController: NavController,
    selected: String,
    userRole: String?
) {
    NavigationBar(containerColor = androidx.compose.ui.graphics.Color.White) {
        if (userRole == "client") {
            // Client : Dashboard, Messages, Contrats, Profil
            NavigationBarItem(
                selected = selected == "dashboard",
                onClick = { navController.navigate("dashboard_client") },
                icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                label = { Text("Dashboard", fontSize = 11.sp) }
            )
            NavigationBarItem(
                selected = selected == "messages",
                onClick = { navController.navigate("messages") },
                icon = { Icon(Icons.Default.Message, contentDescription = null) },
                label = { Text("Messages", fontSize = 11.sp) }
            )
            NavigationBarItem(
                selected = selected == "contrats",
                onClick = { navController.navigate(Routes.MY_CONTRACTS) },
                icon = { Icon(Icons.Default.Description, contentDescription = null) },
                label = { Text("Contrats", fontSize = 11.sp) }
            )
            NavigationBarItem(
                selected = selected == "profil",
                onClick = { navController.navigate("profil") },
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                label = { Text("Profil", fontSize = 11.sp) }
            )
        } else {
            // Freelance : Accueil, Missions, Messages, Contrats, Profil
            NavigationBarItem(
                selected = selected == "home",
                onClick = { navController.navigate(Routes.HOME) },
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text("Accueil", fontSize = 11.sp) }
            )
            NavigationBarItem(
                selected = selected == "missions",
                onClick = { navController.navigate("missions") },
                icon = { Icon(Icons.Default.Work, contentDescription = null) },
                label = { Text("Missions", fontSize = 11.sp) }
            )
            NavigationBarItem(
                selected = selected == "messages",
                onClick = { navController.navigate("messages") },
                icon = { Icon(Icons.Default.Message, contentDescription = null) },
                label = { Text("Messages", fontSize = 11.sp) }
            )
            NavigationBarItem(
                selected = selected == "contrats",
                onClick = { navController.navigate(Routes.MY_CONTRACTS) },
                icon = { Icon(Icons.Default.Description, contentDescription = null) },
                label = { Text("Contrats", fontSize = 11.sp) }
            )
            NavigationBarItem(
                selected = selected == "profil",
                onClick = { navController.navigate("profil") },
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                label = { Text("Profil", fontSize = 11.sp) }
            )
        }
    }
}