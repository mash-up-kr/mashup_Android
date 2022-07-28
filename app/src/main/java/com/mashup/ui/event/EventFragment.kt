package com.mashup.ui.event

import android.content.Intent
import androidx.fragment.app.viewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentEventBinding
import com.mashup.ui.attendance.platform.PlatformAttendanceActivity
import com.mashup.ui.event.model.Event
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFragment : BaseFragment<FragmentEventBinding>() {

    private val viewModel: EventViewModel by viewModels()


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
            adapter = EventViewPagerAdapter(getTestList()).apply {
                setOnItemClickListener(object : EventViewPagerAdapter.OnItemClickListener {
                    override fun onClickAttendanceList() {
                        startActivity(Intent(context, EventDetailActivity::class.java))
                    }

                    override fun onClickCrewAttendanceActivity() {
                        startActivity(Intent(context, PlatformAttendanceActivity::class.java))
                    }
                })
            }
            offscreenPageLimit = 4
        }
    }

    fun getTestList(): ArrayList<Event> {
        val item = viewModel.getList()
        return arrayListOf(item, item, item)
    }

    override fun initObserves() {
    }

    companion object {
        fun newInstance() = EventFragment()
    }
}