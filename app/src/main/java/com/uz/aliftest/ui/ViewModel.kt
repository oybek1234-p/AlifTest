package com.uz.aliftest.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.uz.aliftest.data.AppDatabase
import com.uz.aliftest.data.Post
import com.uz.aliftest.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(
    private val appDatabase: AppDatabase,
    private val repository: Repository
    ) :
    androidx.lifecycle.ViewModel() {

    private val list = ArrayList<Post>()
    val posts = MutableLiveData<ArrayList<Post>>()
    val loading = MutableLiveData(false)

    private fun updateList() {
        posts.postValue(list)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteMessage(message: Post) {
        viewModelScope.launch (Dispatchers.IO){
            appDatabase.messageDao().delete(message)
        }
        list.removeIf { it.id == message.id }
        updateList()
    }

    fun loadList() {
        if (loading.value == true) return
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val cache = appDatabase.messageDao().getAllList()
            if (cache.isEmpty()) {
                repository.loadList {
                    appDatabase.messageDao().insertAll(it)
                    launch(Dispatchers.Main) {
                        list.addAll(it)
                        loading.postValue(false)
                        updateList()
                    }
                }
            } else {
                loading.postValue(false)
                list.addAll(cache)
                updateList()
            }
        }
    }
}