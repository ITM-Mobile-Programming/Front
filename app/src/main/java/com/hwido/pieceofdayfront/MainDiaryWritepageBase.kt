package com.hwido.pieceofdayfront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBaseBinding

class MainDiaryWritepageBase : AppCompatActivity(), MainDiaryWritepageThirdFragment.OnFragmentDestroyedListener {
    val binding by lazy { MainDiarywritepageBaseBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val firstFragment = MainDiaryWritepageFirstFragment()

        val fManager = supportFragmentManager
        fManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            add(binding.baseFrame.id, firstFragment)
        }
    }

    override fun onFragmentDestroyed() {
        // This method will be called when the third fragment is destroyed
        val intent = Intent(this, MainDiaryWritepage::class.java)
        startActivity(intent)
        finish() // If you want to finish the current activity
    }
}