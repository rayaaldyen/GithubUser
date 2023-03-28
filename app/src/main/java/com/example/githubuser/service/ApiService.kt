package com.example.githubuser.service

import com.example.githubuser.DetailUser
import com.example.githubuser.DisplayUser
import com.example.githubuser.GithubResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun searchUsername(
        @Query("q") q: String

    ): Call<GithubResponse>
    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(@Path("username") username: String): Call<DetailUser>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<DisplayUser>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<DisplayUser>>


//    fun postReview(
//        @Field("id") id: String,
//        @Field("name") name: String,
//        @Field("review") review: String
//    ): Call<PostReviewResponse>
}