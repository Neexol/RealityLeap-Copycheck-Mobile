package ru.rtuitlab.copycheck.server

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.rtuitlab.copycheck.models.CopycheckResult

interface ServerApi {
    @Multipart
    @POST("/recognize")
    suspend fun recognize(@Part filePart: MultipartBody.Part): CopycheckResult
}