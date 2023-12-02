package com.hwido.pieceofdayfront

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hwido.pieceofdayfront.databinding.MainDiaryformatBinding
import com.hwido.pieceofdayfront.datamodel.DiaryListLoad

class DiaryAdapter(private val diaryItems: List<DiaryListLoad>) :
    RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainDiaryformatBinding.inflate(inflater, parent, false)
        return DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val diaryItem = diaryItems[position]
        holder.bind(diaryItem)
    }

    override fun getItemCount(): Int {
        return diaryItems.size
    }

    inner class DiaryViewHolder(private val binding: MainDiaryformatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(diaryItem: DiaryListLoad) {
            binding.apply {
                // mbti 관련 data 있어야 함
                //mbtiTextView.text = diaryItem.weatherCode

                // 날씨는 어떻게 표현? 현재 image로 표시하는데 이거 text로 변환?
                //mainDiaryformatWeather. = diaryItem.weather

                mainDiaryformatLocation.text = diaryItem.location
                mainDiaryformatDiaryName.text = diaryItem.title
                mainDiaryformatHastag.text = diaryItem.hashTags?.joinToString(", ") ?: ""

                // 날짜 관련 data 필요
                //mainDiaryformatDate.text = diaryItem.

                Glide.with(root.context)
                    .load(diaryItem.thumbnailUrl)
                    .into(mainDiaryformatDiaryImage)
            }
        }
    }
}