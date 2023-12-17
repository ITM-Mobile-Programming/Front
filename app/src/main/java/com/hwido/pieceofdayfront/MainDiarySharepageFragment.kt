package com.hwido.pieceofdayfront

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hwido.pieceofdayfront.BluetoothServer.BluetoothServerActivity
import com.hwido.pieceofdayfront.ServerAPI.SpringServerAPI
import com.hwido.pieceofdayfront.databinding.MainDiarysharepageBinding
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBinding
import com.hwido.pieceofdayfront.writeNew.MainDiaryWritepageContent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainDiarySharepageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainDiarySharepageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private lateinit var binding: MainDiarysharepageBinding
    private lateinit var diaryAdapter: DiaryAdapter
    private val springServer = SpringServerAPI()
    private var id : Int? = null

    override fun onResume() {
        super.onResume()
        param1?.let {
            Log.d("ITM","들어옴")
            param1?.let { it1 ->
                // 여기를
                springServer.getSharedDiary(it1,  onSuccess = { diaryList ->
                    // 성공 시 실행될 코드
                    Log.d("ITM", "리스트 콜백 ${diaryList.reversed()}")
                    diaryAdapter.updateData(diaryList.reversed())
                }, onFailure = {
                    // 실패 시 실행될 코드
                    Toast.makeText(activity, "NONO", Toast.LENGTH_SHORT).show()
                })
            }
            Log.d("ITM","나감")
        }
    }



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
        binding = MainDiarysharepageBinding.inflate(inflater, container, false)


        diaryAdapter = DiaryAdapter(emptyList())
        binding.mainDiarysharepageDiary.apply {
            layoutManager = LinearLayoutManager(context) // 여기에서 LinearLayoutManager를 설정합니다.
            adapter = diaryAdapter
        }

        diaryAdapter.itemClick = object: DiaryAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {

                val clickedItem = diaryAdapter.getItem(position)

                val location = clickedItem.location
                val weatherCode = clickedItem.weatherCode
                val content = clickedItem.context
                Log.d("ITM_write", "$content")
                val hashTags =
                    "${clickedItem.hashTagList.get(0).hashTag}, ${clickedItem.hashTagList.get(1).hashTag}, ${
                        clickedItem.hashTagList.get(2).hashTag
                    }"
                val imageUrl = clickedItem.thumbnailUrl.toString()
                val title = clickedItem.title
                id = clickedItem.diaryId


                //아이템 데이터 가져와서 여기 레이아웃에 넣어주고 돌린다
                val detailPage = binding.mainShowDetailContents
                binding.mainDiaryformatPopupDiaryName.text = title
                binding.mainDiaryformatPopupLocation.text = location
                binding.mainDiaryformatPopupDetail.text = content
                binding.mainDiarywriteformatPopupHastag.text = hashTags
                Glide.with(this@MainDiarySharepageFragment)
                    .load(imageUrl)
                    .fitCenter()
                    .into(binding.mainDiarywriteformatPopupDiaryImage)


                when (weatherCode) {
                    "Sunny" -> {
                        binding.mainDiaryformatPopupWeather.setImageResource(R.drawable.sunny)
                    }

                    "LittleCloud" -> {
                        binding.mainDiaryformatPopupWeather.setImageResource(R.drawable.littlecloud)
                    }

                    "Cloud" -> {
                        binding.mainDiaryformatPopupWeather.setImageResource(R.drawable.cloud)
                    }

                    "Rain" -> {
                        binding.mainDiaryformatPopupWeather.setImageResource(R.drawable.rain)
                    }

                    "Snow" -> {
                        binding.mainDiaryformatPopupWeather.setImageResource(R.drawable.snow)
                    }

                    else -> {
                        binding.mainDiaryformatPopupWeather.setImageResource(R.drawable.none)
                    }

                }
//                binding.mainDiaryformatPopupHastag.text = hashTags

                detailPage.isVisible = true
//
            }
        }



        binding.getSharedDiary.setOnClickListener {
            navigateToContentServer()
        }
        return binding.root
    }


    private fun navigateToContentServer() {
        //여기서
        val intent = Intent(activity, BluetoothServerActivity::class.java)
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainDiarySharepageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            MainDiarySharepageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}