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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.model.Contract
import mg.jn.seralink.viewmodel.ContractViewModel
import mg.jn.seralink.viewmodel.ContractViewModelFactory

val msgFilterList = listOf("Tout", "Actifs", "Terminés")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesListScreen(navController: NavController) {

    val context = androidx.compose.ui.platform.LocalContext.current
    val dataStore = TokenDataStore(context)
    val viewModel: ContractViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = ContractViewModelFactory(dataStore)
    )
    val contracts by viewModel.contracts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val userRole by dataStore.userRole.collectAsState(initial = "freelance")

    var selectedFilter by remember { mutableStateOf("Tout") }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadContracts()
    }

    val filteredContracts = contracts.filter { contract ->
        val matchesFilter = when (selectedFilter) {
            "Actifs" -> contract.status == "active"
            "Terminés" -> contract.status == "completed"
            else -> true
        }
        val otherName = if (userRole == "client") contract.freelance?.name else contract.client?.name
        val matchesSearch = searchText.isEmpty() ||
                otherName?.contains(searchText, ignoreCase = true) == true ||
                contract.jobListing?.title?.contains(searchText, ignoreCase = true) == true
        matchesFilter && matchesSearch
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Messages", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = GreenPrimary)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            SeraLinkBottomBar(navController = navController, selected = "messages", userRole = userRole)
        },
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).background(Color.White)) {

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Rechercher une conversation...", fontSize = 14.sp, color = Color(0xFFAAAAAA)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFAAAAAA)) },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = { searchText = "" }) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFFAAAAAA))
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = GreenPrimary,
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5)
                ),
                singleLine = true
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(msgFilterList) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) GreenPrimary else Color.White)
                            .border(1.dp, if (isSelected) GreenPrimary else Color(0xFFE0E0E0), RoundedCornerShape(20.dp))
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

            HorizontalDivider(color = Color(0xFFF0F0F0))

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }
                filteredContracts.isNotEmpty() -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(filteredContracts) { contract ->
                            ContractConversationCard(
                                contract = contract,
                                userRole = userRole,
                                onClick = { navController.navigate("chat/${contract.id}") }
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 86.dp, end = 16.dp),
                                color = Color(0xFFF0F0F0)
                            )
                        }
                    }
                }
                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Message, contentDescription = null,
                                modifier = Modifier.size(64.dp), tint = Color(0xFFBBBBBB)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Aucune conversation", color = Color(0xFF888888), fontSize = 15.sp)
                            Text("Vos conversations apparaîtront ici", color = Color(0xFFBBBBBB), fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContractConversationCard(contract: Contract, userRole: String?, onClick: () -> Unit) {
    val otherPerson = if (userRole == "client") contract.freelance else contract.client
    val otherName = otherPerson?.name ?: "Inconnu"
    val initials = otherName.split(" ")
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .take(2).joinToString("").ifEmpty { "??" }

    val statusColor = when (contract.status) {
        "active" -> GreenPrimary
        "completed" -> Color(0xFF1565C0)
        "disputed" -> Color(0xFFE53935)
        else -> Color(0xFF888888)
    }
    val statusLabel = when (contract.status) {
        "active" -> "Actif"
        "completed" -> "Terminé"
        "disputed" -> "Litige"
        "cancelled" -> "Annulé"
        else -> contract.status
    }

    Surface(onClick = onClick, color = Color.White) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape).background(GreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(initials, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        otherName, fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp, color = Color(0xFF1A1A1A)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(statusColor.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(statusLabel, fontSize = 11.sp, color = statusColor, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    contract.jobListing?.title ?: "Contrat #${contract.id}",
                    fontSize = 13.sp, color = Color(0xFF888888), maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "${contract.amount / 1000} 000 Ar",
                    fontSize = 12.sp, color = GreenPrimary, fontWeight = FontWeight.Medium
                )
            }
        }
    }
}