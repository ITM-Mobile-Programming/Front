package com.hwido.pieceofdayfront.myPage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.commit
import com.hwido.pieceofdayfront.R
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBinding
import com.hwido.pieceofdayfront.databinding.MainListpageBinding
import com.hwido.pieceofdayfront.databinding.MainMypageBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainMypageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainMypageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private lateinit var binding: MainMypageBinding

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
        binding = MainMypageBinding.inflate(inflater, container, false)

        binding.mainMypageSharepageBtn.setOnClickListener {
            navigateToShareList()
        }

        binding.mainMypageMbtipageBtn.setOnClickListener {
            navigateToMbti()
        }

        binding.mainMypageFriendlistpageBtn.setOnClickListener {
            navigateToFriendList()
        }

        return binding.root
    }

    private fun navigateToShareList() {
        val intent = Intent(activity, MainMypageSharelist::class.java)
        startActivity(intent)
    }

    private fun navigateToMbti() {
        val intent = Intent(activity, MainMypageMBTI::class.java)
        startActivity(intent)
    }

    private fun navigateToFriendList() {
        val intent = Intent(activity, MainMypageFriendList::class.java)
        startActivity((intent))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainMypageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            MainMypageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}