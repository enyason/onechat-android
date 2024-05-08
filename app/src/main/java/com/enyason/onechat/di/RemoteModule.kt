package com.enyason.onechat.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.enyason.onechat.data.ApplicationDataStore
import com.enyason.onechat.data.remote.OneChatApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {


    @Provides
    fun provideHttpClient(dataStore: ApplicationDataStore): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor)
            .addInterceptor { chain ->
                val token = dataStore.getToken()?.accessToken
                println("Token chain = $token")
                val newRequest = chain.request().newBuilder().apply {
                    addHeader("accept", "application/json")
                    token?.let {
                        addHeader("Authorization", "Bearer $token")
                    }
                }.build()
                chain.proceed(newRequest)
            }
            .build()
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().create()
    }

    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("one_chat_shared_pref", MODE_PRIVATE)
    }

    @Provides
    fun provideOneChatApi(okHttpClient: OkHttpClient): OneChatApi {
        return Retrofit.Builder().client(okHttpClient)
            .baseUrl("https://onechat-api.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(OneChatApi::class.java)
    }

}
