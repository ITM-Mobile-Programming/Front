package com.hwido.pieceofdayfront.myPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hwido.pieceofdayfront.DiaryShareAdapter
import com.hwido.pieceofdayfront.databinding.MainMypageSharelistBinding
import com.hwido.pieceofdayfront.datamodel.DiaryListLoad

class MainMypageSharelist : AppCompatActivity() {
    private lateinit var binding : MainMypageSharelistBinding
    private lateinit var diaryShareAdapter: DiaryShareAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainMypageSharelistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dummyDiaryList: List<DiaryListLoad> = getDummyDiaryList()
        diaryShareAdapter = DiaryShareAdapter(dummyDiaryList)

        binding.mainDiarywritepageDiary.apply {
            layoutManager = LinearLayoutManager(this@MainMypageSharelist)
            adapter = diaryShareAdapter
        }
    }

    private fun getDummyDiaryList() : List<DiaryListLoad> {
        // 여기에 데이터 가져올 것들 수정할 필요 존재
        // 우선은 DiaryListLoad에서 가져오는 것으로 정의해둠
        return listOf(
            DiaryListLoad("Title", "Context", "Location", "Weather Code", "Thumbnail URL", listOf("Tag1", "Tag2", "Tag3"))
        )
    }
}