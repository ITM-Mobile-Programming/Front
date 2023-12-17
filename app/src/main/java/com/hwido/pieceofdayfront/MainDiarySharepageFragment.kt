package com.hwido.pieceofdayfront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.hwido.pieceofdayfront.databinding.MainDiarysharepageBinding
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBinding

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
        binding = MainDiarysharepageBinding.inflate(inflater, container, false)

        // Replace the content of main_diarysharepage_baseframe with MainDiarySharepageDiaryListFragment by default
        if (savedInstanceState == null) {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_diarysharepage_baseframe, MainDiarySharepageDiaryListFragment.newInstance("arg1", "arg2"))
            transaction.addToBackStack(null) // Optional: Adds the transaction to the back stack
            transaction.commit()
        }

        // Find the button in your layout
        val diaryListButton: Button = binding.mainDiarysharepageDiarylistbtn

        // Set a click listener for the button
        diaryListButton.setOnClickListener {
            // Replace the content of main_diarysharepage_baseframe with MainDiarySharepageDiaryListFragment
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_diarysharepage_baseframe, MainDiarySharepageDiaryListFragment.newInstance("arg1", "arg2"))
            transaction.addToBackStack(null) // Optional: Adds the transaction to the back stack
            transaction.commit()
        }

        // Find the button in your layout
        val friendListButton: Button = binding.mainDiarysharepageDiarylistbtn

        // Set a click listener for the button
        friendListButton.setOnClickListener {
            // Replace the content of main_diarysharepage_baseframe with MainDiarySharepageFriendListFragment
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_diarysharepage_baseframe, MainDiarySharepageFriendListFragment.newInstance("arg1", "arg2"))
            transaction.addToBackStack(null) // Optional: Adds the transaction to the back stack
            transaction.commit()
        }

        return binding.root
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