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
import mg.jn.seralink.viewmodel.JobViewModel
import mg.jn.seralink.viewmodel.JobState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val context = androidx.compose.ui.platform.LocalContext.current
    val dataStore = mg.jn.seralink.data.TokenDataStore(context)
    val userRole by dataStore.userRole.collectAsState(initial = null)
    val userName by dataStore.userName.collectAsState(initial = null)

    LaunchedEffect(userRole) {
        if (userRole == "client") {
            navController.navigate("dashboard_client") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    if (userRole == "client") return
    if (userRole == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GreenPrimary)
        }
        return
    }

    val viewModel: JobViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val jobState by viewModel.jobState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadJobs()
    }

    val displayJobs = when (jobState) {
        is JobState.Success -> (jobState as JobState.Success).jobs
        else -> emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                                .background(GreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null,
                                tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SeraLink", fontWeight = FontWeight.ExtraBold,
                            color = GreenPrimary, fontSize = 20.sp)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = null,
                            tint = Color(0xFF333333))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            SeraLinkBottomBar(navController = navController, selected = "home", userRole = "freelance")
        },
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            // Header
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().background(Color.White)
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = "Bonjour ${userName ?: ""} 👋",
                        fontSize = 22.sp, fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Trouvez votre prochaine mission sur SeraLink.",
                        fontSize = 13.sp, color = Color(0xFF888888)
                    )
                }
            }

            // Barre de recherche
            item {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        if (it.length >= 3 || it.isEmpty()) {
                            viewModel.loadJobs(search = it.ifBlank { null })
                        }
                    },
                    placeholder = {
                        Text("Rechercher une mission...",
                            color = Color(0xFFAAAAAA), fontSize = 14.sp)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null,
                            tint = Color(0xFFAAAAAA))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                                viewModel.loadJobs()
                            }) {
                                Icon(Icons.Default.Close, contentDescription = null,
                                    tint = Color(0xFFAAAAAA))
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
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

            // Loading
            if (jobState is JobState.Loading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }
            }

            // Erreur
            if (jobState is JobState.Error) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                    ) {
                        Row(modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WifiOff, contentDescription = null,
                                tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Impossible de charger les missions",
                                color = Color(0xFFD32F2F), fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Missions recommandées
            if (displayJobs.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text("Missions recommandées", fontWeight = FontWeight.Bold,
                            fontSize = 16.sp, color = Color(0xFF1A1A1A))
                        Text("Voir plus", color = GreenPrimary, fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { navController.navigate("missions") })
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(displayJobs.take(3)) { job ->
                            RecommendedJobCard(job = job, onClick = {
                                navController.navigate("job_detail/${job.id}")
                            })
                        }
                    }
                }
            }

            // Catégories
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Catégories populaires", fontWeight = FontWeight.Bold,
                    fontSize = 16.sp, color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CategoryCard(icon = Icons.Default.Brush, label = "Design",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.loadJobs(category = "Design") })
                        CategoryCard(icon = Icons.Default.Code, label = "Dev",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.loadJobs(category = "Dev") })
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CategoryCard(icon = Icons.Default.TrendingUp, label = "Marketing",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.loadJobs(category = "Marketing") })
                        CategoryCard(icon = Icons.Default.Edit, label = "Rédaction",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.loadJobs(category = "Rédaction") })
                    }
                }
            }

            // Missions récentes
            if (displayJobs.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text("Missions récentes", fontWeight = FontWeight.Bold,
                            fontSize = 16.sp, color = Color(0xFF1A1A1A))
                        Text("Voir tout", color = GreenPrimary, fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { navController.navigate("missions") })
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                items(displayJobs) { job ->
                    RecentJobCard(job = job,
                        onClick = { navController.navigate("job_detail/${job.id}") })
                    Spacer(modifier = Modifier.height(1.dp))
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(GreenLight),
                contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = GreenPrimary,
                    modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp, color = Color(0xFF1A1A1A))
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
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                    .background(GreenLight), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Work, contentDescription = null,
                        tint = GreenPrimary, modifier = Modifier.size(22.dp))
                }
                Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF4CAF50).copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)) {
                    Text("Nouveau", color = GreenPrimary, fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = job.title, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                color = Color(0xFF1A1A1A), maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = job.client?.name ?: "", fontSize = 12.sp, color = Color(0xFF888888))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${job.budgetMin / 1000}k Ar", fontWeight = FontWeight.Bold,
                fontSize = 15.sp, color = GreenPrimary)
        }
    }
}

@Composable
fun RecentJobCard(job: mg.jn.seralink.model.JobListing, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF0F0F0)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Work, contentDescription = null,
                    tint = Color(0xFF888888), modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = job.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                    color = Color(0xFF1A1A1A), maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = job.client?.name ?: job.category,
                    fontSize = 12.sp, color = Color(0xFF888888))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "${job.budgetMin / 1000}k Ar", fontWeight = FontWeight.Bold,
                fontSize = 14.sp, color = GreenPrimary)
        }
    }
}