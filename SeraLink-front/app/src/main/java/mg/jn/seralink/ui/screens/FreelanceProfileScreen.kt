package mg.jn.seralink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class PortfolioItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val colorStart: Color,
    val colorEnd: Color
)

val samplePortfolio = listOf(
    PortfolioItem(1, "Plateforme E-commerce", "SaaS / Web App", Color(0xFF1B5E20), Color(0xFF388E3C)),
    PortfolioItem(2, "Application Santé", "Mobile / React Native", Color(0xFF546E7A), Color(0xFF90A4AE)),
    PortfolioItem(3, "Système de Design", "Figma / CSS", Color(0xFF37474F), Color(0xFF607D8B)),
    PortfolioItem(4, "API Gateway", "Backend / Security", Color(0xFF1A237E), Color(0xFF283593)),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreelanceProfileScreen(navController: NavController, userId: Int) {

    val skills = listOf("React.js", "Node.js", "Tailwind CSS", "PostgreSQL", "Next.js", "UI Design", "API REST")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = Color(0xFF333333))
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Share, contentDescription = "Partager", tint = Color(0xFF333333))
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Plus", tint = Color(0xFF333333))
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
                    selected = false,
                    onClick = { navController.navigate("profil") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profil", fontSize = 11.sp) }
                )
            }
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            // Header avec bannière + avatar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                // Bannière grise
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color(0xFFE0E0E0))
                )

                // Avatar centré chevauchant la bannière
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .align(Alignment.BottomCenter)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(82.dp)
                            .clip(CircleShape)
                            .background(GreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "FR",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Badge vérifié
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .align(Alignment.BottomCenter)
                        .offset(x = 28.dp, y = (-2).dp)
                        .clip(CircleShape)
                        .background(GreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nom + titre + localisation + note
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Faly Rakotoarisoa",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Développeur Fullstack React & Node.js",
                    fontSize = 14.sp,
                    color = Color(0xFF555555)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null,
                            tint = Color(0xFF888888), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Antananarivo, MG", fontSize = 12.sp, color = Color(0xFF888888))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null,
                            tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("4.9 (42 avis)", fontSize = 12.sp,
                            color = GreenPrimary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Stats 3 colonnes
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfilePublicStat("124", "MISSIONS")
                    Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color(0xFFE0E0E0)))
                    ProfilePublicStat("42", "AVIS")
                    Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color(0xFFE0E0E0)))
                    ProfilePublicStat("98%", "RÉUSSITE")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tarif + bouton Me contacter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("TARIF HORAIRE", fontSize = 11.sp, color = Color(0xFF888888),
                        fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("35.000 Ar", fontSize = 20.sp,
                            fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                        Text(" / heure", fontSize = 13.sp, color = Color(0xFF888888))
                    }
                }
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    modifier = Modifier.height(44.dp)
                ) {
                    Text("Me contacter", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Compétences
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Bolt, contentDescription = null,
                        tint = GreenPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Compétences", fontWeight = FontWeight.Bold,
                        fontSize = 17.sp, color = Color(0xFF1A1A1A))
                }
                Spacer(modifier = Modifier.height(14.dp))

                // Rangées de chips
                val rows = skills.chunked(3)
                rows.forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        row.forEach { skill ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFFF0F0F0))
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(skill, fontSize = 13.sp, color = Color(0xFF333333))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Portfolio
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Work, contentDescription = null,
                        tint = GreenPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Portfolio", fontWeight = FontWeight.Bold,
                        fontSize = 17.sp, color = Color(0xFF1A1A1A))
                }
                Spacer(modifier = Modifier.height(14.dp))

                // Grille 2 colonnes
                val rows2 = samplePortfolio.chunked(2)
                rows2.forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        row.forEach { item ->
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                elevation = CardDefaults.cardElevation(0.dp)
                            ) {
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(item.colorStart, item.colorEnd)
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Code,
                                            contentDescription = null,
                                            tint = Color.White.copy(alpha = 0.4f),
                                            modifier = Modifier.size(48.dp)
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .background(Color.White)
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            item.title,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = Color(0xFF1A1A1A)
                                        )
                                        Text(
                                            item.subtitle,
                                            fontSize = 11.sp,
                                            color = Color(0xFF888888)
                                        )
                                    }
                                }
                            }
                        }
                        // Si la rangée est impaire, ajouter un spacer
                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfilePublicStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = GreenPrimary)
        Spacer(modifier = Modifier.height(2.dp))
        Text(label, fontSize = 11.sp, color = Color(0xFF888888), fontWeight = FontWeight.Medium)
    }
}