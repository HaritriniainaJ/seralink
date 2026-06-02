package mg.jn.seralink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardFreelanceScreen(navController: NavController) {

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Propositions", "Contrats")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Tableau de bord", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Bonjour Rakoto 👋", fontSize = 12.sp, color = Color(0xFF888888))
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
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
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
                            icon = Icons.Default.Send,
                            value = "12",
                            label = "Propositions",
                            color = GreenPrimary,
                            bgColor = GreenLight
                        )
                        DashStatCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Description,
                            value = "3",
                            label = "Contrats actifs",
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
                            icon = Icons.Default.HourglassEmpty,
                            value = "5",
                            label = "En attente",
                            color = Color(0xFFE65100),
                            bgColor = Color(0xFFFFF3E0)
                        )
                        DashStatCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Payments,
                            value = "2.4M Ar",
                            label = "Revenus totaux",
                            color = Color(0xFF6A1B9A),
                            bgColor = Color(0xFFF3E5F5)
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
                // Propositions
                if (sampleProposals.isEmpty()) {
                    item {
                        EmptyState(
                            icon = Icons.Default.Send,
                            message = "Aucune proposition envoyée",
                            subtitle = "Parcourez les missions et postulez !"
                        )
                    }
                } else {
                    items(sampleProposals) { proposal ->
                        ProposalCard(
                            proposal = proposal,
                            onClick = { navController.navigate("messages") }
                        )
                    }
                }
            } else {
                // Contrats
                if (sampleContracts.isEmpty()) {
                    item {
                        EmptyState(
                            icon = Icons.Default.Description,
                            message = "Aucun contrat actif",
                            subtitle = "Vos contrats apparaîtront ici"
                        )
                    }
                } else {
                    items(sampleContracts) { contract ->
                        ContractCard(
                            contract = contract,
                            onClick = { navController.navigate("messages/${contract.id}") }
                        )
                    }
                }
            }
        }
    }
}

data class SampleProposal(
    val id: Int,
    val jobTitle: String,
    val clientName: String,
    val budget: Int,
    val status: String,
    val sentDate: String
)

val sampleProposals = listOf(
    SampleProposal(1, "Développeur Mobile Flutter", "TechSolutions MG", 1200000, "pending", "Il y a 2 jours"),
    SampleProposal(2, "UI/UX Designer", "Creative MG", 850000, "accepted", "Il y a 5 jours"),
    SampleProposal(3, "Rédaction fiches produits", "E-commerce MG", 200000, "rejected", "Il y a 1 semaine"),
    SampleProposal(4, "Développement API REST", "StartupMG", 600000, "pending", "Aujourd'hui"),
)

@Composable
fun ProposalCard(proposal: SampleProposal, onClick: () -> Unit) {

    val statusColor = when (proposal.status) {
        "accepted" -> Color(0xFF2E7D32)
        "rejected" -> Color(0xFFE53935)
        else -> Color(0xFFE65100)
    }
    val statusBg = when (proposal.status) {
        "accepted" -> Color(0xFFE8F5E9)
        "rejected" -> Color(0xFFFFEBEE)
        else -> Color(0xFFFFF3E0)
    }
    val statusLabel = when (proposal.status) {
        "accepted" -> "Acceptée ✓"
        "rejected" -> "Refusée ✗"
        else -> "En attente"
    }

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
                    proposal.jobTitle,
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Business, contentDescription = null,
                        tint = Color(0xFF888888), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(proposal.clientName, fontSize = 13.sp, color = Color(0xFF555555))
                }
                Text(
                    "${proposal.budget / 1000} 000 Ar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = GreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(proposal.sentDate, fontSize = 12.sp, color = Color(0xFF888888))

                // Bouton message si acceptée
                if (proposal.status == "accepted") {
                    TextButton(
                        onClick = onClick,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Message, contentDescription = null,
                            tint = GreenPrimary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ouvrir le chat", color = GreenPrimary,
                            fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun DashStatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    color: Color,
    bgColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1A1A1A))
                Text(label, fontSize = 11.sp, color = Color(0xFF888888))
            }
        }
    }
}

@Composable
fun EmptyState(icon: ImageVector, message: String, subtitle: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null,
                modifier = Modifier.size(64.dp), tint = Color(0xFFBBBBBB))
            Spacer(modifier = Modifier.height(12.dp))
            Text(message, color = Color(0xFF888888), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, color = Color(0xFFBBBBBB), fontSize = 13.sp)
        }
    }
}