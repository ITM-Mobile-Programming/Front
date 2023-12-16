package com.hwido.pieceofdayfront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hwido.pieceofdayfront.databinding.MainDiarysharepagefriendlistBinding
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainDiarySharepageFriendListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainDiarySharepageFriendListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: MainDiarysharepagefriendlistBinding
    private lateinit var friendListAdapter: FriendListAdapter

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
        binding = MainDiarysharepagefriendlistBinding.inflate(inflater, container, false)

        // Assuming you have a RecyclerView in your layout with id main_diarysharepagefriendlist_list
        val friendListRecyclerView: RecyclerView = binding.root.findViewById(R.id.main_diarysharepagefriendlist_list)

        // Create an instance of your FriendListAdapter
        friendListAdapter = FriendListAdapter()

        // Set some dummy data
        val dummyFriendList = listOf("Friend 1", "Friend 2", "Friend 3", "Friend 4", "Friend 5") // Replace with your actual dummy data
        friendListAdapter.setFriendList(dummyFriendList)

        // Set the adapter to the RecyclerView
        friendListRecyclerView.adapter = friendListAdapter

        // Optionally, set a layout manager
        friendListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainDiarySharepageFriendListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainDiarySharepageFriendListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}