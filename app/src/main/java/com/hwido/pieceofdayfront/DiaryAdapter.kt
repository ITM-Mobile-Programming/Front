package com.hwido.pieceofdayfront

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hwido.pieceofdayfront.databinding.MainDiaryformatBinding
import com.hwido.pieceofdayfront.datamodel.DiaryEntry

class DiaryAdapter(private var diaryItems: List<DiaryEntry>) :
    RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainDiaryformatBinding.inflate(inflater, parent, false)
        return DiaryViewHolder(binding)
    }

    fun updateData(newDiaryList: List<DiaryEntry>) {
        diaryItems = newDiaryList
        notifyDataSetChanged()
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

        fun bind(diaryItem: DiaryEntry) {
            binding.apply {
                // mbti 관련 data 있어야 함
                //mbtiTextView.text = diaryItem.weatherCode

                // 날씨는 어떻게 표현? 현재 image로 표시하는데 이거 text로 변환?
                // 텍스트 받고 이미지 가져올 것임
                //mainDiaryformatWeather. = diaryItem.weather

                mainDiaryformatLocation.text = diaryItem.location
                mainDiaryformatDiaryName.text = diaryItem.title


                mainDiaryformatHastag.text = "${diaryItem.hashTagList[0].hashTag}, ${diaryItem.hashTagList[1].hashTag},${diaryItem.hashTagList[2].hashTag}"

                // 날짜 관련 data 필요
                //mainDiaryformatDate.text = diaryItem.

                Log.d("ITM","${diaryItem.thumbnailUrl}")
                Glide.with(root.context)
                    .load(diaryItem.thumbnailUrl)
                    .fitCenter()
                    .into(mainDiaryformatDiaryImage)

            }
        }
    }
}