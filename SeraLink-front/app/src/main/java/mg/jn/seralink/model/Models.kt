package mg.jn.seralink.model

import com.google.gson.annotations.SerializedName

// Auth
data class LoginRequest(
    val email: String,
    val password: String,
    val role: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String,
    val role: String
)

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val avatar: String?
)

data class LoginResponse(
    val token: String,
    val user: UserResponse
)

// Job
data class JobListing(
    val id: Int,
    @SerializedName("client_id")
    val clientId: Int,
    val title: String,
    val description: String,
    val category: String,
    @SerializedName("budget_min")
    val budgetMin: Int,
    @SerializedName("budget_max")
    val budgetMax: Int,
    @SerializedName("budget_type")
    val budgetType: String,
    val status: String,
    val deadline: String?,
    val client: UserResponse?
)

data class JobListingResponse(
    val data: List<JobListing>,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("last_page")
    val lastPage: Int,
    val total: Int
)

data class CreateJobRequest(
    val title: String,
    val description: String,
    val category: String,
    @SerializedName("budget_min")
    val budgetMin: Int,
    @SerializedName("budget_max")
    val budgetMax: Int,
    @SerializedName("budget_type")
    val budgetType: String,
    val deadline: String
)

// Proposal
data class Proposal(
    val id: Int,
    @SerializedName("job_listing_id")
    val jobListingId: Int,
    @SerializedName("freelance_id")
    val freelanceId: Int,
    @SerializedName("cover_letter")
    val coverLetter: String,
    val budget: Int,
    val deadline: String,
    val status: String,
    val freelance: UserResponse?,
    @SerializedName("job_listing")
    val jobListing: JobListing?
)

data class CreateProposalRequest(
    @SerializedName("cover_letter")
    val coverLetter: String,
    val budget: Int,
    val deadline: String
)

data class AcceptProposalResponse(
    val message: String,
    val contract: Contract
)

// Contract
data class Contract(
    val id: Int,
    @SerializedName("job_listing_id")
    val jobListingId: Int,
    @SerializedName("client_id")
    val clientId: Int,
    @SerializedName("freelance_id")
    val freelanceId: Int,
    val amount: Int,
    val status: String,
    @SerializedName("payment_status")
    val paymentStatus: String,
    val deadline: String?,
    val client: UserResponse?,
    val freelance: UserResponse?,
    @SerializedName("job_listing")
    val jobListing: JobListing?
)

// Message
data class Message(
    val id: Int,
    @SerializedName("contract_id")
    val contractId: Int,
    @SerializedName("sender_id")
    val senderId: Int,
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    val sender: UserResponse?
)

data class SendMessageRequest(
    val content: String
)

data class CreateMessageRequest(
    val content: String
)

// Dashboard
data class DashboardStats(
    @SerializedName("total_missions")
    val totalMissions: Int = 0,
    @SerializedName("open_missions")
    val openMissions: Int = 0,
    @SerializedName("active_contracts")
    val activeContracts: Int = 0,
    @SerializedName("total_spent")
    val totalSpent: Int = 0,
    @SerializedName("total_proposals")
    val totalProposals: Int = 0,
    @SerializedName("pending_proposals")
    val pendingProposals: Int = 0,
    @SerializedName("total_revenue")
    val totalRevenue: Int = 0
)

data class ClientDashboard(
    val stats: DashboardStats,
    val missions: List<JobListing>,
    val contracts: List<Contract>
)

data class FreelanceDashboard(
    val stats: DashboardStats,
    val proposals: List<Proposal>,
    val contracts: List<Contract>
)

// Alias pour compatibilité
typealias ClientDashboardResponse = ClientDashboard
typealias FreelanceDashboardResponse = FreelanceDashboard