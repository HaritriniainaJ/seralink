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
}