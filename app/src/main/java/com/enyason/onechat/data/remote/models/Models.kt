package com.enyason.onechat.data.remote.models

import com.google.gson.annotations.SerializedName

data class APIResponse<T>(
    val data: T,
    val success: Boolean,
    val message: String,
    val responseCode: String
)

data class AuthInfo(
    val token: Token,
    val user: User
)

data class Token(
    val accessToken: String,
    val refreshToken: String
)

data class User(
    val username: String,
    val email: String,
    @SerializedName("full_name")
    val fullName: String
)


data class Message(
    val id: String,

    val content: String,

    val author: String,

    val room: String,

    @SerializedName("created_at") val createdAt: String

)


data class Room(
    val id: String,

    val name: String,

    val creator: String,

    val room: String,

    @SerializedName("created_at") val createdAt: String

)


data class LoginRequest(
    val email: String,
    val password: String,
    @SerializedName("fcm_token") val token: String
)

data class RegistrationRequest(
    val email: String,
    @SerializedName("full_name") val fullName: String,
    val username: String,
    val password: String
)
