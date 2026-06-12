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
import mg.jn.seralink.viewmodel.DashboardViewModel
import mg.jn.seralink.viewmodel.DashboardState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardFreelanceScreen(navController: NavController) {

    val viewModel: DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val dashboardState by viewModel.dashboardState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Propositions", "Contrats")

    val context = androidx.compose.ui.platform.LocalContext.current
    val dataStore = mg.jn.seralink.data.TokenDataStore(context)
    val userName by dataStore.userName.collectAsState(initial = "")

    LaunchedEffect(Unit) {
        viewModel.loadFreelanceDashboard()
    }

    val proposals = when (dashboardState) {
        is DashboardState.FreelanceSuccess -> (dashboardState as DashboardState.FreelanceSuccess).data.proposals
        else -> emptyList()
    }
    val contracts = when (dashboardState) {
        is DashboardState.FreelanceSuccess -> (dashboardState as DashboardState.FreelanceSuccess).data.contracts
        else -> emptyList()
    }
    val stats = (dashboardState as? DashboardState.FreelanceSuccess)?.data

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Tableau de bord", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Bonjour ${userName ?: ""} 👋", fontSize = 12.sp, color = Color(0xFF888888))
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
            SeraLinkBottomBar(navController = navController, selected = "home", userRole = "freelance")
        },
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            if (dashboardState is DashboardState.Loading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }
            }

            // Stats
            item {
                Column(modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DashStatCard(
                            modifier = Modifier.weight(1f), icon = Icons.Default.Send,
                            value = stats?.totalProposals?.toString() ?: "0",
                            label = "Propositions", color = GreenPrimary, bgColor = GreenLight
                        )
                        DashStatCard(
                            modifier = Modifier.weight(1f), icon = Icons.Default.Description,
                            value = stats?.activeContracts?.toString() ?: "0",
                            label = "Contrats actifs", color = Color(0xFF1565C0), bgColor = Color(0xFFE3F2FD)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DashStatCard(
                            modifier = Modifier.weight(1f), icon = Icons.Default.HourglassEmpty,
                            value = stats?.pendingProposals?.toString() ?: "0",
                            label = "En attente", color = Color(0xFFE65100), bgColor = Color(0xFFFFF3E0)
                        )
                        DashStatCard(
                            modifier = Modifier.weight(1f), icon = Icons.Default.Payments,
                            value = stats?.totalEarnings?.let { "${it / 1000}k Ar" } ?: "0 Ar",
                            label = "Revenus totaux", color = Color(0xFF6A1B9A), bgColor = Color(0xFFF3E5F5)
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

            if (selectedTab == 0) {
                if (proposals.isEmpty() && dashboardState !is DashboardState.Loading) {
                    item {
                        EmptyState(
                            icon = Icons.Default.Send,
                            message = "Aucune proposition envoyée",
                            subtitle = "Parcourez les missions et postulez !"
                        )
                    }
                } else {
                    items(proposals) { proposal ->
                        RealProposalCard(
                            proposal = proposal,
                            onClick = { proposal.contractId?.let { navController.navigate("chat/$it") } }
                        )
                    }
                }
            } else {
                if (contracts.isEmpty() && dashboardState !is DashboardState.Loading) {
                    item {
                        EmptyState(
                            icon = Icons.Default.Description,
                            message = "Aucun contrat actif",
                            subtitle = "Vos contrats apparaîtront ici"
                        )
                    }
                } else {
                    items(contracts) { contract ->
                        RealContractCard(
                            contract = contract,
                            onClick = { navController.navigate("chat/${contract.id}") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RealProposalCard(proposal: mg.jn.seralink.model.Proposal, onClick: () -> Unit) {
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
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
                    proposal.jobListing?.title ?: proposal.job?.title ?: "Mission",
                    fontWeight = FontWeight.Bold, fontSize = 14.sp,
                    color = Color(0xFF1A1A1A), modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(statusBg)
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
                Text(
                    "${proposal.budget / 1000} 000 Ar",
                    fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GreenPrimary
                )
            }
            if (proposal.status == "accepted") {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onClick, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) {
                    Icon(Icons.Default.Message, contentDescription = null,
                        tint = GreenPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ouvrir le chat", color = GreenPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun RealContractCard(contract: mg.jn.seralink.model.Contract, onClick: () -> Unit) {
    val statusColor = when (contract.status) {
        "active" -> GreenPrimary
        "completed" -> Color(0xFF1565C0)
        "disputed" -> Color(0xFFE53935)
        else -> Color(0xFF888888)
    }
    val statusBg = when (contract.status) {
        "active" -> GreenLight
        "completed" -> Color(0xFFE3F2FD)
        "disputed" -> Color(0xFFFFEBEE)
        else -> Color(0xFFF5F5F5)
    }
    val statusLabel = when (contract.status) {
        "active" -> "Actif"
        "completed" -> "Terminé"
        "disputed" -> "Litige"
        else -> contract.status
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
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
                    contract.jobListing?.title ?: "Contrat #${contract.id}",
                    fontWeight = FontWeight.Bold, fontSize = 14.sp,
                    color = Color(0xFF1A1A1A), modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(statusBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(statusLabel, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${contract.amount / 1000} 000 Ar",
                fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GreenPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onClick, contentPadding = PaddingValues(0.dp)) {
                Icon(Icons.Default.Message, contentDescription = null,
                    tint = GreenPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Ouvrir le chat", color = GreenPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
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
        modifier = modifier, shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(42.dp).clip(RoundedCornerShape(10.dp)).background(bgColor),
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
    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFFBBBBBB))
            Spacer(modifier = Modifier.height(12.dp))
            Text(message, color = Color(0xFF888888), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, color = Color(0xFFBBBBBB), fontSize = 13.sp)
        }
    }
}