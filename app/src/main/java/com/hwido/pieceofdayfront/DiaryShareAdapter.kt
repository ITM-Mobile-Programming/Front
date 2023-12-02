package com.hwido.pieceofdayfront

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hwido.pieceofdayfront.databinding.MainDiaryformatBinding
import com.hwido.pieceofdayfront.databinding.MainSharediaryformatBinding
import com.hwido.pieceofdayfront.datamodel.DiaryListLoad

class DiaryShareAdapter(private val diaryItems: List<DiaryListLoad>) : RecyclerView.Adapter<DiaryShareAdapter.DiaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainSharediaryformatBinding.inflate(inflater, parent, false)
        return DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val diaryItem = diaryItems[position]
        holder.bind(diaryItem)
    }

    override fun getItemCount(): Int {
        return diaryItems.size
    }

    inner class DiaryViewHolder(private val binding: MainSharediaryformatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(diaryItem: DiaryListLoad) {
            binding.apply {
                // 날짜 관련 data 필요
                //mainSharediaryformatModifyDate.text = diaryItem.

                Glide.with(root.context)
                    .load(diaryItem.thumbnailUrl)
                    .into(mainSharediaryformatDiaryImage)
            }
        }
    }
}