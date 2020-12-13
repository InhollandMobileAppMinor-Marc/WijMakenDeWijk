package nl.woonwaard.wij_maken_de_wijk.api

import nl.woonwaard.wij_maken_de_wijk.api.models.LoginResponse
import nl.woonwaard.wij_maken_de_wijk.api.models.RegistrationResponse
import nl.woonwaard.wij_maken_de_wijk.api.models.StatusResponse
import nl.woonwaard.wij_maken_de_wijk.domain.models.*
import retrofit2.Response
import retrofit2.http.*

interface WmdwApiSpecification {
    @POST("register")
    suspend fun register(
        @Body registration: Registration
    ): Response<RegistrationResponse>

    @POST("login")
    suspend fun login(
        @Body credentials: Credentials
    ): Response<LoginResponse>

    @GET("status")
    suspend fun getStatus(
        @Header("Authorization") authorization: String? = null
    ): Response<StatusResponse>

    @GET("posts")
    suspend fun getPosts(
        @Header("Authorization") authorization: String,
        @Query("categories") categories: String? = null,
        @Query("inlineAuthor") inlineAuthor: Boolean = true,
        @Query("inlineComments") inlineComments: Boolean = false
    ): Response<Set<Post>>

    @GET("posts/{id}")
    suspend fun getPostById(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Query("inlineAuthor") inlineAuthor: Boolean = true,
        @Query("inlineComments") inlineComments: Boolean = false
    ): Response<Post>

    @POST("posts")
    suspend fun addPost(
        @Header("Authorization") authorization: String,
        @Body createdPost: CreatedPost,
        @Query("inlineAuthor") inlineAuthor: Boolean = true
    ): Response<Post>

    @GET("posts/{postId}/comments")
    suspend fun getCommentsForPost(
        @Header("Authorization") authorization: String,
        @Path("postId") postId: String,
        @Query("inlineAuthor") inlineAuthor: Boolean = true
    ): Response<Set<Comment>>

    @GET("comments/{id}")
    suspend fun getCommentById(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Query("inlineAuthor") inlineAuthor: Boolean = true,
        @Query("inlinePost") inlinePost: Boolean = false
    ): Response<Comment>

    @POST("posts/{postId}/comments")
    suspend fun addCommentToPost(
        @Header("Authorization") authorization: String,
        @Path("postId") postId: String,
        @Body createdComment: CreatedComment,
        @Query("inlineAuthor") inlineAuthor: Boolean = true
    ): Response<Comment>

    @GET("users")
    suspend fun getUsers(
        @Header("Authorization") authorization: String
    ): Response<Set<User>>

    @GET("users/{id}")
    suspend fun getUserById(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Response<User>

    @DELETE("notifications")
    suspend fun getNewNotifications(
        @Header("Authorization") authorization: String,
        @Query("inlineComments") inlineComments: Boolean = true,
        @Query("inlineAuthor") inlineAuthor: Boolean = true,
        @Query("inlinePost") inlinePost: Boolean = true
    ): Response<Set<Notification>>
}
