package nl.woonwaard.wij_maken_de_wijk.api

import nl.woonwaard.wij_maken_de_wijk.api.models.*
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

    @PATCH("credentials")
    suspend fun updateEmail(
        @Header("Authorization") authorization: String,
        @Body credentials: EmailUpdateRequest
    ): Response<UpdatedCredentialsResponse>

    @PATCH("credentials")
    suspend fun updatePassword(
        @Header("Authorization") authorization: String,
        @Body credentials: PasswordUpdateRequest
    ): Response<UpdatedCredentialsResponse>

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

    @POST("posts/{id}/reports")
    suspend fun reportPost(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Response<Unit>

    @POST("posts/{id}/votes")
    suspend fun addVoteToPost(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Response<Unit>

    @DELETE("posts/{id}")
    suspend fun deletePostById(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Response<Unit>

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

    @POST("comments/{id}/reports")
    suspend fun reportComment(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Response<Unit>

    @DELETE("comments/{id}")
    suspend fun deleteCommentById(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Response<Unit>

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

    @DELETE("users/{id}")
    suspend fun deleteUserById(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Response<Unit>

    @DELETE("notifications")
    suspend fun getNewNotifications(
        @Header("Authorization") authorization: String,
        @Query("inlineComments") inlineComments: Boolean = true,
        @Query("inlineAuthor") inlineAuthor: Boolean = true,
        @Query("inlinePost") inlinePost: Boolean = true
    ): Response<Set<Notification>>
}
