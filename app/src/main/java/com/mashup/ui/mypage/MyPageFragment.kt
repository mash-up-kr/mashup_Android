package com.mashup.ui.mypage

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
import com.mashup.ui.model.AttendanceModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>() {

    private val viewModel: MyPageViewModel by viewModels()

    private val attendanceAdapter by lazy {
        MyPageAttendanceListAdapter().apply {
            setOnItemClickListener(object : MyPageAttendanceListAdapter.OnItemEventListener {
                override fun onItemClick(id: AttendanceModel) {
                }
            })
        }
    }


    override val layoutId: Int = R.layout.fragment_my_page

    override fun initViews() {
        super.initViews()
        val bottomSheetView =
            layoutInflater.inflate(R.layout.fragment_my_page_attendance_info, null)
        context?.let {
            val bottomSheetDialog = BottomSheetDialog(it)
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetDialog.show()
//            bottomSheetDialog.dismiss()
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
                        (activity as MainActivity).updateStatusBarColor("#F8F7FC")
                    } else {
                        viewBinding.layoutTitle.visibility = View.VISIBLE
                        (activity as MainActivity).updateStatusBarColor("#FFFFFF")
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

}