package com.hwido.pieceofdayfront.myPage

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.hwido.pieceofdayfront.FriendListAdapter
import com.hwido.pieceofdayfront.R
import com.hwido.pieceofdayfront.ServerAPI.SpringServerAPI
import com.hwido.pieceofdayfront.databinding.MainMypagefriendlistBinding
import com.hwido.pieceofdayfront.login.LoginMainpage

class MainMypageFriendList : AppCompatActivity() {

    private lateinit var binding: MainMypagefriendlistBinding
    private lateinit var friendListAdapter: FriendListAdapter

    val sharedPreferences: SharedPreferences by lazy {
        val masterKeyAlias = MasterKey
            .Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()


        EncryptedSharedPreferences.create(
            applicationContext,
            LoginMainpage.FILE_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainMypagefriendlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val friendListRecyclerView = binding.mainMypagefriendlistList

        // Create an instance of your FriendListAdapter
        friendListAdapter = FriendListAdapter()

        // Set the adapter to the RecyclerView
        friendListRecyclerView.adapter = friendListAdapter

        // Optionally, set a layout manager
        friendListRecyclerView.layoutManager = LinearLayoutManager(this)

        val accessToken = sharedPreferences.getString(LoginMainpage.app_JWT_token, "access").toString()

        fetchFriendList(accessToken)
    }
    private fun fetchFriendList(accessToken: String) {
        SpringServerAPI().getFriendList(
            accessToken = accessToken,
            onSuccess = { friendDataList ->
                Log.d("ITM", "Successfully fetch friend list")
                friendListAdapter.setFriendList(friendDataList)
            },
            onFailure = {
                Log.d("ITM", "Failed to fetch friend list")
            }
        )
    }


}