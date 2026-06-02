package mg.jn.seralink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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

data class ChatMessage(
    val id: Int,
    val content: String,
    val isMe: Boolean,
    val time: String,
    val senderName: String
)

val sampleMessages = listOf(
    ChatMessage(1, "Bonjour, j'ai bien reçu votre proposition.", false, "09:00", "TechSolutions MG"),
    ChatMessage(2, "Bonjour ! Merci, je suis disponible dès lundi.", true, "09:05", "Moi"),
    ChatMessage(3, "Parfait. Pouvez-vous commencer par la page d'accueil ?", false, "09:10", "TechSolutions MG"),
    ChatMessage(4, "Oui bien sûr, je vais préparer une maquette d'abord.", true, "09:12", "Moi"),
    ChatMessage(5, "Super, on attend votre retour. Bon courage !", false, "09:15", "TechSolutions MG"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, contractId: Int) {
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val conversation = sampleConversations.find { it.contractId == contractId }

    LaunchedEffect(sampleMessages.size) {
        if (sampleMessages.isNotEmpty()) {
            listState.animateScrollToItem(sampleMessages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = conversation?.otherPersonName?.take(2)?.uppercase() ?: "??",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = conversation?.otherPersonName ?: "Chat",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = conversation?.missionTitle ?: "",
                                fontSize = 11.sp,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Écrire un message...", fontSize = 14.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = GreenPrimary
                        ),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (messageText.isNotBlank()) GreenPrimary else Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { if (messageText.isNotBlank()) messageText = "" },
                            enabled = messageText.isNotBlank()
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Envoyer",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        },
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sampleMessages) { message ->
                MessageBubble(message = message)
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMe) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isMe) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.senderName.take(2).uppercase(),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (message.isMe) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = if (message.isMe) 16.dp else 4.dp,
                            topEnd = if (message.isMe) 4.dp else 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
                    .background(if (message.isMe) GreenPrimary else Color.White)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .widthIn(max = 260.dp)
            ) {
                Text(
                    text = message.content,
                    color = if (message.isMe) Color.White else Color(0xFF1A1A1A),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = message.time,
                fontSize = 11.sp,
                color = Color(0xFF888888)
            )
        }
    }
}