package mg.jn.seralink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class SampleMission(
    val id: Int,
    val title: String,
    val proposalCount: Int,
    val status: String,
    val budget: Int,
    val postedDate: String
)

data class SampleApplication(
    val id: Int,
    val freelanceName: String,
    val initials: String,
    val jobTitle: String,
    val missionTitle: String,
    val budget: Int,
    val message: String,
    val sentDate: String
)

val clientSampleMissions = listOf(
    SampleMission(1, "Développeur Mobile Flutter", 5, "active", 1200000, "Il y a 3 jours"),
    SampleMission(2, "UI/UX Designer Web", 2, "active", 800000, "Il y a 1 semaine"),
    SampleMission(3, "Rédacteur SEO", 8, "closed", 300000, "Il y a 2 semaines"),
)

val clientSampleApplications = listOf(
    SampleApplication(1, "Rakoto Jean", "RJ", "Développeur Fullstack", "Développeur Mobile Flutter", 1100000, "Bonjour, je suis disponible pour ce projet...", "Il y a 2 jours"),
    SampleApplication(2, "Faly Andriantsoa", "FA", "UI/UX Designer", "UI/UX Designer Web", 750000, "J'ai 5 ans d'expérience en design...", "Il y a 3 jours"),
    SampleApplication(3, "Haja Rakotondrabe", "HR", "Développeur Android", "Développeur Mobile Flutter", 1200000, "Je maîtrise Flutter et Kotlin...", "Aujourd'hui"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardClientScreen(navController: NavController) {

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Mes Missions", "Propositions reçues")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Tableau de bord", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Bonjour Client 👋", fontSize = 12.sp, color = Color(0xFF888888))
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFF333333))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = {},
                    containerColor = GreenPrimary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Publier une mission")
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                    label = { Text("Dashboard", fontSize = 11.sp) }
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
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {

            // Stats cards
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashStatCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Work,
                            value = "3",
                            label = "Missions publiées",
                            color = GreenPrimary,
                            bgColor = GreenLight
                        )
                        DashStatCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.People,
                            value = "15",
                            label = "Propositions reçues",
                            color = Color(0xFF1565C0),
                            bgColor = Color(0xFFE3F2FD)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashStatCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Description,
                            value = "2",
                            label = "Contrats actifs",
                            color = Color(0xFF6A1B9A),
                            bgColor = Color(0xFFF3E5F5)
                        )
                        DashStatCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Payments,
                            value = "3.1M Ar",
                            label = "Dépenses totales",
                            color = Color(0xFFE65100),
                            bgColor = Color(0xFFFFF3E0)
                        )
                    }
                }
            }

            // Tabs
            item {
                Spacer(modifier = Modifier.height(12.dp))
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = GreenPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = GreenPrimary
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        )
                    }
                }
            }

            // Contenu selon tab
            if (selectedTab == 0) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                if (clientSampleMissions.isEmpty()) {
                    item {
                        EmptyState(
                            icon = Icons.Default.Work,
                            message = "Aucune mission publiée",
                            subtitle = "Cliquez sur + pour publier votre première mission"
                        )
                    }
                } else {
                    items(clientSampleMissions) { mission ->
                        ClientMissionCard(mission = mission, navController = navController)
                    }
                }
            } else {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                if (clientSampleApplications.isEmpty()) {
                    item {
                        EmptyState(
                            icon = Icons.Default.Inbox,
                            message = "Aucune proposition reçue",
                            subtitle = "Les freelances postuleront à vos missions ici"
                        )
                    }
                } else {
                    items(clientSampleApplications) { application ->
                        ApplicationCard(
                            application = application,
                            onMessage = { navController.navigate("messages") },
                            onAccept = { navController.navigate(Routes.MY_CONTRACTS) },
                            onViewProfile = { navController.navigate("freelance_profile/${application.id}") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClientMissionCard(mission: SampleMission, navController: NavController) {

    val statusColor = if (mission.status == "active") GreenPrimary else Color(0xFF888888)
    val statusBg = if (mission.status == "active") GreenLight else Color(0xFFF0F0F0)
    val statusLabel = if (mission.status == "active") "Active" else "Fermée"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    mission.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(statusBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(statusLabel, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.People, contentDescription = null,
                        tint = Color(0xFF888888), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${mission.proposalCount} propositions",
                        fontSize = 13.sp, color = Color(0xFF555555))
                }
                Text(
                    "${mission.budget / 1000} 000 Ar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = GreenPrimary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(mission.postedDate, fontSize = 12.sp, color = Color(0xFF888888))
        }
    }
}

@Composable
fun ApplicationCard(
    application: SampleApplication,
    onMessage: () -> Unit,
    onAccept: () -> Unit,
    onViewProfile: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(GreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        application.initials,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        application.freelanceName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        application.jobTitle,
                        fontSize = 12.sp,
                        color = Color(0xFF888888)
                    )
                }
                Text(
                    "${application.budget / 1000} 000 Ar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = GreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Work, contentDescription = null,
                    tint = Color(0xFF888888), modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(application.missionTitle, fontSize = 12.sp, color = Color(0xFF555555))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                application.message,
                fontSize = 13.sp,
                color = Color(0xFF666666),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(application.sentDate, fontSize = 11.sp, color = Color(0xFFBBBBBB))

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF5F5F5))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewProfile,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF555555))
                ) {
                    Icon(Icons.Default.Person, contentDescription = null,
                        modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Profil", fontSize = 13.sp)
                }
                OutlinedButton(
                    onClick = onMessage,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary)
                ) {
                    Icon(Icons.Default.Message, contentDescription = null,
                        modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Message", fontSize = 13.sp)
                }
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null,
                        modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Accepter", fontSize = 13.sp)
                }
            }
        }
    }
}