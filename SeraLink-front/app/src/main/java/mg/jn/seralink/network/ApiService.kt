package mg.jn.seralink.network

import mg.jn.seralink.model.LoginRequest
import mg.jn.seralink.model.LoginResponse
import mg.jn.seralink.model.RegisterRequest
import mg.jn.seralink.model.JobListingResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @POST("logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>

    @GET("jobs")
    suspend fun getJobs(
        @Query("category") category: String? = null,
        @Query("budget_min") budgetMin: Int? = null,
        @Query("budget_max") budgetMax: Int? = null,
        @Query("search") search: String? = null
    ): Response<JobListingResponse>
}