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
import mg.jn.seralink.model.Message
import mg.jn.seralink.viewmodel.MessageViewModel
import mg.jn.seralink.viewmodel.MessageState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, contractId: Int) {

    val viewModel: MessageViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val messageState by viewModel.messageState.collectAsState()
    val sendState by viewModel.sendState.collectAsState()

    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val context = androidx.compose.ui.platform.LocalContext.current
    val dataStore = mg.jn.seralink.data.TokenDataStore(context)

    LaunchedEffect(contractId) {
        viewModel.loadMessages(contractId)
    }

    val messages = when (messageState) {
        is MessageState.Success -> (messageState as MessageState.Success).messages
        else -> emptyList()
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LaunchedEffect(sendState) {
        if (sendState != null) viewModel.resetSend()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(36.dp).clip(CircleShape).background(GreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("CH", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Contrat #$contractId", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Conversation", fontSize = 11.sp, color = Color(0xFF888888))
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
            Surface(shadowElevation = 8.dp, color = Color.White) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
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
                        modifier = Modifier.size(48.dp).clip(CircleShape)
                            .background(if (messageText.isNotBlank()) GreenPrimary else Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                if (messageText.isNotBlank()) {
                                    viewModel.sendMessage(contractId, messageText)
                                    messageText = ""
                                }
                            },
                            enabled = messageText.isNotBlank()
                        ) {
                            Icon(
                                Icons.Default.Send, contentDescription = "Envoyer",
                                tint = Color.White, modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        },
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        when (messageState) {
            is MessageState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            }
            else -> {
                if (messages.isEmpty() && messageState !is MessageState.Loading) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Chat, contentDescription = null,
                                tint = Color(0xFFCCCCCC), modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Aucun message pour l'instant",
                                fontSize = 14.sp, color = Color(0xFF999999)
                            )
                            Text(
                                "Commencez la conversation !",
                                fontSize = 13.sp, color = Color(0xFFBBBBBB)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(messages) { message ->
                            RealMessageBubble(message = message)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RealMessageBubble(message: Message) {
    val isMe = message.senderType == "me" || message.isFromMe

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        if (!isMe) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(GreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.sender?.name?.take(2)?.uppercase() ?: "??",
                    color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Column(horizontalAlignment = if (isMe) Alignment.End else Alignment.Start) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = if (isMe) 16.dp else 4.dp,
                            topEnd = if (isMe) 4.dp else 16.dp,
                            bottomStart = 16.dp, bottomEnd = 16.dp
                        )
                    )
                    .background(if (isMe) GreenPrimary else Color.White)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .widthIn(max = 260.dp)
            ) {
                Text(
                    text = message.content,
                    color = if (isMe) Color.White else Color(0xFF1A1A1A),
                    fontSize = 14.sp, lineHeight = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = message.createdAt.takeLast(5),
                fontSize = 11.sp, color = Color(0xFF888888)
            )
        }
    }
}