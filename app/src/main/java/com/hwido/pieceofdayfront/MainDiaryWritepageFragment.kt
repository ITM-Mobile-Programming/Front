package com.hwido.pieceofdayfront

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.hwido.pieceofdayfront.ServerAPI.ServerResponseCallback
import com.hwido.pieceofdayfront.ServerAPI.SpringServerAPI
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBinding
import com.hwido.pieceofdayfront.datamodel.DiaryEntry
import com.hwido.pieceofdayfront.writeNew.MainDiaryWritepageContent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [MainDiaryWritepageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainDiaryWritepageFragment : Fragment() , ServerResponseCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private lateinit var binding: MainDiarywritepageBinding
    private lateinit var diaryAdapter: DiaryAdapter
    private val springServer = SpringServerAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onResume() {
        super.onResume()
        param1?.let {
            Log.d("ITM","들어옴")
            springServer.getDiaryList(it, this)
            Log.d("ITM","나감")
        }
    }

    override fun onSuccessSpringDiaryList(diaryList: List<DiaryEntry>) {
        Log.d("ITM", "리스트 콜백 ${diaryList.reversed()}")
        diaryAdapter.updateData(diaryList.reversed())

        // adapter를 recyclerview에 설정
    }
    override fun onSuccessSpring(ouPutData: String) {
    }

    override fun onSuccessSpring(diaryId: Int, hashTags: String, imageUrl: String) {
    }

    override fun onErrorSpring(error: Throwable) {
        Log.e("ITM", "Error: ${error.message}")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MainDiarywritepageBinding.inflate(inflater, container, false)

        // adapter 빈 list로 기본 설정


        diaryAdapter = DiaryAdapter(emptyList())
        binding.mainDiarywritepageDiary.apply {
            layoutManager = LinearLayoutManager(context) // 여기에서 LinearLayoutManager를 설정합니다.
            adapter = diaryAdapter
        }

        diaryAdapter.itemClick = object: DiaryAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {

                val clickedItem = diaryAdapter.getItem(position)

                val location = clickedItem.location
                val weatherCode = clickedItem.weatherCode
                val content = clickedItem.context
                val hashTags = clickedItem.hashTagList.toString()
                val imageUrl = clickedItem.thumbnailUrl.toString()
                val title = clickedItem.title

                //아이템 데이터 가져와서 여기 레이아웃에 넣어주고 돌린다




                val detailPage = binding.mainShowDetailContents
                binding.mainDiaryformatPopupDiaryName.text = title
                binding.mainDiaryformatPopupDiaryImage
//                binding.mainDiaryformatPopupDate.text =
                binding.mainDiaryformatPopupLocation.text = location
                binding.mainDiaryformatPopupDetail.text = content


                when(weatherCode){
                    "Sunny" -> {
                        binding.mainDiaryformatPopupWeather.setImageResource(R.drawable.sunny)
                    }
                    "LittleCloud" ->{
                        binding.mainDiaryformatPopupWeather.setImageResource(R.drawable.littlecloud)
                    }
                    "Cloud" ->{
                        binding.mainDiaryformatPopupWeather.setImageResource(R.drawable.cloud)
                    }
                    "Rain" ->{
                        binding.mainDiaryformatPopupWeather.setImageResource(R.drawable.rain)
                    }
                    "Snow" ->{
                        binding.mainDiaryformatPopupWeather.setImageResource(R.drawable.snow)
                    }
                    else-> {
                        binding.mainDiaryformatPopupWeather.setImageResource(R.drawable.none)
                    }

                }
//                binding.mainDiaryformatPopupHastag.text = hashTags

                detailPage.isVisible = true
//
            }
        }

        binding.backToLinearpage.setOnClickListener {
            val detailPage = binding.mainShowDetailContents
            detailPage.isVisible = false
        }



        binding.mainDiarywritepageWriteDiary.setOnClickListener {
            navigateToContent()
        }


        return binding.root
    }

    private fun navigateToContent() {
        val intent = Intent(activity, MainDiaryWritepageContent::class.java)
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainDiaryWritepageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            MainDiaryWritepageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}