package com.uz.aliftest.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.uz.aliftest.data.Post

var messageDiff = object : DiffUtil.ItemCallback<Post>() {
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.body == newItem.body
    }

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }
}