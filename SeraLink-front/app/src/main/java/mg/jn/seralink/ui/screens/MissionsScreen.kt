package mg.jn.seralink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mg.jn.seralink.model.JobListing

val filters = listOf("Récent", "Budget", "Urgent", "Long terme")

val sampleMissions = listOf(
    JobListing(1, 1, "Création d'un site e-commerce pour artisanat local",
        "Nous recherchons un développeur pour concevoir une plateforme de vente en ligne mettant en avant...",
        "Développement Web", 150000, 150000, "fixed", "open", "Dans 7 jours",
        mg.jn.seralink.model.UserResponse(1, "Andry R.", "andry@seralink.mg", "client", null)),
    JobListing(2, 2, "Logo et charte graphique pour une startup Agritech",
        "Besoin d'une identité visuelle forte qui combine agriculture et technologie pour notre nouvelle...",
        "Graphisme & Design", 80000, 80000, "fixed", "open", "Dans 3 jours",
        mg.jn.seralink.model.UserResponse(2, "Mialy T.", "mialy@seralink.mg", "client", null)),
    JobListing(3, 3, "Traduction technique Français - Malagasy",
        "Besoin d'une traduction urgente d'un manuel d'utilisation de 50 pages pour une ONG...",
        "Rédaction & Traduction", 250000, 250000, "fixed", "open", "Dans 24h",
        mg.jn.seralink.model.UserResponse(3, "Jean P.", "jean@seralink.mg", "client", null)),
    JobListing(4, 4, "Campagne Publicitaire Facebook & Instagram",
        "Expert en Ads recherché pour booster la visibilité d'une agence de voyage locale avant la haute...",
        "Marketing Digital", 120000, 120000, "fixed", "open", "Dans 14 jours",
        mg.jn.seralink.model.UserResponse(4, "Feno S.", "feno@seralink.mg", "client", null)),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionsScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("Récent") }

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
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Person, contentDescription = null)
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
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Work, contentDescription = null) },
                    label = { Text("Missions", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
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
                    onClick = {},
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
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Missions\ndisponibles",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A),
                            lineHeight = 30.sp
                        )
                        OutlinedButton(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp, Color(0xFFE0E0E0)
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF555555)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Filtres",
                                fontSize = 13.sp,
                                color = Color(0xFF555555)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Filtres chips
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filters) { filter ->
                            val isSelected = filter == selectedFilter
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        if (isSelected) GreenPrimary else Color.White
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) GreenPrimary else Color(0xFFE0E0E0),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { selectedFilter = filter }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = filter,
                                    color = if (isSelected) Color.White else Color(0xFF555555),
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            // Liste missions
            items(sampleMissions) { job ->
                Spacer(modifier = Modifier.height(12.dp))
                MissionCard(
                    job = job,
                    onClick = { navController.navigate("job_detail/${job.id}") }
                )
            }
        }
    }
}

@Composable
fun MissionCard(job: JobListing, onClick: () -> Unit) {
    val isUrgent = job.deadline?.contains("24h") == true

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Catégorie + Urgent + Favori
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(GreenLight)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = job.category,
                            color = GreenPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (isUrgent) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFFFEBEE))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Urgent",
                                color = Color(0xFFE53935),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = Color(0xFFBBBBBB),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Titre
            Text(
                text = job.title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF1A1A1A),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = job.description,
                fontSize = 13.sp,
                color = Color(0xFF888888),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tags (catégorie comme tag)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(job.category.split(" ").first(), job.budgetType).forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFF5F5F5))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 11.sp,
                            color = Color(0xFF555555)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            // Client + Budget + Délai
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFBDBDBD)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = job.client?.name?.take(2)?.uppercase() ?: "??",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = job.client?.name ?: "",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = " 4.8",
                            fontSize = 11.sp,
                            color = Color(0xFF888888)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${job.budgetMin / 1000} 000 Ar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GreenPrimary
                    )
                    Text(
                        text = job.deadline ?: "",
                        fontSize = 11.sp,
                        color = if (isUrgent) Color(0xFFE53935) else Color(0xFF888888)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bouton Voir la mission
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = GreenPrimary
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, GreenPrimary)
            ) {
                Text(
                    "Voir la mission",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}