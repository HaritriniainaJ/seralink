package mg.jn.seralink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mon Profil", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Edit, contentDescription = "Modifier", tint = GreenPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Accueil", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("missions") },
                    icon = { Icon(Icons.Default.Work, contentDescription = null) },
                    label = { Text("Missions", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("messages") },
                    icon = { Icon(Icons.Default.Message, contentDescription = null) },
                    label = { Text("Messages", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.MY_CONTRACTS) },
                    icon = { Icon(Icons.Default.Description, contentDescription = null) },
                    label = { Text("Contrats", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profil", fontSize = 11.sp) }
                )
            }
        },
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            // Header profil
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(GreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "RJ",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Rakoto Jean",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1A1A1A)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(GreenLight)
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Text("Freelance", color = GreenPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "rakoto@seralink.mg",
                        fontSize = 13.sp,
                        color = Color(0xFF888888)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfilStatItem(value = "12", label = "Propositions")
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(Color(0xFFF0F0F0))
                        )
                        ProfilStatItem(value = "5", label = "Contrats")
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(Color(0xFFF0F0F0))
                        )
                        ProfilStatItem(value = "4.8 ★", label = "Note")
                    }
                }
            }

            // Compétences
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Compétences",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    val skills = listOf("Kotlin", "Android", "Jetpack Compose", "Laravel", "PHP", "MySQL")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        skills.take(3).forEach { skill ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GreenLight)
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Text(skill, fontSize = 12.sp, color = GreenPrimary, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        skills.drop(3).forEach { skill ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GreenLight)
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Text(skill, fontSize = 12.sp, color = GreenPrimary, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Menu options
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    ProfilMenuItem(
                        icon = Icons.Default.Person,
                        label = "Modifier mon profil",
                        onClick = {}
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
                    ProfilMenuItem(
                        icon = Icons.Default.Dashboard,
                        label = "Dashboard Client",
                        onClick = { navController.navigate("dashboard_client") }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
                    ProfilMenuItem(
                        icon = Icons.Default.Settings,
                        label = "Paramètres",
                        onClick = {}
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
                    ProfilMenuItem(
                        icon = Icons.Default.Work,
                        label = "Mes contrats",
                        onClick = { navController.navigate(Routes.MY_CONTRACTS) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
                    ProfilMenuItem(
                        icon = Icons.Default.Star,
                        label = "Mes évaluations",
                        onClick = {}
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
                    ProfilMenuItem(
                        icon = Icons.Default.Settings,
                        label = "Paramètres",
                        onClick = {}
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton déconnexion
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                ProfilMenuItem(
                    icon = Icons.Default.Logout,
                    label = "Se déconnecter",
                    onClick = { navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                    tint = Color(0xFFE53935),
                    labelColor = Color(0xFFE53935)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfilStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = GreenPrimary)
        Spacer(modifier = Modifier.height(2.dp))
        Text(label, fontSize = 11.sp, color = Color(0xFF888888))
    }
}

@Composable
fun ProfilMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color = Color(0xFF555555),
    labelColor: Color = Color(0xFF1A1A1A)
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (tint == Color(0xFFE53935)) Color(0xFFFFEBEE) else Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                label,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = labelColor,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFCCCCCC),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}