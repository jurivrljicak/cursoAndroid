package com.example.cursoandroidclase2.data.repository

import com.example.cursoandroidclase2.data.api.RetrofitClient
import com.example.cursoandroidclase2.data.model.Post

class PostRepository {
    suspend fun fetchPosts(): List<Post> = RetrofitClient.api.getPosts()
}