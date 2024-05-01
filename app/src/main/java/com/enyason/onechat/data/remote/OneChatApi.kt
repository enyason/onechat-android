package com.enyason.onechat.data.remote

import com.enyason.onechat.data.remote.models.APIResponse
import com.enyason.onechat.data.remote.models.AuthInfo
import com.enyason.onechat.data.remote.models.LoginRequest
import com.enyason.onechat.data.remote.models.Message
import com.enyason.onechat.data.remote.models.RegistrationRequest
import com.enyason.onechat.data.remote.models.Room
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface OneChatApi {

    @GET("api/chat/rooms/{room_id}messages")
    suspend fun getRoomMessages(@Query("room_id") roomId: String): APIResponse<List<Message>>

    @GET("api/chat/rooms")
    suspend fun getRooms(): APIResponse<List<Room>>

    @POST("api/auth")
    suspend fun login(@Body loginRequest: LoginRequest): APIResponse<AuthInfo>

    @POST("api/users")
    suspend fun register(@Body registrationRequest: RegistrationRequest): APIResponse<Unit>
}
