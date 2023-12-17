package com.hwido.pieceofdayfront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hwido.pieceofdayfront.databinding.MainDiarysharepagediarylistBinding
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBinding
import com.hwido.pieceofdayfront.DT.SharedDiaryItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainDiarySharepageDiaryListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainDiarySharepageDiaryListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: MainDiarysharepagediarylistBinding
    private lateinit var sharedDiaryAdapter: SharedDiaryListAdapter

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
        binding = MainDiarysharepagediarylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy data for testing
        val dummyData = listOf(
            SharedDiaryItem("dummy_image1",  "Writer 1", "Date 1"),
            SharedDiaryItem("dummy_image2",  "Writer 2", "Date 2"),
            SharedDiaryItem("dummy_image3",  "Writer 3", "Date 3")
        )

        // Initialize the adapter with the dummy data
        sharedDiaryAdapter = SharedDiaryListAdapter(dummyData)

        // Set up RecyclerView
        binding.mainDiarysharepagediarylistList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = sharedDiaryAdapter
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainDiarySharepageDiaryListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainDiarySharepageDiaryListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}