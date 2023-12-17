package com.hwido.pieceofdayfront

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hwido.pieceofdayfront.ServerAPI.ServerResponseCallback
import com.hwido.pieceofdayfront.ServerAPI.SpringServerAPI
import com.hwido.pieceofdayfront.databinding.MainListpageBinding
import com.hwido.pieceofdayfront.datamodel.DiaryEntry
import com.hwido.pieceofdayfront.datamodel.DiaryListLoad
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"


/**
 * A simple [Fragment] subclass.
 * Use the [MainListpageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainListpageFragment : Fragment(), ServerResponseCallback {
    private var param1: String? = null
    private lateinit var binding: MainListpageBinding
    private val springServer = SpringServerAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onResume() {
        super.onResume()
        param1?.let { authToken ->
            Log.d("ITM","들어옴")
            springServer.getDiaryList(authToken, this)
            Log.d("ITM","나감")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MainListpageBinding.inflate(inflater, container, false)

        // 객체 생성
        val dayText: TextView = binding.mainMainpageDay
        val calendarView: CalendarView = binding.mainMainpageCalendar

        // 날짜 형태
        val dateFormat: DateFormat = SimpleDateFormat("yyyy년 MM월 dd일")

        updateDisplayedDate(calendarView.date)


        // 날짜 변환
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            // 날짜 변수에 담기
            val selectedDate  = "${year}년${month+1}월${dayOfMonth}일"

            updateDisplayedDate(calendarView.date)

            // 현재 날짜
            dayText.text = selectedDate
            Log.d("mainpage","${dayText.text}")

            // 변수를 텍스트뷰에 담아준다
            updateDiaryEntries(selectedDate)
        }

        return binding.root
    }

    private fun updateDisplayedDate(selectedDateInMillis: Long) {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy년MM월dd일")
        val date = Date(selectedDateInMillis)
        binding.mainMainpageDay.text = dateFormat.format(date)
    }

    private fun updateDiaryEntries(selectedDate: String) {
        getDateDiary(selectedDate)
    }

    private fun getDateDiary(selectedDate: String) {
        param1?.let {
            springServer.getDateDiary(it, selectedDate, onSuccess = { diaryEntries ->
                Log.d("ITM", "Successfully fetched date diary entries")
                displayDiaryEntriesForDate(diaryEntries)
            }, onFailure = {
                Log.e("ITM", "Failed to fetch date diary entries")
            })
        }
    }

    private fun getWeatherImageResource(weatherCode: String?): Int {
        return when (weatherCode) {
            "Sunny" -> R.drawable.sunny
            "LittleCloud" -> R.drawable.littlecloud
            "Cloud" -> R.drawable.cloud
            "Rain" -> R.drawable.rain
            "Snow" -> R.drawable.snow
            else -> R.drawable.none
        }
    }

    private fun displayDiaryEntriesForDate(diaryEntries: DiaryListLoad) {
        val locationTextView = binding.mainMainpageDiaryView.findViewById<TextView>(R.id.main_diaryformat_modify_location)
        val titleTextView = binding.mainMainpageDiaryView.findViewById<TextView>(R.id.main_diaryformat_modify_diaryName)
        val thumbnailImageView = binding.mainMainpageDiaryView.findViewById<ImageView>(R.id.main_diaryformat_modify_diaryImage)
        val weatherImageView = binding.mainMainpageDiaryView.findViewById<ImageView>(R.id.main_diaryformat_modify_weather)
        //val hashtagTextView = binding.mainMainpageDiaryView.findViewById<TextView>(R.id.main_diaryformat_modify_hastag)


        val firstEntry = diaryEntries // Assuming you're showing details of the first diary entry
        locationTextView.text = firstEntry.location
        titleTextView.text = firstEntry.title

        //Log.d("Glide", "DiaryEntry: $firstEntry")
        Log.d("Glide", "Loading image from URL: ${firstEntry.thumbnailUrl}")
        // image load
        // Image load
        firstEntry.thumbnailUrl?.let { url ->
            Log.d("Glide", "Loading image from URL: $url")
            Glide.with(requireContext())
                .load(url)
                .error(R.drawable.snow)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("Glide", "Error loading image", e)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("Glide", "Image loaded successfully")
                        return false
                    }
                })
                .into(thumbnailImageView)
        }


        firstEntry.weatherCode?.let {
            weatherImageView.setImageResource(getWeatherImageResource(it))
        }

        // Build a string representation of hashtags
        //val hashtagString = firstEntry.hashTagList.joinToString(", ") { hashtag -> hashtag.hashTag }
        //hashtagTextView.text = hashtagString
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainListpageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            MainListpageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onSuccessSpringDiaryList(diaryList: List<DiaryEntry>) {
        Log.d("ITM", "리스트 콜백 ${diaryList.reversed()}")

    }
    override fun onSuccessSpring(ouPutData: String) {
    }

    override fun onSuccessSpring(diaryId: Int, hashTags: String, imageUrl: String) {
    }

    override fun onErrorSpring(error: Throwable) {
        Log.e("ITM", "Error: ${error.message}")
    }
}