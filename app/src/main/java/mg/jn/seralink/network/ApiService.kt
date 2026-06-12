package mg.jn.seralink.network

import mg.jn.seralink.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @POST("logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>

    // Jobs
    @GET("jobs")
    suspend fun getJobs(
        @Query("category") category: String? = null,
        @Query("budget_min") budgetMin: Int? = null,
        @Query("budget_max") budgetMax: Int? = null,
        @Query("search") search: String? = null
    ): Response<JobListingResponse>

    @GET("jobs/{id}")
    suspend fun getJob(@Path("id") id: Int): Response<JobListing>

    @POST("jobs")
    suspend fun createJob(
        @Header("Authorization") token: String,
        @Body job: CreateJobRequest
    ): Response<JobListing>

    @PUT("jobs/{id}")
    suspend fun updateJob(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body job: CreateJobRequest
    ): Response<JobListing>

    @DELETE("jobs/{id}")
    suspend fun deleteJob(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    // Proposals
    @GET("jobs/{jobId}/proposals")
    suspend fun getProposals(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: Int
    ): Response<List<Proposal>>

    @POST("jobs/{jobId}/proposals")
    suspend fun submitProposal(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: Int,
        @Body proposal: CreateProposalRequest
    ): Response<Proposal>

    @POST("proposals/{id}/accept")
    suspend fun acceptProposal(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<AcceptProposalResponse>

    @POST("proposals/{id}/reject")
    suspend fun rejectProposal(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @GET("my-proposals")
    suspend fun getMyProposals(
        @Header("Authorization") token: String
    ): Response<List<Proposal>>

    // Messages
    @GET("contracts/{contractId}/messages")
    suspend fun getMessages(
        @Header("Authorization") token: String,
        @Path("contractId") contractId: Int
    ): Response<List<Message>>

    @POST("contracts/{contractId}/messages")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Path("contractId") contractId: Int,
        @Body message: SendMessageRequest
    ): Response<Message>

    // Dashboard
    @GET("dashboard/freelance")
    suspend fun getDashboardFreelance(
        @Header("Authorization") token: String
    ): Response<FreelanceDashboard>

    @GET("dashboard/client")
    suspend fun getDashboardClient(
        @Header("Authorization") token: String
    ): Response<ClientDashboard>

    // Profile
    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<UserResponse>

    @PUT("profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<UserResponse>

    @GET("users/{id}")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<PublicUserResponse>

    // Contracts
    @GET("contracts")
    suspend fun getContracts(
        @Header("Authorization") token: String
    ): Response<List<Contract>>

    @GET("contracts/{id}")
    suspend fun getContract(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Contract>

    // Contracts actions
    @POST("contracts/{id}/pay")
    suspend fun payContract(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ContractPayResponse>

    @POST("contracts/{id}/complete")
    suspend fun completeContract(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ContractActionResponse>

    @POST("contracts/{id}/release")
    suspend fun releaseContract(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ContractActionResponse>

    @POST("contracts/{id}/sign")
    suspend fun signContract(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ContractActionResponse>

    @GET("contracts/{id}/pdf")
    @Streaming
    suspend fun downloadContractPdf(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<okhttp3.ResponseBody>

    @POST("contracts/{id}/dispute")
    suspend fun disputeContract(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ContractActionResponse>

    @GET("users/{userId}/reviews")
    suspend fun getUserReviews(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<UserReviewsResponse>

    @POST("contracts/{contractId}/reviews")
    suspend fun submitReview(
        @Header("Authorization") token: String,
        @Path("contractId") contractId: Int,
        @Body review: ReviewRequest
    ): Response<Review>
}