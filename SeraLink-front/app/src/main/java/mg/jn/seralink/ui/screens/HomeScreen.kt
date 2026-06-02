package mg.jn.seralink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController



val sampleJobs = listOf(
    mg.jn.seralink.model.JobListing(1, 1, "Développeur Mobile Flutter",
        "Développement d'une application mobile cross-platform",
        "Dev", 1200000, 1200000, "fixed", "open", "Dans 7 jours",
        mg.jn.seralink.model.UserResponse(1, "TechSolutions Madagascar", "tech@seralink.mg", "client", null)),
    mg.jn.seralink.model.JobListing(2, 2, "UI/UX Designer",
        "Refonte complète de l'interface utilisateur",
        "Design", 850000, 850000, "fixed", "open", "Dans 5 jours",
        mg.jn.seralink.model.UserResponse(2, "Creative MG", "creative@seralink.mg", "client", null)),
    mg.jn.seralink.model.JobListing(3, 3, "Rédaction de fiches produits SEO",
        "Rédaction de 50 fiches produits optimisées SEO",
        "Rédaction", 200000, 200000, "fixed", "open", "Il y a 2h",
        mg.jn.seralink.model.UserResponse(3, "E-commerce MG", "ecom@seralink.mg", "client", null)),
    mg.jn.seralink.model.JobListing(4, 4, "Saisie de données comptables",
        "Saisie et vérification de données comptables",
        "Comptabilité", 450000, 450000, "fixed", "open", "Il y a 5h",
        mg.jn.seralink.model.UserResponse(4, "Cabinet Alpha", "alpha@seralink.mg", "client", null)),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val userName = "Tahina"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "SeraLink",
                            fontWeight = FontWeight.ExtraBold,
                            color = GreenPrimary,
                            fontSize = 20.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color(0xFF333333)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFBDBDBD)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
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
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = "Bonjour $userName, trouvez\nvotre prochaine mission 👋",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        lineHeight = 30.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Plus de 150 nouvelles missions correspondent à votre profil aujourd'hui.",
                        fontSize = 13.sp,
                        color = Color(0xFF888888),
                        lineHeight = 18.sp
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            "Rechercher une mission, une compétence...",
                            color = Color(0xFFAAAAAA),
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFAAAAAA))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = GreenPrimary,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Missions recommandées",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        "Voir plus",
                        color = GreenPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { navController.navigate("missions") }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(sampleJobs.take(3)) { job ->
                        RecommendedJobCard(job = job, onClick = {
                            navController.navigate("job_detail/${job.id}")
                        })
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Catégories populaires",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CategoryCard(icon = Icons.Default.Brush, label = "Design", modifier = Modifier.weight(1f))
                        CategoryCard(icon = Icons.Default.Code, label = "Dev", modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CategoryCard(icon = Icons.Default.TrendingUp, label = "Marketing", modifier = Modifier.weight(1f))
                        CategoryCard(icon = Icons.Default.Edit, label = "Rédaction", modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Missions récentes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        "Voir tout",
                        color = GreenPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { navController.navigate("missions") }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(sampleJobs) { job ->
                RecentJobCard(
                    job = job,
                    onClick = { navController.navigate("job_detail/${job.id}") }
                )
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }
}

@Composable
fun RecommendedJobCard(job: mg.jn.seralink.model.JobListing, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(200.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(GreenLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Work, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(22.dp))
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF4CAF50).copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("Nouveau", color = GreenPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = job.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1A1A1A), maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = job.client?.name ?: "", fontSize = 12.sp, color = Color(0xFF888888))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${job.budgetMin / 1000}k Ar", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = GreenPrimary)
        }
    }
}

@Composable
fun CategoryCard(icon: ImageVector, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(GreenLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF1A1A1A))
        }
    }
}

@Composable
fun RecentJobCard(job: mg.jn.seralink.model.JobListing, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Work, contentDescription = null, tint = Color(0xFF888888), modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = job.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF1A1A1A), maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = "${job.client?.name ?: job.category} • Il y a 2h", fontSize = 12.sp, color = Color(0xFF888888))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "${job.budgetMin / 1000}k Ar", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GreenPrimary)
        }
    }
}