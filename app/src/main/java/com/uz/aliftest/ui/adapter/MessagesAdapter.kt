package com.uz.aliftest.ui.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uz.aliftest.R
import com.uz.aliftest.data.Post
import com.uz.aliftest.databinding.MessageItemBinding
import com.uz.aliftest.loadImage
import com.uz.aliftest.visibleOrGone

class MessagesAdapter(private val onLongClick: (post: Post, pos: Int) -> Unit) :
    ListAdapter<Post, ViewHolderMy<MessageItemBinding>>(messageDiff) {
    private var inflater: LayoutInflater? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        inflater = LayoutInflater.from(recyclerView.context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderMy<MessageItemBinding> {
        val binding = DataBindingUtil.inflate<MessageItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.message_item,
            parent,
            false
        )
        val h = ViewHolderMy(binding)
        binding.apply {
            root.setOnLongClickListener {
                onLongClick.invoke(getItem(h.layoutPosition), h.layoutPosition)
                return@setOnLongClickListener true
            }
        }
        return h
    }

    private fun getRandomImage() = fakeImages[(0 until fakeImages.size).random()]

    private val photos = arrayListOf<Pair<Int,String>>()

    override fun submitList(list: MutableList<Post>?) {
        super.submitList(list)

        if (!list.isNullOrEmpty()) {
            fakePhotos.forEach {
                val random = (0 until list.size.coerceIn(0,10)).random()
                photos.add(Pair(random,it))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolderMy<MessageItemBinding>, position: Int) {
        holder.apply {
            binding.apply {
                val post = getItem(position)
                titleView.text = post.title
                messageView.text = post.body
                userPhView.loadImage(getRandomImage(),true)

                var set = false

                photos.forEach {
                    if (it.first == position) {
                        set = true
                        photoView.loadImage(it.second)
                    }
                }
                photoView.visibleOrGone(set)
            }
        }
    }
}