package com.hwido.pieceofdayfront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBaseBinding
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageFirstfragmentBinding
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageSecondfragmentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainDiaryWritepageSecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainDiaryWritepageSecondFragment : Fragment() {
    private lateinit var binding: MainDiarywritepageSecondfragmentBinding
    private lateinit var mainbinding: MainDiarywritepageBaseBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        binding = MainDiarywritepageSecondfragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainbinding = (activity as MainDiaryWritepageBase).binding

        val thirdFragment = MainDiaryWritepageThirdFragment()
        val fManager = parentFragmentManager
        binding.mainDiarywritepageSecondfragmentBtn.setOnClickListener {
            fManager.commit {
                replace(mainbinding.baseFrame.id, thirdFragment)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainDiaryWritepageSecondFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainDiaryWritepageSecondFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}