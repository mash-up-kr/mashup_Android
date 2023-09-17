package com.mashup.ui.mypage

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentMyPageBinding
import com.mashup.feature.mypage.profile.model.ProfileCardData
import com.mashup.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>() {
    override val layoutId: Int = R.layout.fragment_my_page

    private val viewModel: MyPageViewModel by viewModels()

    private val attendanceAdapter by lazy {
        AttendanceListAdapter().apply {
            setOnItemClickListener(object : AttendanceListAdapter.OnItemEventListener {
                override fun onTotalAttendanceClick() {
                    showAttendanceInfoDialog()
                }

                override fun onStartSettingActivity() {
                    startActivity(SettingActivity.newIntent(requireContext()))
                }

                override fun onStartEditProfileActivity() {
                    startActivity(MyProfileEditActivity.newIntent(requireContext()))
                }

                override fun onStartEditProfileCardActivity(card: ProfileCardData) {
                    startActivity(MyProfileCardEditActivity.newIntent(requireContext(), card))
                }
            })
        }
    }

    override fun initViews() {
        super.initViews()
        initSwipeRefresh()
        initRecyclerView()
    }

    private fun initSwipeRefresh() {
        viewBinding.layoutSwipe.apply {
            setOnRefreshListener { viewModel.getMyPageData() }
            setColorSchemeColors(
                ContextCompat.getColor(requireContext(), com.mashup.core.common.R.color.brand500)
            )
        }
    }

    private fun initRecyclerView() {
        viewBinding.rvMypage.adapter = attendanceAdapter
    }

    override fun initObserves() {
        flowViewLifecycleScope {
            viewModel.myPageData.collectLatest {
                viewBinding.layoutSwipe.isRefreshing = false
                attendanceAdapter.submitList(it)
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
        AttendanceExplainDialog().show(childFragmentManager, AttendanceExplainDialog::class.simpleName)
    }

    companion object {
        fun newInstance() = MyPageFragment()
    }
}
