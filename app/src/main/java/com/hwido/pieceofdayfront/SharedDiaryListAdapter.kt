package com.hwido.pieceofdayfront

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hwido.pieceofdayfront.databinding.MainSharediaryformatModifyBinding
import com.hwido.pieceofdayfront.datamodel.SharedDiaryItem

class SharedDiaryListAdapter(private val sharedDiaryList: List<SharedDiaryItem>) :
    RecyclerView.Adapter<SharedDiaryListAdapter.SharedDiaryViewHolder>() {

    class SharedDiaryViewHolder(private val binding: MainSharediaryformatModifyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sharedDiaryItem: SharedDiaryItem) {
            // Use Glide to load the image into the ImageView
            Glide.with(binding.root)
                .load(sharedDiaryItem.diaryImage)
                .into(binding.mainSharediaryformatModifyDiaryImage)

            binding.mainSharediaryformatModifyWriter.text = sharedDiaryItem.writer
            binding.mainSharediaryformatModifyDate.text = sharedDiaryItem.date

            // Set any other views or click listeners here
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedDiaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainSharediaryformatModifyBinding.inflate(inflater, parent, false)
        return SharedDiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SharedDiaryViewHolder, position: Int) {
        holder.bind(sharedDiaryList[position])
    }

    override fun getItemCount(): Int {
        return sharedDiaryList.size
    }
}