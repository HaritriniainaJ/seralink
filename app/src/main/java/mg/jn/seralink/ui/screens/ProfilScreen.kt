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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.viewmodel.ProfilState
import mg.jn.seralink.viewmodel.ProfilViewModel
import mg.jn.seralink.viewmodel.ProfilViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilScreen(navController: NavController) {

    val context = LocalContext.current
    val dataStore = TokenDataStore(context)
    val viewModel: ProfilViewModel = viewModel(factory = ProfilViewModelFactory(dataStore))
    val profilState by viewModel.profilState.collectAsState()
    val userRole by dataStore.userRole.collectAsState(initial = null)
    val reviewAverage by viewModel.reviewAverage.collectAsState()
    val reviewCount by viewModel.reviewCount.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyProfile()
    }

    val user = (profilState as? ProfilState.Success)?.user

    LaunchedEffect(profilState) {
        val u = (profilState as? ProfilState.Success)?.user
        if (u != null) viewModel.loadUserReviews(u.id)
    }

    val displayName = user?.name ?: "Chargement..."
    val displayEmail = user?.email ?: ""
    val displayRole = user?.role ?: userRole ?: "freelance"
    val initials = displayName.split(" ")
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .take(2).joinToString("")
        .ifEmpty { "??" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mon Profil", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            SeraLinkBottomBar(navController = navController, selected = "profil", userRole = userRole)
        },
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            if (profilState is ProfilState.Loading) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = GreenPrimary)
                }
            }

            // Header profil
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier.size(90.dp).clip(CircleShape).background(GreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(initials, color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier.size(26.dp).clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = null,
                                tint = GreenPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(displayName, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF1A1A1A))
                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(20.dp))
                            .background(GreenLight).padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (displayRole == "client") "Client" else "Freelance",
                            color = GreenPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(displayEmail, fontSize = 13.sp, color = Color(0xFF888888))

                    if (reviewCount > 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null,
                                tint = Color(0xFFFFC107), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("$reviewAverage ($reviewCount avis)",
                                fontSize = 13.sp, color = Color(0xFF555555), fontWeight = FontWeight.Medium)
                        }

                        val (badgeText, badgeColor, badgeBg) = when {
                            reviewAverage >= 4.5 && reviewCount >= 5 ->
                                Triple("⭐ Top Freelance", Color(0xFFB8860B), Color(0xFFFFF8E1))
                            reviewAverage >= 4.0 ->
                                Triple("✓ Recommandé", Color(0xFF2E7D32), Color(0xFFE8F5E9))
                            else -> Triple("", Color.Transparent, Color.Transparent)
                        }
                        if (badgeText.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                    .background(badgeBg)
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(badgeText, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = badgeColor)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Menu options
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    ProfilMenuItem(
                        icon = Icons.Default.Work,
                        label = "Mes contrats",
                        onClick = { navController.navigate(Routes.MY_CONTRACTS) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
                    if (displayRole == "client") {
                        ProfilMenuItem(
                            icon = Icons.Default.Dashboard,
                            label = "Tableau de bord",
                            onClick = { navController.navigate("dashboard_client") }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
                    }
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

            // Déconnexion
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                ProfilMenuItem(
                    icon = Icons.Default.Logout,
                    label = "Se déconnecter",
                    onClick = {
                        navController.navigate(Routes.LOGIN) { popUpTo(0) }
                    },
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
    Surface(onClick = onClick, color = Color.Transparent) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp))
                    .background(if (tint == Color(0xFFE53935)) Color(0xFFFFEBEE) else Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(label, fontWeight = FontWeight.Medium, fontSize = 14.sp,
                color = labelColor, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null,
                tint = Color(0xFFCCCCCC), modifier = Modifier.size(20.dp))
        }
    }
}