package com.mashup.ui.event

import androidx.fragment.app.viewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentEventBinding
import com.mashup.ui.model.Event

class EventFragment : BaseFragment<FragmentEventBinding>() {

    private val viewModel: EventViewModel by viewModels()


    val list = arrayListOf(
        Event("정기세미나", "1차아"),
        Event("안드팀 특별세미나", "다양한 세미나를 만나보세요"),
        Event("안오스팀 특별세미나", "다양한 세미나를 만나보세요")
    )

    override val layoutId: Int = R.layout.fragment_event
    override fun initViews() {
        super.initViews()
        viewBinding.vp.apply {
            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
            val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pagerWidth)
            setPageTransformer { page, position ->
                page.translationX = ((position) * -(pageMarginPx + pagerWidth))
                page.scaleY = 1 - (0.1f * kotlin.math.abs(position))
            }
            adapter = EventViewPagerAdapter(list).apply {
                setOnItemClickListener(object : EventViewPagerAdapter.OnItemClickListener {
                    override fun onClickAttendanceList() {
                    }
                })
            }
            offscreenPageLimit = 4
        }
    }

    override fun initObserves() {
    }

    companion object {
        fun newInstance() = EventFragment()
    }
}