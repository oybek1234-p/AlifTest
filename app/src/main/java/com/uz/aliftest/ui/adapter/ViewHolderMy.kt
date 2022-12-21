package com.uz.aliftest.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class ViewHolderMy<T : ViewDataBinding>(val binding: T) :
    RecyclerView.ViewHolder(binding.root)