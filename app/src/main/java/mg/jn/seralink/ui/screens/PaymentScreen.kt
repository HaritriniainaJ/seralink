package mg.jn.seralink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.viewmodel.ContractActionState
import mg.jn.seralink.viewmodel.ContractViewModel
import mg.jn.seralink.viewmodel.ContractViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController, contractId: Int) {

    val context = LocalContext.current
    val dataStore = TokenDataStore(context)
    val viewModel: ContractViewModel = viewModel(
        factory = ContractViewModelFactory(dataStore)
    )
    val actionState by viewModel.actionState.collectAsState()

    // Initialise Stripe avec ta clé publique
    LaunchedEffect(Unit) {
        PaymentConfiguration.init(
            context,
            "pk_test_51TfyHCLWI4ZXQnI59rT3F6MHa45HrRaPuf2RlUC0o5xQgRE5iga7IqRDVtfISMlniFyfLGHqJoONKAHpZshx3rbg00x5vk2Ffy"
        )
    }

    val paymentSheet = rememberPaymentSheet { result ->
        when (result) {
            is PaymentSheetResult.Completed -> {
                navController.navigate("my_contracts") {
                    popUpTo("payment/$contractId") { inclusive = true }
                }
            }
            is PaymentSheetResult.Canceled -> {}
            is PaymentSheetResult.Failed -> {}
        }
    }

    // Quand le clientSecret est prêt, ouvre Stripe
    LaunchedEffect(actionState) {
        if (actionState is ContractActionState.PaymentReady) {
            val clientSecret = (actionState as ContractActionState.PaymentReady).clientSecret
            paymentSheet.presentWithPaymentIntent(
                clientSecret,
                PaymentSheet.Configuration(
                    merchantDisplayName = "SeraLink"
                )
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paiement Escrow", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // Icône paiement
            Box(
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Payment, contentDescription = null,
                    tint = GreenPrimary, modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Paiement sécurisé", fontWeight = FontWeight.Bold,
                fontSize = 22.sp, color = Color(0xFF1A1A1A))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Les fonds seront bloqués en escrow jusqu'à la validation du travail.",
                fontSize = 14.sp, color = Color(0xFF888888),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center)

            Spacer(modifier = Modifier.height(32.dp))

            // Info contrat
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Contrat", fontSize = 14.sp, color = Color(0xFF888888))
                        Text("#$contractId", fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp, color = Color(0xFF1A1A1A))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Mode", fontSize = 14.sp, color = Color(0xFF888888))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null,
                                tint = GreenPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Escrow sécurisé", fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp, color = GreenPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Comment ça marche
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Comment ça marche ?", fontWeight = FontWeight.Bold,
                        fontSize = 14.sp, color = GreenPrimary)
                    Spacer(modifier = Modifier.height(10.dp))
                    listOf(
                        "1. Tu paies maintenant → fonds bloqués chez Stripe",
                        "2. Le freelance travaille sur ta mission",
                        "3. Tu valides le travail → fonds libérés au freelance",
                        "4. En cas de litige → support SeraLink intervient"
                    ).forEach { step ->
                        Text(step, fontSize = 13.sp, color = Color(0xFF2E7D32),
                            modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Bouton payer
            Button(
                onClick = { viewModel.payContract(contractId) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                enabled = actionState !is ContractActionState.Loading
            ) {
                if (actionState is ContractActionState.Loading) {
                    CircularProgressIndicator(color = Color.White,
                        modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Lock, contentDescription = null,
                        modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Payer maintenant", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Security, contentDescription = null,
                    tint = Color(0xFF888888), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Paiement sécurisé par Stripe", fontSize = 12.sp, color = Color(0xFF888888))
            }

            // Erreur
            if (actionState is ContractActionState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ErrorOutline, contentDescription = null,
                            tint = Color(0xFFE53935), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text((actionState as ContractActionState.Error).message,
                            color = Color(0xFFE53935), fontSize = 13.sp)
                    }
                }
            }
        }
    }
}