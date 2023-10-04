package com.mashup.ui.mypage

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
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
                ContextCompat.getColor(requireContext(), com.mashup.core.common.R.color.brand500)
            )
        }
    }

    private fun initRecyclerView() {
        viewBinding.rvMypage.adapter = attendanceAdapter
    }

    override fun initObserves() {
        viewModel.attendanceList.observe(viewLifecycleOwner) {
            viewBinding.layoutSwipe.isRefreshing = false
            attendanceAdapter.submitList(it)
        }

        flowViewLifecycleScope {
            viewModel.errorCode.collectLatest {
                viewBinding.layoutSwipe.isRefreshing = false
                handleCommonError(it)
            }
        }
    }

    private fun showAttendanceInfoDialog() {
        AttendanceExplainDialog().show(childFragmentManager, AttendanceExplainDialog::class.simpleName)
    }

    companion object {
        fun newInstance() = MyPageFragment()
    }

    override val layoutId: Int = R.layout.fragment_my_page
}
