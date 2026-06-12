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
import mg.jn.seralink.viewmodel.JobViewModel
import mg.jn.seralink.viewmodel.JobState

val missionFilters = listOf("Récent", "Budget", "Urgent", "Long terme")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionsScreen(navController: NavController) {
    val viewModel: JobViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val jobState by viewModel.jobState.collectAsState()
    var selectedFilter by remember { mutableStateOf("Récent") }
    var searchQuery by remember { mutableStateOf("") }

    val context = androidx.compose.ui.platform.LocalContext.current
    val dataStore = mg.jn.seralink.data.TokenDataStore(context)
    val userRole by dataStore.userRole.collectAsState(initial = null)

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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            SeraLinkBottomBar(navController = navController, selected = "missions", userRole = userRole)
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Missions\ndisponibles", fontSize = 24.sp,
                            fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A), lineHeight = 30.sp
                        )
                        OutlinedButton(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.FilterList, contentDescription = null,
                                modifier = Modifier.size(16.dp), tint = Color(0xFF555555))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Filtres", fontSize = 13.sp, color = Color(0xFF555555))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            if (it.length >= 3 || it.isEmpty()) {
                                viewModel.loadJobs(search = it.ifBlank { null })
                            }
                        },
                        placeholder = { Text("Rechercher...", color = Color(0xFFAAAAAA), fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFAAAAAA)) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = ""; viewModel.loadJobs() }) {
                                    Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFFAAAAAA))
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = GreenPrimary,
                            unfocusedContainerColor = Color(0xFFF8F8F8),
                            focusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(missionFilters) { filter ->
                            val isSelected = filter == selectedFilter
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) GreenPrimary else Color.White)
                                    .border(1.dp,
                                        if (isSelected) GreenPrimary else Color(0xFFE0E0E0),
                                        RoundedCornerShape(20.dp))
                                    .clickable {
                                        selectedFilter = filter
                                        when (filter) {
                                            "Récent" -> viewModel.loadJobs()
                                            "Urgent" -> viewModel.loadJobs(search = "urgent")
                                            else -> viewModel.loadJobs()
                                        }
                                    }
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

            // Loading
            if (jobState is JobState.Loading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp),
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

            // Vide
            if (displayJobs.isEmpty() && jobState !is JobState.Loading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(48.dp),
                        contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Work, contentDescription = null,
                                tint = Color(0xFFCCCCCC), modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Aucune mission disponible",
                                fontSize = 14.sp, color = Color(0xFF999999))
                        }
                    }
                }
            }

            // Compteur
            if (jobState is JobState.Success && displayJobs.isNotEmpty()) {
                item {
                    Text(
                        text = "${displayJobs.size} mission(s) trouvée(s)",
                        fontSize = 13.sp, color = Color(0xFF888888),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                }
            }

            // Liste missions
            items(displayJobs) { job ->
                Spacer(modifier = Modifier.height(12.dp))
                MissionCard(job = job, onClick = { navController.navigate("job_detail/${job.id}") })
            }
        }
    }
}

@Composable
fun MissionCard(job: JobListing, onClick: () -> Unit) {
    val isUrgent = job.deadline?.contains("24h") == true

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
                        .background(GreenLight).padding(horizontal = 10.dp, vertical = 4.dp)) {
                        Text(job.category, color = GreenPrimary, fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold)
                    }
                    if (isUrgent) {
                        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFFFEBEE)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                            Text("Urgent", color = Color(0xFFE53935), fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                Icon(Icons.Default.FavoriteBorder, contentDescription = null,
                    tint = Color(0xFFBBBBBB), modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(job.title, fontWeight = FontWeight.Bold, fontSize = 15.sp,
                color = Color(0xFF1A1A1A), lineHeight = 22.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(job.description, fontSize = 13.sp, color = Color(0xFF888888),
                maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(job.category.split(" ").first(), job.budgetType).forEach { tag ->
                    Box(modifier = Modifier.clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF5F5F5)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                        Text(tag, fontSize = 11.sp, color = Color(0xFF555555))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(GreenPrimary),
                    contentAlignment = Alignment.Center) {
                    Text(
                        job.client?.name?.split(" ")?.mapNotNull { it.firstOrNull()?.uppercaseChar() }
                            ?.take(2)?.joinToString("") ?: "??",
                        color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(job.client?.name ?: "", fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp, color = Color(0xFF1A1A1A))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("${job.budgetMin / 1000} 000 Ar", fontWeight = FontWeight.Bold,
                        fontSize = 14.sp, color = GreenPrimary)
                    if (!job.deadline.isNullOrEmpty()) {
                        Text(job.deadline, fontSize = 11.sp,
                            color = if (isUrgent) Color(0xFFE53935) else Color(0xFF888888))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = GreenPrimary),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, GreenPrimary)
            ) {
                Text("Voir la mission", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }
}