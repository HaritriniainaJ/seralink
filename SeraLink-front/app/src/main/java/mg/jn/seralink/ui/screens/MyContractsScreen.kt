package mg.jn.seralink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.model.Contract
import mg.jn.seralink.viewmodel.ContractActionState
import mg.jn.seralink.viewmodel.ContractViewModel
import mg.jn.seralink.viewmodel.ContractViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyContractsScreen(navController: NavController) {
    val context = LocalContext.current
    val dataStore = TokenDataStore(context)
    val viewModel: ContractViewModel = viewModel(
        factory = ContractViewModelFactory(dataStore)
    )

    val contracts by viewModel.contracts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val actionState by viewModel.actionState.collectAsState()
    val userRole by dataStore.userRole.collectAsState(initial = "freelance")

    LaunchedEffect(Unit) {
        viewModel.loadContracts()
    }

    when (actionState) {
        is ContractActionState.Success -> {
            AlertDialog(
                onDismissRequest = { viewModel.resetAction() },
                icon = {
                    Icon(Icons.Default.CheckCircle, contentDescription = null,
                        tint = GreenPrimary, modifier = Modifier.size(48.dp))
                },
                title = { Text("Succès", fontWeight = FontWeight.Bold) },
                text = { Text((actionState as ContractActionState.Success).message) },
                confirmButton = {
                    Button(onClick = { viewModel.resetAction() },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(10.dp)) { Text("OK") }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
        is ContractActionState.PaymentReady -> {
            val clientSecret = (actionState as ContractActionState.PaymentReady).clientSecret
            AlertDialog(
                onDismissRequest = { viewModel.resetAction() },
                icon = {
                    Icon(Icons.Default.Payment, contentDescription = null,
                        tint = GreenPrimary, modifier = Modifier.size(48.dp))
                },
                title = { Text("Paiement Escrow", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Votre paiement escrow a été créé avec succès via Stripe.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Référence : ${clientSecret.take(20)}...",
                            fontSize = 11.sp, color = Color(0xFF888888))
                    }
                },
                confirmButton = {
                    Button(onClick = { viewModel.resetAction() },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(10.dp)) { Text("OK") }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
        is ContractActionState.Error -> {
            AlertDialog(
                onDismissRequest = { viewModel.resetAction() },
                icon = {
                    Icon(Icons.Default.ErrorOutline, contentDescription = null,
                        tint = Color(0xFFE53935), modifier = Modifier.size(48.dp))
                },
                title = { Text("Erreur", fontWeight = FontWeight.Bold) },
                text = { Text((actionState as ContractActionState.Error).message) },
                confirmButton = {
                    Button(onClick = { viewModel.resetAction() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        shape = RoundedCornerShape(10.dp)) { Text("OK") }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
        else -> {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mes Contrats", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            SeraLinkBottomBar(navController = navController, selected = "contrats", userRole = userRole)
        },
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = GreenPrimary)
                }
                errorMessage != null -> {
                    Column(modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = errorMessage ?: "Erreur inconnue", color = Color(0xFFE53935), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.loadContracts() },
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)) {
                            Text("Réessayer")
                        }
                    }
                }
                contracts.isEmpty() -> {
                    Column(modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Description, contentDescription = null,
                            modifier = Modifier.size(64.dp), tint = Color(0xFFBBBBBB))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Aucun contrat pour le moment", color = Color(0xFF888888), fontSize = 15.sp)
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(contracts) { contract ->
                            ContractCard(
                                contract = contract,
                                userRole = userRole ?: "freelance",
                                isActionLoading = actionState is ContractActionState.Loading,
                                onOpenChat = { navController.navigate("chat/${contract.id}") },
                                onPay = { viewModel.payContract(contract.id) },
                                onComplete = { viewModel.completeContract(contract.id) },
                                onRelease = { viewModel.releaseContract(contract.id) },
                                onDispute = { viewModel.disputeContract(contract.id) },
                                onSign = { viewModel.signContract(contract.id) },
                                onDownloadPdf = { viewModel.downloadAndOpenPdf(context, contract.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContractCard(
    contract: Contract,
    userRole: String,
    isActionLoading: Boolean,
    onOpenChat: () -> Unit,
    onPay: () -> Unit,
    onComplete: () -> Unit,
    onRelease: () -> Unit,
    onDispute: () -> Unit,
    onSign: () -> Unit,
    onDownloadPdf: () -> Unit
) {
    val statusColor = when (contract.status) {
        "active" -> Color(0xFF2E7D32)
        "completed" -> Color(0xFF1565C0)
        "disputed" -> Color(0xFFE65100)
        else -> Color(0xFF888888)
    }
    val statusBg = when (contract.status) {
        "active" -> Color(0xFFE8F5E9)
        "completed" -> Color(0xFFE3F2FD)
        "disputed" -> Color(0xFFFFF3E0)
        else -> Color(0xFFF5F5F5)
    }
    val statusLabel = when (contract.status) {
        "active" -> "Actif"
        "completed" -> "Terminé"
        "disputed" -> "Litige"
        else -> contract.status
    }
    val paymentColor = when (contract.paymentStatus) {
        "released" -> Color(0xFF2E7D32)
        "escrowed" -> Color(0xFF1565C0)
        else -> Color(0xFFE53935)
    }
    val paymentLabel = when (contract.paymentStatus) {
        "released" -> "Payé ✓"
        "escrowed" -> "En escrow 🔒"
        else -> "Non payé"
    }

    val hasSigned = if (userRole == "client") contract.clientSigned else contract.freelanceSigned

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text("Contrat #${contract.id}", fontWeight = FontWeight.Bold,
                    fontSize = 15.sp, color = Color(0xFF1A1A1A))
                Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    .background(statusBg).padding(horizontal = 12.dp, vertical = 4.dp)) {
                    Text(statusLabel, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(contract.jobListing?.title ?: "Mission #${contract.jobListingId}",
                fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF333333))

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Montant", fontSize = 11.sp, color = Color(0xFF888888))
                    Text("${contract.amount / 1000} 000 Ar", fontWeight = FontWeight.Bold,
                        fontSize = 18.sp, color = GreenPrimary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Paiement", fontSize = 11.sp, color = Color(0xFF888888))
                    Text(paymentLabel, fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp, color = paymentColor)
                }
            }

            contract.deadline?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null,
                        modifier = Modifier.size(14.dp), tint = Color(0xFF888888))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Délai : $it", fontSize = 12.sp, color = Color(0xFF888888))
                }
            }

            // Statut signatures
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    .background(if (contract.clientSigned) Color(0xFFE8F5E9) else Color(0xFFF5F5F5))
                    .padding(horizontal = 10.dp, vertical = 4.dp)) {
                    Text(
                        if (contract.clientSigned) "✓ Client signé" else "Client non signé",
                        fontSize = 11.sp,
                        color = if (contract.clientSigned) Color(0xFF2E7D32) else Color(0xFF888888)
                    )
                }
                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    .background(if (contract.freelanceSigned) Color(0xFFE8F5E9) else Color(0xFFF5F5F5))
                    .padding(horizontal = 10.dp, vertical = 4.dp)) {
                    Text(
                        if (contract.freelanceSigned) "✓ Freelance signé" else "Freelance non signé",
                        fontSize = 11.sp,
                        color = if (contract.freelanceSigned) Color(0xFF2E7D32) else Color(0xFF888888)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bouton PDF (toujours visible)
            OutlinedButton(
                onClick = onDownloadPdf,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF37474F))
            ) {
                Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Voir le contrat PDF", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }

            if (contract.status == "active") {

                Spacer(modifier = Modifier.height(8.dp))

                if (!hasSigned) {
                    Button(
                        onClick = onSign,
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                        enabled = !isActionLoading
                    ) {
                        if (isActionLoading) {
                            CircularProgressIndicator(color = Color.White,
                                modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Edit, contentDescription = null,
                                modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Signer le contrat", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = onOpenChat,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Messagerie", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (userRole == "client") {
                    if (contract.paymentStatus == "unpaid") {
                        Button(
                            onClick = onPay,
                            modifier = Modifier.fillMaxWidth().height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                            enabled = !isActionLoading
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Payer l'escrow (Stripe)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    if (contract.paymentStatus == "escrowed") {
                        Button(
                            onClick = onRelease,
                            modifier = Modifier.fillMaxWidth().height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            enabled = !isActionLoading
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Valider & Libérer paiement", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                if (userRole == "freelance") {
                    Button(
                        onClick = onComplete,
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                        enabled = !isActionLoading
                    ) {
                        Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Marquer comme terminé", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                OutlinedButton(
                    onClick = onDispute,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
                    enabled = !isActionLoading
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ouvrir un litige", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }
    }
}