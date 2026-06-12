package mg.jn.seralink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mg.jn.seralink.model.CreateProposalRequest
import mg.jn.seralink.viewmodel.JobViewModel
import mg.jn.seralink.viewmodel.JobDetailState
import mg.jn.seralink.viewmodel.ProposalViewModel
import mg.jn.seralink.viewmodel.ProposalActionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostulerScreen(navController: NavController, jobId: Int) {

    val jobViewModel: JobViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val proposalViewModel: ProposalViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    val jobDetailState by jobViewModel.jobDetailState.collectAsState()
    val actionState by proposalViewModel.actionState.collectAsState()

    var coverLetter by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }

    LaunchedEffect(jobId) {
        jobViewModel.loadJobDetail(jobId)
    }

    val showSuccess = actionState is ProposalActionState.Success
    val isLoading = actionState is ProposalActionState.Loading
    val job = (jobDetailState as? JobDetailState.Success)?.job

    if (showSuccess) {
        AlertDialog(
            onDismissRequest = {
                proposalViewModel.resetAction()
                navController.popBackStack()
            },
            icon = {
                Icon(Icons.Default.CheckCircle, contentDescription = null,
                    tint = GreenPrimary, modifier = Modifier.size(48.dp))
            },
            title = { Text("Proposition envoyée !", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            text = {
                Text("Votre proposition a été envoyée au client. Vous serez notifié dès qu'il répondra.",
                    fontSize = 14.sp, color = Color(0xFF555555))
            },
            confirmButton = {
                Button(
                    onClick = {
                        proposalViewModel.resetAction()
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("OK") }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (actionState is ProposalActionState.Error) {
        AlertDialog(
            onDismissRequest = { proposalViewModel.resetAction() },
            icon = {
                Icon(Icons.Default.ErrorOutline, contentDescription = null,
                    tint = Color(0xFFE53935), modifier = Modifier.size(48.dp))
            },
            title = { Text("Erreur", fontWeight = FontWeight.Bold) },
            text = {
                Text((actionState as ProposalActionState.Error).message,
                    fontSize = 14.sp, color = Color(0xFF555555))
            },
            confirmButton = {
                Button(onClick = { proposalViewModel.resetAction() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(10.dp)) { Text("OK") }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    val formValid = coverLetter.isNotBlank() && budget.isNotBlank() && deadline.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Postuler à la mission", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            if (job != null) {
                Surface(shadowElevation = 8.dp, color = Color.White) {
                    Button(
                        onClick = {
                            if (formValid) {
                                proposalViewModel.submitProposal(
                                    jobId = jobId,
                                    request = CreateProposalRequest(
                                        coverLetter = coverLetter,
                                        budget = budget.toIntOrNull() ?: 0,
                                        deadline = deadline
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (formValid) GreenPrimary else Color(0xFFBBBBBB)
                        ),
                        enabled = formValid && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White,
                                modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Envoyer ma proposition", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        },
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        when {
            jobDetailState is JobDetailState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            }
            jobDetailState is JobDetailState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.WifiOff, contentDescription = null,
                            tint = Color(0xFFCCCCCC), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Impossible de charger la mission",
                            fontSize = 14.sp, color = Color(0xFF999999))
                    }
                }
            }
            job != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Mission concernée", fontSize = 12.sp, color = Color(0xFF888888),
                                fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(job.title, fontWeight = FontWeight.Bold, fontSize = 15.sp,
                                color = Color(0xFF1A1A1A))
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                    .background(GreenLight).padding(horizontal = 10.dp, vertical = 4.dp)) {
                                    Text(job.category, color = GreenPrimary, fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold)
                                }
                                Text("Budget client : ${job.budgetMin / 1000} 000 Ar",
                                    fontSize = 12.sp, color = Color(0xFF888888))
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("Votre proposition", fontWeight = FontWeight.Bold, fontSize = 16.sp,
                                color = Color(0xFF1A1A1A))
                            Spacer(modifier = Modifier.height(20.dp))

                            Text("Budget proposé (Ar)", fontSize = 13.sp, fontWeight = FontWeight.Medium,
                                color = Color(0xFF333333))
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = budget,
                                onValueChange = { budget = it.filter { c -> c.isDigit() } },
                                placeholder = { Text("Ex: 150000", color = Color(0xFFAAAAAA)) },
                                leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null, tint = GreenPrimary) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color(0xFFE0E0E0), focusedBorderColor = GreenPrimary)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Délai de livraison", fontSize = 13.sp, fontWeight = FontWeight.Medium,
                                color = Color(0xFF333333))
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = deadline,
                                onValueChange = { deadline = it },
                                placeholder = { Text("Ex: 2025-08-15", color = Color(0xFFAAAAAA)) },
                                leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GreenPrimary) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color(0xFFE0E0E0), focusedBorderColor = GreenPrimary)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Lettre de motivation", fontSize = 13.sp, fontWeight = FontWeight.Medium,
                                color = Color(0xFF333333))
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = coverLetter,
                                onValueChange = { if (it.length <= 500) coverLetter = it },
                                placeholder = {
                                    Text("Expliquez pourquoi vous êtes le meilleur candidat...",
                                        color = Color(0xFFAAAAAA), fontSize = 13.sp)
                                },
                                modifier = Modifier.fillMaxWidth().height(160.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color(0xFFE0E0E0), focusedBorderColor = GreenPrimary),
                                maxLines = 8
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("${coverLetter.length}/500 caractères", fontSize = 11.sp,
                                color = if (coverLetter.length > 450) Color(0xFFE53935) else Color(0xFF888888),
                                modifier = Modifier.align(Alignment.End))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null,
                                tint = GreenPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Conseils pour une bonne proposition",
                                    fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = GreenPrimary)
                                Spacer(modifier = Modifier.height(6.dp))
                                listOf(
                                    "Soyez précis sur votre expérience",
                                    "Proposez un budget réaliste",
                                    "Mentionnez des projets similaires",
                                    "Restez professionnel et concis"
                                ).forEach { conseil ->
                                    Row(verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 2.dp)) {
                                        Box(modifier = Modifier.size(5.dp)
                                            .clip(RoundedCornerShape(50)).background(GreenPrimary))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(conseil, fontSize = 12.sp, color = Color(0xFF2E7D32))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}