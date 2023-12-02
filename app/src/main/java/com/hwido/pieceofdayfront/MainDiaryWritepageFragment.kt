package com.hwido.pieceofdayfront

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBinding
import com.hwido.pieceofdayfront.databinding.MainMainpageBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MainDiarywritepageBinding.inflate(inflater, container, false)

        binding.mainDiarywritepageWriteDiary.setOnClickListener {
            navigateToContent()
        }

        return binding.root
    }

    private fun navigateToContent() {
        val intent = Intent(activity, MainDiaryWritepageContent::class.java)
        startActivity(intent)
    }
    
    // 여기에 다이어리 작성 버튼 누르면, MainDiaryWritepageContent로 activty intent 이루어져야 한다

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