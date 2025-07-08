package com.dev.focusnotes.data.source.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotesApiService {

    @GET("notes")
    suspend fun getNotes(): List<NetworkNote>

    @POST("notes")
    suspend fun saveNote(@Body note: NetworkNote): NetworkNote
}
