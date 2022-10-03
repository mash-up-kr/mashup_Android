package com.mashup.ui.mypage

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentMyPageBinding
import com.mashup.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>() {

    private val viewModel: MyPageViewModel by viewModels()

    private val attendanceAdapter by lazy {
        AttendanceListAdapter().apply {
            setOnItemClickListener(object : AttendanceListAdapter.OnItemEventListener {
                override fun onTotalAttendanceClick() {
                    showAttendanceInfoDialog()
                }

                override fun onStartSettingActivity() {
                    context?.let { startActivity(SettingActivity.newIntent(it)) }
                }
            })
        }
    }

    override fun initViews() {
        super.initViews()
        initRecyclerView()
        initSwipeRefresh()
    }

    private fun initSwipeRefresh() {
        viewBinding.layoutSwipe.apply {
            setOnRefreshListener { viewModel.getMember() }
            setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.brand500)
            )
        }
    }

    private fun initRecyclerView() {
        viewBinding.rvMypage.apply {
            adapter = attendanceAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    // 현재 뷰에서 최상단에 보이는 아이템의 위치 (조금이라도 보여도 인식됨)
                    val firstVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (firstVisibleItemPosition == 0) {
                        viewBinding.layoutTitle.visibility = View.GONE
                    } else {
                        viewBinding.layoutTitle.visibility = View.VISIBLE
                        viewBinding.layoutTitle.setOnClickListener {
                            showAttendanceInfoDialog()
                        }
                    }
                }
            })
        }
    }

    override fun initObserves() {
        viewModel.attendanceList.observe(viewLifecycleOwner) { it ->
            viewBinding.layoutSwipe.isRefreshing = false
            attendanceAdapter.submitList(it)
            it.firstOrNull()?.profile?.let {
                viewBinding.tvTitle.text = it.name
                viewBinding.tvNum.text = it.getAttendanceScore()
            }
        }

        flowViewLifecycleScope {
            viewModel.errorCode.collectLatest {
                viewBinding.layoutSwipe.isRefreshing = false
                handleCommonError(it)
            }
        }
    }

    private fun showAttendanceInfoDialog() {
        AttendanceExplainDialog().show(
            childFragmentManager,
            null
        )
    }

    companion object {
        fun newInstance() = MyPageFragment()
    }

    override val layoutId: Int = R.layout.fragment_my_page
}
