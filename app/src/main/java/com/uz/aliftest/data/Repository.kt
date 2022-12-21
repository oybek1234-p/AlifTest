package com.uz.aliftest.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import org.json.JSONArray
import org.json.JSONObject

class Repository {

    private val ktorClient = HttpClient(OkHttp)

    private val apiUrl = "https://jsonplaceholder.typicode.com/posts"

    private fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
        when (val value = this[it]) {
            is JSONArray -> {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }

            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else -> value
        }
    }

    suspend fun loadList(result: (list: ArrayList<Post>) -> Unit) {
        val body = ktorClient.get(apiUrl).body<String>()
        val jsonArray = JSONArray(body)
        val list = arrayListOf<Post>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i) ?: return
            val it = jsonObject.toMap()
            val id = it["id"]
            val userId = it["userId"]
            val title = it["title"]
            val b = it["body"]
            val post = Post()
            post.id = id.toString()
            post.userId = userId.toString()
            post.body = b.toString()
            post.title = title.toString()
            list.add(post)
        }
        result.invoke(list)
    }
}