package com.hwido.pieceofdayfront.myPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.commit
import com.hwido.pieceofdayfront.BluetoothClient.BluetoothClientActivity
import com.hwido.pieceofdayfront.DT.WriteDataRequestTransfer
import com.hwido.pieceofdayfront.R
import com.hwido.pieceofdayfront.ServerAPI.SpringServerAPI
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
    private val  SpringServerCall= SpringServerAPI()

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


        param1?.let {
            SpringServerCall.getMyPage(it, onSuccess = { MyData ->
                // 성공 시 실행될 코드
                //데이터 클래스에 넣어 둔다
//                val transferData =
//                    WriteDataRequestTransfer(longToInt, mycode, title, content, location, weather)
//                val intent = Intent(this, BluetoothClientActivity::class.java)

                //데이터 클래스로 보낸
//                intent.putExtra("codeAndContent", transferData)
//                Log.d("ITMM","${transferData.toString()}")
//                //위치
////                hideProgressBar()
//                startActivity(intent)
                binding.mainMypageEmail.text = MyData.email
                binding.mainMypageIntro.text = MyData.introduce
                binding.mainMypageName.text = MyData.nickName
                val memberID = MyData.memberId!!
                SpringServerCall.getImagePage(11, onSuccess = { MyImage ->

                    binding.profileBasic.setImageBitmap(MyImage)

                },onFailure = {
                    // 실패 시 실행될 코드
                    Toast.makeText(activity, "NONO", Toast.LENGTH_SHORT).show()
                })

            }, onFailure = {
                // 실패 시 실행될 코드
                Toast.makeText(activity, "NONO", Toast.LENGTH_SHORT).show()
            })

        }


        //        binding.profileBasic.setImageResource()





        // Inflate the layout for this fragment
        binding = MainMypageBinding.inflate(inflater, container, false)

        binding.mainMypageSharepageBtn.setOnClickListener {
            navigateToShareList()
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