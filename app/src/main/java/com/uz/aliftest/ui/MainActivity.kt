package com.uz.aliftest.ui

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.uz.aliftest.data.AppDatabase
import com.uz.aliftest.data.Repository
import com.uz.aliftest.databinding.ActivityMainBinding
import com.uz.aliftest.showSnackBar
import com.uz.aliftest.toast
import com.uz.aliftest.ui.adapter.MessagesAdapter
import com.uz.aliftest.visibleOrGone


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: MessagesAdapter
    private lateinit var database: AppDatabase
    private lateinit var repository: Repository

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        database =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database").build()
        repository = Repository()

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ViewModel(database, repository) as T
            }
        })[ViewModel::class.java]

        binding.apply {
            myAdapter = MessagesAdapter { p, pos ->
                //On long click ->
                AlertDialog.Builder(root.context)
                    .setTitle("Do you want delete this post?")
                    .setMessage("You cant restore it again")
                    .setPositiveButton(
                        "DELETE"
                    ) { _, _ ->
                        viewModel.deleteMessage(p)
                    }.show()
            }
            recyclerView.apply {
                adapter = myAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)

                val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
                    ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT
                    ) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                        val p = myAdapter.currentList[viewHolder.layoutPosition]
                        viewModel.deleteMessage(p)
                        showSnackBar("Deleted")
                    }
                }
                ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(this)
            }

            viewModel.apply {
                //Если fragment поставим lifecycleScope
                loading.observeForever {
                    progressBar.visibleOrGone(it)
                }
                posts.observeForever {
                    myAdapter.submitList(it.toMutableList())
                }
                viewModel.loadList()
            }
        }
    }
}