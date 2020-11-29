package ru.rtuitlab.copycheck.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.rtuitlab.copycheck.models.CopycheckResult
import ru.rtuitlab.copycheck.server.ServerRepository
import ru.rtuitlab.copycheck.server.handle.Resource
import ru.rtuitlab.copycheck.utils.SingleLiveEvent
import ru.rtuitlab.copycheck.utils.SongResult
import ru.rtuitlab.copycheck.utils.fileName


class MainViewModel(app: Application) : AndroidViewModel(app) {

    var selectedUri: Uri? = null
        set(value) {
            fileName = value.fileName()
            field = value
        }
    var fileName = ""
        private set

    fun uploadFile() {
        val uri = selectedUri ?: return
        val songByteArray = getApplication<Application>().contentResolver.openInputStream(uri)!!.readBytes()

        viewModelScope.launch {
            _songResultResource.value = Resource.loading()
            val response = withContext(Dispatchers.IO) {
                ServerRepository.checkSong(fileName, songByteArray)
            }
            _songResultResource.value = response
        }
    }

    private val _songResultResource = SingleLiveEvent<Resource<SongResult>>()
    val songResultResource: LiveData<Resource<SongResult>> = _songResultResource

    fun resetUriSelect() {
        selectedUri = null
    }

    lateinit var selectedCopycheckResult: CopycheckResult
}