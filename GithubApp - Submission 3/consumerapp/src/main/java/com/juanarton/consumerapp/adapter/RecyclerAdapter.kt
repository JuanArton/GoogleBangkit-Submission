package com.juanarton.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.juanarton.consumerapp.model.DataParcel
import com.juanarton.consumerapp.R
import com.juanarton.consumerapp.databinding.ItemViewBinding

class RecyclerAdapter(private val listData: ArrayList<DataParcel>) :
    RecyclerView.Adapter<RecyclerAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, idx: Int) {
        val list = listData[idx]
        holder.bindData(list)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(list) }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemViewBinding.bind(itemView)

        fun bindData(data: DataParcel){
            with(itemView){
                Glide.with(context)
                    .load(data.anydata)
                    .centerCrop()
                    .into(binding.listPhoto)

                binding.name.text = data.login
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataParcel)
    }
}