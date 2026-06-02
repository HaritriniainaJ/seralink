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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mg.jn.seralink.model.Contract
import mg.jn.seralink.model.UserResponse

val sampleContracts = listOf(
    Contract(1, 1, 1, 2, 1200000, "active", "unpaid", "2025-08-01",
        UserResponse(1, "TechSolutions MG", "tech@seralink.mg", "client", null),
        UserResponse(2, "Rakoto Jean", "rakoto@seralink.mg", "freelance", null),
        null),
    Contract(2, 2, 1, 3, 800000, "pending", "unpaid", "2025-09-01",
        UserResponse(1, "Creative MG", "creative@seralink.mg", "client", null),
        UserResponse(3, "Miora Soa", "miora@seralink.mg", "freelance", null),
        null),
    Contract(3, 3, 1, 4, 500000, "completed", "paid", "2025-07-15",
        UserResponse(1, "E-commerce MG", "ecom@seralink.mg", "client", null),
        UserResponse(4, "Haja Feno", "haja@seralink.mg", "freelance", null),
        null),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyContractsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mes Contrats",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        if (sampleContracts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFFBBBBBB)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Aucun contrat pour le moment",
                        color = Color(0xFF888888),
                        fontSize = 15.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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

@Composable
fun ContractCard(contract: Contract, onClick: () -> Unit) {
    val statusColor = when (contract.status) {
        "active"    -> Color(0xFF2E7D32)
        "completed" -> Color(0xFF1565C0)
        "pending"   -> Color(0xFFE65100)
        else        -> Color(0xFF888888)
    }

    val statusBg = when (contract.status) {
        "active"    -> Color(0xFFE8F5E9)
        "completed" -> Color(0xFFE3F2FD)
        "pending"   -> Color(0xFFFFF3E0)
        else        -> Color(0xFFF5F5F5)
    }

    val statusLabel = when (contract.status) {
        "active"    -> "Actif"
        "completed" -> "Terminé"
        "pending"   -> "En attente"
        else        -> contract.status
    }

    val paymentColor = if (contract.paymentStatus == "paid") Color(0xFF2E7D32) else Color(0xFFE53935)
    val paymentLabel = if (contract.paymentStatus == "paid") "Payé ✓" else "Non payé"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header — statut + numéro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Contrat #${contract.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1A1A1A)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(statusBg)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(statusLabel, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Titre mission
            Text(
                text = contract.jobListing?.title ?: "Mission #${contract.jobListingId}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            // Montant + paiement
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Montant", fontSize = 11.sp, color = Color(0xFF888888))
                    Text(
                        "${contract.amount / 1000} 000 Ar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = GreenPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Paiement", fontSize = 11.sp, color = Color(0xFF888888))
                    Text(
                        paymentLabel,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = paymentColor
                    )
                }
            }

            // Délai
            contract.deadline?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFF888888)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Délai : $it", fontSize = 12.sp, color = Color(0xFF888888))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bouton messagerie
            if (contract.status == "active") {
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Icon(
                        Icons.Default.Message,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ouvrir la messagerie", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }
    }
}