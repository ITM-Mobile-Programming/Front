package com.hwido.pieceofdayfront

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBinding
import com.hwido.pieceofdayfront.databinding.MainMainpageBinding
import com.hwido.pieceofdayfront.login.LoginMainpage
import com.hwido.pieceofdayfront.myPage.MainMypageSharelist
import com.hwido.pieceofdayfront.writeNew.MainDiaryWritepageContent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainDiaryWritepageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainDiaryWritepageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: MainDiarywritepageBinding
    private lateinit var diaryAdapter: DiaryAdapter
    private val springServer = SpringServerAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    //    getDiaryList//리썸에 넣어준다

    //    onSuccess 가져와야함

    override fun onResume() {
        super.onResume()
        // 프래그먼트가 다시 활성화될 때 수행할 작업

        //엑세스 토큰 넣어주고
        //프레그 먼트 만들어줄떄 param1에 access token 넣어준다
//        springServer.getDiaryList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MainDiarywritepageBinding.inflate(inflater, container, false)


        // adapter 빈 list로 기본 설정
        diaryAdapter = DiaryAdapter(emptyList())

        // adapter를 recyclerview에 설정
        binding.mainDiarywritepageDiary.adapter = diaryAdapter

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
        fun newInstance(param1: String, param2: String) =
            MainDiaryWritepageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}