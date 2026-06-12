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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.model.Review
import mg.jn.seralink.viewmodel.ProfilViewModel
import mg.jn.seralink.viewmodel.ProfilViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreelanceProfileScreen(navController: NavController, userId: Int) {

    val context = LocalContext.current
    val dataStore = TokenDataStore(context)
    val viewModel: ProfilViewModel = viewModel(factory = ProfilViewModelFactory(dataStore))
    val publicProfile by viewModel.publicProfile.collectAsState()
    val isLoading by viewModel.isLoadingPublic.collectAsState()
    val userRole by dataStore.userRole.collectAsState(initial = null)
    val reviews by viewModel.userReviews.collectAsState()
    val reviewAverage by viewModel.reviewAverage.collectAsState()
    val reviewCount by viewModel.reviewCount.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadPublicProfile(userId)
        viewModel.loadUserReviews(userId)
    }

    val displayName = publicProfile?.name ?: "..."
    val initials = displayName.split(" ")
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .take(2).joinToString("")
        .ifEmpty { "?" }

    val skills = publicProfile?.skills
        ?.split(",")
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() }
        ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = Color(0xFF333333))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            SeraLinkBottomBar(navController = navController, selected = "", userRole = userRole)
        },
        containerColor = Color.White
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header bannière + avatar
                Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFE8F5E9)))
                    Box(
                        modifier = Modifier.size(90.dp).align(Alignment.BottomCenter)
                            .clip(CircleShape).background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier.size(82.dp).clip(CircleShape).background(GreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(initials, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Nom + badge + bio + localisation
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(displayName, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF1A1A1A))

                    // Badge réputation (calculé localement, pas d'API supplémentaire)
                    if (reviewCount > 0) {
                        val (badgeText, badgeColor, badgeBg) = when {
                            reviewAverage >= 4.5 && reviewCount >= 5 ->
                                Triple("⭐ Top Freelance", Color(0xFFB8860B), Color(0xFFFFF8E1))
                            reviewAverage >= 4.0 ->
                                Triple("✓ Recommandé", Color(0xFF2E7D32), Color(0xFFE8F5E9))
                            else -> Triple("", Color.Transparent, Color.Transparent)
                        }
                        if (badgeText.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                    .background(badgeBg)
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(badgeText, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = badgeColor)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    val bio = publicProfile?.bio
                    if (!bio.isNullOrEmpty()) {
                        Text(bio, fontSize = 13.sp, color = Color(0xFF555555),
                            modifier = Modifier.padding(horizontal = 24.dp),
                            textAlign = TextAlign.Center, maxLines = 3)
                        Spacer(modifier = Modifier.height(6.dp))
                    } else {
                        Text("Freelance SeraLink", fontSize = 13.sp, color = Color(0xFF888888))
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null,
                            tint = Color(0xFF888888), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Madagascar", fontSize = 12.sp, color = Color(0xFF888888))
                    }

                    // Note moyenne
                    if (reviewCount > 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) { index ->
                                Icon(
                                    if (index < reviewAverage.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("$reviewAverage ($reviewCount avis)",
                                fontSize = 13.sp, color = Color(0xFF555555), fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bouton contacter
                Button(
                    onClick = { navController.navigate("messages") },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(48.dp)
                ) {
                    Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Contacter ce freelance", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Compétences
                if (skills.isNotEmpty()) {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bolt, contentDescription = null,
                                tint = GreenPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Compétences", fontWeight = FontWeight.Bold,
                                fontSize = 17.sp, color = Color(0xFF1A1A1A))
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        skills.chunked(3).forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(bottom = 8.dp)) {
                                row.forEach { skill ->
                                    Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                        .background(Color(0xFFF0F0F0))
                                        .padding(horizontal = 14.dp, vertical = 8.dp)) {
                                        Text(skill, fontSize = 13.sp, color = Color(0xFF333333))
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                }

                // Section Avis
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null,
                            tint = GreenPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Avis clients", fontWeight = FontWeight.Bold,
                            fontSize = 17.sp, color = Color(0xFF1A1A1A))
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    if (reviews.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center) {
                            Text("Aucun avis pour le moment", fontSize = 13.sp, color = Color(0xFF999999))
                        }
                    } else {
                        reviews.forEach { review ->
                            ReviewCard(review = review)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(GreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = (review.reviewer?.name ?: "?")
                        .split(" ").mapNotNull { it.firstOrNull()?.uppercaseChar() }
                        .take(2).joinToString("").ifEmpty { "?" }
                    Text(initials, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(review.reviewer?.name ?: "Anonyme",
                        fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF1A1A1A))
                    Row {
                        repeat(5) { index ->
                            Icon(
                                if (index < review.rating) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
            if (!review.comment.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(review.comment, fontSize = 13.sp, color = Color(0xFF555555))
            }
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