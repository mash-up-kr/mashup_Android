package com.mashup

import androidx.lifecycle.SavedStateHandle
import com.mashup.constant.EXTRA_SCHEDULE_ID
import com.mashup.data.repository.AttendanceRepository
import com.mashup.fake.FakeAttendanceDao
import com.mashup.ui.attendance.platform.PlatformAttendanceViewModel
import com.mashup.util.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PlatformAttendanceViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val fakeAttendanceDao = FakeAttendanceDao()
    private val savedStateHandle = SavedStateHandle()

    private lateinit var platformAttendanceViewModel: PlatformAttendanceViewModel

    @Before
    fun setup() {
        savedStateHandle[EXTRA_SCHEDULE_ID] = 0
        platformAttendanceViewModel = PlatformAttendanceViewModel(
            AttendanceRepository(fakeAttendanceDao),
            savedStateHandle
        )
    }

    @Test
    fun `when eventNum is 0, notice message is 아직 일정 시작 전이예요`() {
        // Given
        fakeAttendanceDao.eventNum = 0

        // When
        platformAttendanceViewModel.getPlatformAttendanceList()
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(platformAttendanceViewModel.notice.value, "아직 일정 시작 전이예요.")
    }

    @Test
    fun `when isEnd is false, notice message is 출석체크가 실시간으로 진행되고 있어요`() {
        // Given
        fakeAttendanceDao.isEnd = false
        fakeAttendanceDao.eventNum = 1

        // When
        platformAttendanceViewModel.getPlatformAttendanceList()
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(platformAttendanceViewModel.notice.value, "출석체크가 실시간으로 진행되고 있어요")
    }

    @Test
    fun `when isEnd is true & eventNum is 1, notice message is 1부 출석이 완료되었어요`() {
        // Given
        fakeAttendanceDao.isEnd = true
        fakeAttendanceDao.eventNum = 1

        // When
        platformAttendanceViewModel.getPlatformAttendanceList()
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(platformAttendanceViewModel.notice.value, "1부 출석이 완료되었어요.")
    }

    @Test
    fun `when isEnd is true & eventNum is 2, notice message is 출석체크가 완료되었어요`() {
        // Given
        fakeAttendanceDao.isEnd = true
        fakeAttendanceDao.eventNum = 2

        // When
        platformAttendanceViewModel.getPlatformAttendanceList()
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(platformAttendanceViewModel.notice.value, "출석체크가 완료되었어요")
    }

    @Test
    fun `when exceptional case, notice message is 서버에서 이상한 일이 발생했어요 ㅜ`() {
        // Given
        fakeAttendanceDao.isEnd = false
        fakeAttendanceDao.eventNum = 3

        // When
        platformAttendanceViewModel.getPlatformAttendanceList()
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(platformAttendanceViewModel.notice.value, "서버에서 이상한 일이 발생했어요 ㅜ")
    }
}
