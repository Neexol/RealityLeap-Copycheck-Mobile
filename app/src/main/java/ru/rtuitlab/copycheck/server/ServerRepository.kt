package ru.rtuitlab.copycheck.server

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.rtuitlab.copycheck.models.CopycheckResult
import ru.rtuitlab.copycheck.server.handle.Resource
import ru.rtuitlab.copycheck.server.handle.ResponseHandler
import ru.rtuitlab.copycheck.utils.SongResult
import java.lang.Exception
import java.util.concurrent.TimeUnit

object ServerRepository {

    private val responseHandler = ResponseHandler()

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    private val api: ServerApi =  Retrofit.Builder()
        .baseUrl("http://copycheck.herokuapp.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create()

    suspend fun checkSong(fileName: String, songByteArray: ByteArray) = try {
        val filePart = MultipartBody.Part.createFormData(
            "file",
            fileName,
            songByteArray.toRequestBody(
                "audio/*".toMediaTypeOrNull(),
                0, songByteArray.size
            )
        )
        responseHandler.handleSuccess(SongResult.Recognized(api.recognize(filePart)))
    } catch (e: Exception) {
        if (e is HttpException && e.code() == 404) {
            responseHandler.handleSuccess(SongResult.NotRecognized(fileName))
        } else {
            responseHandler.handleException(e)
        }
    }
}