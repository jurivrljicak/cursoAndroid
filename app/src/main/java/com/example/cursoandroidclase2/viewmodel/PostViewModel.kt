package com.example.cursoandroidclase2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cursoandroidclase2.data.api.RetrofitClient
import com.example.cursoandroidclase2.data.model.Post
import com.example.cursoandroidclase2.data.repository.PostRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import java.io.IOException

class PostViewModel : ViewModel() {
    private val repository = PostRepository()
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadPosts (context: Context) {
        Log.d("PostViewModel", "Inicializando ViewModel")

        if (RetrofitClient.isNetworkAvailable(context)) {
            viewModelScope.launch {
                try {
                    _posts.value = repository.fetchPosts()
                } catch (e: JsonSyntaxException) {
                    _error.value = "Error de formato de datos: ${e.message}"
                } catch (e: IOException) {
                    _error.value = "Error de red: ${e.message}"
                }
                catch (e: Exception) {
                    Log.e("PostViewModel", "Error: ${e.message}")
                    _error.value = "PostViewModel Error: ${e.message}"
                }
            }
        } else {
            println("🚫 No hay conexión a Internet")
            _error.value = "🚫 No hay conexión a Internet"
        }
    }
}

