package com.mashup.ui.mypage

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentMyPageBinding
import com.mashup.ui.main.MainActivity
import com.mashup.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>() {

    private val viewModel: MyPageViewModel by viewModels()
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val attendanceAdapter by lazy {
        AttendanceListAdapter().apply {
            setOnItemClickListener(object : AttendanceListAdapter.OnItemEventListener {
                override fun onTotalAttendanceClick() {
                    bottomSheetDialog.show()
                }

                override fun onStartSettingActivity() {
                    startActivity(Intent(context, SettingActivity::class.java))
                }
            })
        }
    }

    override fun initViews() {
        super.initViews()
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_attendance_info, null)
        context?.let {
            bottomSheetDialog = BottomSheetDialog(it)
            bottomSheetDialog.apply {
                setContentView(bottomSheetView)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            (activity as MainActivity).updateStatusBarColor(it.getColor(R.color.gray950))
        }
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
                            bottomSheetDialog.show()
                        }
                    }
                    /*
                    수동 다음 아이템 당겨오기
                        val lastVisibleItemPosition =
                             (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                         val itemTotalCount = recyclerView.adapter!!.itemCount - 2 // 어댑터에 등록된 아이템의 총 개수 -1
                         if (lastVisibleItemPosition >= itemTotalCount) {
                             viewModel.getItem(++page)
                         }
                     */
                }
            })
        }
    }

    override fun initObserves() {
        viewModel.attendanceList.observe(viewLifecycleOwner) { it ->
            attendanceAdapter.submitList(it)
        }
    }

    companion object {
        fun newInstance() = MyPageFragment()
    }

    override val layoutId: Int = R.layout.fragment_my_page
}