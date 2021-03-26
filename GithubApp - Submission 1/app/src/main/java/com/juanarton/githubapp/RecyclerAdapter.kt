package com.juanarton.githubapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.juanarton.githubapp.databinding.ItemViewBinding

class RecyclerAdapter(private val listData: ArrayList<DataParcel>, private val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.GridViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, idx: Int) {
        val list = listData[idx]
        holder.bindData(list)

        holder.itemView.setOnClickListener{
            val moveWithObjectIntent = Intent(context, UserProfile::class.java)
            moveWithObjectIntent.putExtra(UserProfile.EXTRA_MYDATA, list)
            context.startActivity(moveWithObjectIntent)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class GridViewHolder(iView: View): RecyclerView.ViewHolder(iView) {
        private val binding = ItemViewBinding.bind(iView)
        fun bindData(data: DataParcel){
            val id = context.resources.getIdentifier(data.avatar,"drawable", context.packageName)
            Glide.with(context)
                .load(id)
                .centerCrop()
                .into(binding.listPhoto)

            binding.name.text = data.nama
            binding.userName.text = data.username
        }
    }
}