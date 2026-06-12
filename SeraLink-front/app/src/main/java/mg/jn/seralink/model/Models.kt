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

data class PublicUserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val avatar: String? = null,
    val bio: String? = null,
    val skills: String? = null
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
    val client: UserResponse?,
    @SerializedName("proposals_count")
    val proposalsCount: Int? = null,
    @SerializedName("created_at")
    val createdAt: String? = null
)

data class JobListingResponse(
    val data: List<JobListing>,
    val total: Int = 0,
    @SerializedName("current_page")
    val currentPage: Int = 1,
    @SerializedName("last_page")
    val lastPage: Int = 1
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
    val coverLetter: String? = null,
    val budget: Int,
    val deadline: String? = null,
    val status: String,
    val freelance: UserResponse? = null,
    @SerializedName("job_listing")
    val jobListing: JobListing? = null,
    @SerializedName("contract_id")
    val contractId: Int? = null,
    val job: JobListing? = null,
    @SerializedName("freelancer")
val freelancer: UserResponse? = null,
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
    val jobListing: JobListing?,
    @SerializedName("client_signed")
    val clientSigned: Boolean = false,
    @SerializedName("freelance_signed")
    val freelanceSigned: Boolean = false
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
    val sender: UserResponse?,
    @SerializedName("sender_type")
    val senderType: String? = null,
    @SerializedName("is_from_me")
    val isFromMe: Boolean = false
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
    val missions: List<JobListing> = emptyList(),
    val contracts: List<Contract> = emptyList(),
    @SerializedName("recent_proposals")
    val recentProposals: List<Proposal> = emptyList()
) {
    val totalJobs: Int get() = stats.totalMissions
    val totalProposalsReceived: Int get() = stats.totalProposals
    val activeContracts: Int get() = stats.activeContracts
    val totalSpent: Int get() = stats.totalSpent
    val jobs: List<JobListing> get() = missions
}

data class FreelanceDashboard(
    val stats: DashboardStats,
    val proposals: List<Proposal> = emptyList(),
    val contracts: List<Contract> = emptyList()
) {
    val totalProposals: Int get() = stats.totalProposals
    val activeContracts: Int get() = stats.activeContracts
    val pendingProposals: Int get() = stats.pendingProposals
    val totalEarnings: Int get() = stats.totalRevenue
}

// Profile
data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val bio: String? = null,
    val skills: String? = null
)

data class ContractPayResponse(
    val message: String,
    @SerializedName("client_secret")
    val clientSecret: String,
    val contract: Contract
)

data class ContractActionResponse(
    val message: String,
    val contract: Contract
)

// Alias
typealias ClientDashboardResponse = ClientDashboard
typealias FreelanceDashboardResponse = FreelanceDashboard