package com.hwido.pieceofdayfront

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import com.hwido.pieceofdayfront.ServerAPI.ServerApiService
import com.hwido.pieceofdayfront.ServerAPI.SpringServerAPI
import com.hwido.pieceofdayfront.databinding.MainListpageBinding
import com.hwido.pieceofdayfront.databinding.MainMainpageBinding
import com.hwido.pieceofdayfront.datamodel.DiaryEntry
import com.hwido.pieceofdayfront.datamodel.getDiaryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
class MainListpageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private lateinit var binding: MainListpageBinding
    private lateinit var diaryResponse: getDiaryResponse
    private val springServer = SpringServerAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
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

        // date 타입
        val date = Date(calendarView.date)

        // 현재 날짜
        dayText.text = dateFormat.format(date)
        Log.d("mainpage","${dayText.text}")

        // 날짜 변환
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            // 날짜 변수에 담기
            val selectedDate  = "${year}년 ${month+1}월 ${dayOfMonth}일"

            // 변수를 텍스트뷰에 담아준다
            updateDiaryEntries(selectedDate)
        }

        return binding.root
    }

    private fun updateDiaryEntries(selectedDate: String) {
        // TODO: Implement logic to fetch diary entries for the selected date
        // Example: You may have a function in your data source to get diary entries
        getDiaryEntries(selectedDate)
    }

    private fun getDiaryEntries(selectedDate: String) {
        // TODO: Call your API or data source to fetch diary entries
        // Example: Replace this with your actual API call using Retrofit or other networking libraries
        val retrofit = Retrofit.Builder()
            .baseUrl("your_base_url")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ServerApiService::class.java)

        apiService.getDiaryList(selectedDate).enqueue(object : Callback<getDiaryResponse> {
            override fun onResponse(
                call: Call<getDiaryResponse>,
                response: Response<getDiaryResponse>
            ) {
                if (response.isSuccessful) {
                    diaryResponse = response.body() ?: getDiaryResponse(0, "", emptyList())
                    updateDiaryView()
                } else {
                    // Handle error
                    Log.e("MainListpageFragment", "Error fetching diary entries: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<getDiaryResponse>, t: Throwable) {
                // Handle failure
                Log.e("MainListpageFragment", "Failed to fetch diary entries: ${t.message}")
            }
        })
    }

    private fun updateDiaryView() {
        // TODO: Update the UI with the fetched diary entries
        // Use diaryResponse.data to access the list of DiaryEntry objects
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
}