package com.mashup.ui.event

import android.content.Context
import android.content.Intent
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityEventDetailBinding
import com.mashup.ui.model.EventDetail

class EventDetailActivity : BaseActivity<ActivityEventDetailBinding>() {
    override val layoutId = R.layout.activity_event_detail

    val test = listOf<EventDetail>(
        EventDetail(0, 0, "1부", ""),
        EventDetail(0, 1, "안드로이드 팀 세미나", "일반 본문 텍스트 스타일입니다."),
        EventDetail(0, 1, "안드로이드 팀 세미나", "일반 본문 텍스트 스타일입니다."),
        EventDetail(0, 1, "안드로이드 팀 세미나", "일반 본문 텍스트 스타일입니다."),
        EventDetail(0, 1, "안드로이드 팀 세미나", "일반 본문 텍스트 스타일입니다."),
        EventDetail(0, 1, "안드로이드 팀 세미나", "일반 본문 텍스트 스타일입니다."),
        EventDetail(0, 0, "1부", ""),
        EventDetail(0, 1, "안드로이드 팀 세미나", "일반 본문 텍스트 스타일입니다."),
        EventDetail(0, 1, "안드로이드 팀 세미나", "일반 본문 텍스트 스타일입니다."),
        EventDetail(0, 1, "안드로이드 팀 세미나", "일반 본문 텍스트 스타일입니다."),
        EventDetail(0, 1, "안드로이드 팀 세미나", "일반 본문 텍스트 스타일입니다."),
        EventDetail(0, 1, "안드로이드 팀 세미나", "일반 본문 텍스트 스타일입니다."),
    )

    private val eventDetailAdapter by lazy {
        EventDetailAdapter().apply {
            setOnItemClickListener(object : EventDetailAdapter.OnItemEventListener {
                override fun onExitEventClick() {
                }
            })
        }
    }

    override fun initViews() {
        viewBinding.rvEvent.apply {
            adapter = eventDetailAdapter
            eventDetailAdapter.submitList(test)
        }
    }

    companion object {
        fun start(context: Context?, action: Intent.() -> Unit = {}) {
            val intent = Intent(context, EventDetailActivity::class.java).apply(action)
            context?.startActivity(intent)
        }
    }

}
