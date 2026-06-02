package mg.jn.seralink.model

import com.google.gson.annotations.SerializedName

// Auth
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String,
    val role: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

// User
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val bio: String?,
    val avatar: String?,
    val location: String?,
    val phone: String?
)

// Job
data class JobListing(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    @SerializedName("budget_min")
    val budgetMin: Long,
    @SerializedName("budget_max")
    val budgetMax: Long,
    @SerializedName("budget_type")
    val budgetType: String,
    val status: String,
    val deadline: String?,
    val client: User?
)

// Pagination
data class JobListingResponse(
    val data: List<JobListing>,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("last_page")
    val lastPage: Int,
    val total: Int
)

// Contract
data class Contract(
    val id: Int,
    @SerializedName("job_listing_id")
    val jobListingId: Int,
    val amount: Long,
    val status: String,
    @SerializedName("payment_status")
    val paymentStatus: String,
    val deadline: String?
)