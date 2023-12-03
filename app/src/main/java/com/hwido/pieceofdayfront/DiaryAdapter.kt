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

//                1) Sunny 2) LittleCloud 3)Cloud 4)Rain 5) Snow


                val ee = diaryItem.weatherCode
                Log.d("ITM" , "날씨 $ee") // 폼 고처야함 good으로 나온다
                when(diaryItem.weatherCode){
                    "Sunny" -> {
                        mainDiaryformatWeather.setImageResource(R.drawable.sunny)
                    }
                    "LittleCloud" ->{
                        mainDiaryformatWeather.setImageResource(R.drawable.littlecloud)
                    }
                    "Cloud" ->{
                        mainDiaryformatWeather.setImageResource(R.drawable.cloud)
                    }
                    "Rain" ->{
                        mainDiaryformatWeather.setImageResource(R.drawable.rain)
                    }
                    "Snow" ->{
                        mainDiaryformatWeather.setImageResource(R.drawable.snow)
                    }
                    else-> {
                        mainDiaryformatWeather.setImageResource(R.drawable.none)
                    }

                }


//                val weatherCategory = if((pty?.category?.equals("PTY") == true )&& (sky?.category.equals("SKY"))){
//                    when(pty.fcstValue){
//                        "0"-> {
//                            when(sky?.fcstValue){
//                                "1"-> { "Sunny"}
//                                "2" -> {"LittleCloud"}
//                                else->{
//                                    "Cloud"
//                                }
//
//                            }
//                        }
//                        "1", "2","5","6" -> {"Rain"}
//                        else -> {
//                            "Snow"
//                        }
//                    }
//                }else{
//                    "해당하는 시간, 정보가 아닙니다"
//                }




                var hashTag : String = ""
                for ( i in 0..<diaryItem.hashTagList.size){
                    hashTag += diaryItem.hashTagList[i].hashTag
                }

                mainDiaryformatHastag.text = hashTag

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