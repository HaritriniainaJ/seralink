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

data class ConversationItem(
    val id: Int,
    val contractId: Int,
    val otherPersonName: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val missionTitle: String,
    val isOnline: Boolean = false,
    val isRead: Boolean = true
)

val sampleConversations = listOf(
    ConversationItem(1, 1, "Mamy Rakoto", "Bonjour, j'ai bien reçu les spécifica...", "14:32", 2, "Création site e-commerce", isOnline = true, isRead = false),
    ConversationItem(2, 2, "Lalao Razafy", "Parfait, on se voit demain pour le kick...", "Hier", 0, "Logo startup Agritech", isOnline = false, isRead = true),
    ConversationItem(3, 3, "Jean Dupont (Client)", "Merci pour la rapidité de l'envoi du contrat.", "Lun.", 0, "Traduction technique", isOnline = false, isRead = true),
    ConversationItem(4, 4, "Andry Randria", "Est-ce que tu pourrais m'envoyer le...", "Lun.", 1, "Campagne Facebook", isOnline = true, isRead = false),
    ConversationItem(5, 5, "Sitraka Andria", "Le paiement a été validé par la plateforme.", "Dim.", 0, "Développement API", isOnline = false, isRead = true),
)

val messageFilters = listOf("Tout", "Non lus", "Missions", "Favoris")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesListScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("Tout") }
    var searchText by remember { mutableStateOf("") }

    val filteredConversations = when (selectedFilter) {
        "Non lus" -> sampleConversations.filter { !it.isRead }
        else -> sampleConversations
    }.filter {
        searchText.isEmpty() || it.otherPersonName.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Menu, contentDescription = null, tint = Color(0xFF333333))
                        }
                        Text(
                            "Messages",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = GreenPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFF333333))
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFBDBDBD))
                            .padding(end = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
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
                    selected = true,
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
                    onClick = { navController.navigate("profil") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profil", fontSize = 11.sp) }
                )
            }
        },
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            // Barre de recherche
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Rechercher une conversation...", fontSize = 14.sp, color = Color(0xFFAAAAAA)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFAAAAAA)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = GreenPrimary,
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5)
                ),
                singleLine = true
            )

            // Filtres
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(messageFilters) { filter ->
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

            // Liste conversations
            if (filteredConversations.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFFBBBBBB))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Aucune conversation", color = Color(0xFF888888), fontSize = 15.sp)
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredConversations) { conversation ->
                        ConversationCard(
                            conversation = conversation,
                            onClick = { navController.navigate("chat/${conversation.contractId}") }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 86.dp, end = 16.dp),
                            color = Color(0xFFF0F0F0)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationCard(conversation: ConversationItem, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (!conversation.isRead) Color(0xFFF0FFF0) else Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar avec indicateur online
            Box(modifier = Modifier.size(52.dp)) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(GreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversation.otherPersonName.take(2).uppercase(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (conversation.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .align(Alignment.BottomEnd)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50))
                                .align(Alignment.Center)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.otherPersonName,
                        fontWeight = if (!conversation.isRead) FontWeight.Bold else FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = conversation.time,
                        fontSize = 12.sp,
                        color = if (!conversation.isRead) GreenPrimary else Color(0xFF888888),
                        fontWeight = if (!conversation.isRead) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage,
                        fontSize = 13.sp,
                        color = if (!conversation.isRead) Color(0xFF333333) else Color(0xFF888888),
                        fontWeight = if (!conversation.isRead) FontWeight.Medium else FontWeight.Normal,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    if (conversation.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(GreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = conversation.unreadCount.toString(),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}