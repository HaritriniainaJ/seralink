package mg.jn.seralink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mg.jn.seralink.ui.screens.*
import mg.jn.seralink.ui.theme.SeraLinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeraLinkTheme {
                SeraLinkApp()
            }
        }
    }
}

@Composable
fun SeraLinkApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }

        composable(Routes.REGISTER) {
            RegisterScreen(navController = navController)
        }

        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }

        composable(
            route = "review/{contractId}",
            arguments = listOf(navArgument("contractId") { type = NavType.IntType })
        ) { backStackEntry ->
            val contractId = backStackEntry.arguments?.getInt("contractId") ?: 1
            ReviewScreen(navController = navController, contractId = contractId)
        }

        composable("dashboard_freelance") {
            DashboardFreelanceScreen(navController = navController)
        }

        composable(
            route = "payment/{contractId}",
            arguments = listOf(navArgument("contractId") { type = NavType.IntType })
        ) { backStackEntry ->
            val contractId = backStackEntry.arguments?.getInt("contractId") ?: 1
            PaymentScreen(navController = navController, contractId = contractId)
        }

        composable("dashboard_client") {
            DashboardClientScreen(navController = navController)
        }

        composable("publier_mission") {
            PublierMissionScreen(navController = navController)
        }

        composable("missions") {
            MissionsScreen(navController = navController)
        }

        composable(
            route = Routes.JOB_DETAIL,
            arguments = listOf(navArgument("jobId") { type = NavType.IntType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getInt("jobId") ?: 1
            JobDetailScreen(navController = navController, jobId = jobId)
        }

        composable(
            route = "postuler/{jobId}",
            arguments = listOf(navArgument("jobId") { type = NavType.IntType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getInt("jobId") ?: 1
            PostulerScreen(navController = navController, jobId = jobId)
        }

        composable("messages") {
            MessagesListScreen(navController = navController)
        }

        composable(
            route = "chat/{contractId}",
            arguments = listOf(navArgument("contractId") { type = NavType.IntType })
        ) { backStackEntry ->
            val contractId = backStackEntry.arguments?.getInt("contractId") ?: 1
            ChatScreen(navController = navController, contractId = contractId)
        }

        composable(Routes.MY_CONTRACTS) {
            MyContractsScreen(navController = navController)
        }

        composable("profil") {
            ProfilScreen(navController = navController)
        }

        composable(
            route = Routes.FREELANCE_PROFILE,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            FreelanceProfileScreen(navController = navController, userId = userId)
        }
    }
}