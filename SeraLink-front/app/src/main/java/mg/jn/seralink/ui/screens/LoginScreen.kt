package mg.jn.seralink.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mg.jn.seralink.R
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import mg.jn.seralink.viewmodel.AuthViewModel
import mg.jn.seralink.viewmodel.AuthState



@Composable
fun LoginScreen(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val authState by viewModel.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("client") }

    // Observer l'état auth
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "SeraLink",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GreenPrimary
            )
            Text(
                text = "La plateforme freelance malgache",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Sélection du rôle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { selectedRole = "client" },
                            modifier = Modifier.weight(1f).height(64.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                2.dp,
                                if (selectedRole == "client") GreenPrimary else Color(0xFFE0E0E0)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedRole == "client") GreenLight else Color.White
                            )
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (selectedRole == "client") GreenPrimary else Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    "Je suis Client",
                                    fontSize = 12.sp,
                                    color = if (selectedRole == "client") GreenPrimary else Color.Gray,
                                    fontWeight = if (selectedRole == "client") FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { selectedRole = "freelance" },
                            modifier = Modifier.weight(1f).height(64.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                2.dp,
                                if (selectedRole == "freelance") GreenPrimary else Color(0xFFE0E0E0)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedRole == "freelance") GreenLight else Color.White
                            )
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Work,
                                    contentDescription = null,
                                    tint = if (selectedRole == "freelance") GreenPrimary else Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    "Je suis Freelance",
                                    fontSize = 12.sp,
                                    color = if (selectedRole == "freelance") GreenPrimary else Color.Gray,
                                    fontWeight = if (selectedRole == "freelance") FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Erreur
                    if (authState is AuthState.Error) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFEBEE)
                            )
                        ) {
                            Text(
                                text = (authState as AuthState.Error).message,
                                color = Color(0xFFD32F2F),
                                modifier = Modifier.padding(12.dp),
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Champ Email
                    Text(
                        text = "Email",
                        modifier = Modifier.align(Alignment.Start),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("votre@email.mg", color = Color.LightGray) },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = GreenPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Champ Mot de passe
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mot de passe",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        TextButton(onClick = {}) {
                            Text("Oublié ?", color = GreenPrimary, fontSize = 13.sp)
                        }
                    }
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = GreenPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Bouton Se connecter
                    Button(
                        onClick = {
                            if (email.isNotBlank() && password.isNotBlank()) {
                                viewModel.login(email, password, selectedRole)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        enabled = authState !is AuthState.Loading
                    ) {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Se connecter", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Pas encore inscrit ? ", fontSize = 14.sp, color = Color.Gray)
                        TextButton(
                            onClick = { navController.navigate(Routes.REGISTER) },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                "Créer un compte",
                                color = GreenPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                        Text("  OU  ", color = Color.Gray, fontSize = 13.sp)
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = "Google",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Continuer avec Google", fontSize = 14.sp, color = Color.DarkGray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = buildAnnotatedString {
                    append("En vous connectant, vous acceptez nos ")
                    withStyle(SpanStyle(color = GreenPrimary)) { append("Conditions d'Utilisation") }
                    append(" et notre ")
                    withStyle(SpanStyle(color = GreenPrimary)) { append("Politique de Confidentialité") }
                    append(".")
                },
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}