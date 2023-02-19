package com.mashup

import androidx.lifecycle.SavedStateHandle
import com.mashup.constant.EXTRA_SCHEDULE_ID
import com.mashup.data.repository.AttendanceRepository
import com.mashup.fake.FakeAttendanceDao
import com.mashup.ui.attendance.platform.PlatformAttendanceViewModel
import com.mashup.util.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PlatformAttendanceViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Test
    fun `when eventNum is 0, notice message is 아직 일정 시작 전이예요`() {
        // Given
        val fakeAttendanceDao = FakeAttendanceDao().also {
            it.eventNum = 0
        }

        val savedStateHandle = SavedStateHandle().apply {
            set(EXTRA_SCHEDULE_ID, 0)
        }

        val viewModel = PlatformAttendanceViewModel(
            AttendanceRepository(fakeAttendanceDao),
            savedStateHandle
        )

        // When
        viewModel.getPlatformAttendanceList()
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(viewModel.notice.value, "아직 일정 시작 전이예요.")
    }

    @Test
    fun `when isEnd is false, notice message is 출석체크가 실시간으로 진행되고 있어요`() {
        // Given
        val fakeAttendanceDao = FakeAttendanceDao().also {
            it.isEnd = false
            it.eventNum = 1
        }

        val savedStateHandle = SavedStateHandle().apply {
            set(EXTRA_SCHEDULE_ID, 0)
        }

        val viewModel = PlatformAttendanceViewModel(
            AttendanceRepository(fakeAttendanceDao),
            savedStateHandle
        )

        // When
        viewModel.getPlatformAttendanceList()
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(viewModel.notice.value, "출석체크가 실시간으로 진행되고 있어요")
    }

    @Test
    fun `when isEnd is true & eventNum is 1, notice message is 1부 출석이 완료되었어요`() {
        // Given
        val fakeAttendanceDao = FakeAttendanceDao().also {
            it.isEnd = true
            it.eventNum = 1
        }

        val savedStateHandle = SavedStateHandle().apply {
            set(EXTRA_SCHEDULE_ID, 0)
        }

        val viewModel = PlatformAttendanceViewModel(
            AttendanceRepository(fakeAttendanceDao),
            savedStateHandle
        )

        // When
        viewModel.getPlatformAttendanceList()
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(viewModel.notice.value, "1부 출석이 완료되었어요.")
    }

    @Test
    fun `when isEnd is true & eventNum is 2, notice message is 출석체크가 완료되었어요`() {
        // Given
        val fakeAttendanceDao = FakeAttendanceDao().also {
            it.isEnd = true
            it.eventNum = 2
        }

        val savedStateHandle = SavedStateHandle().apply {
            set(EXTRA_SCHEDULE_ID, 0)
        }

        val viewModel = PlatformAttendanceViewModel(
            AttendanceRepository(fakeAttendanceDao),
            savedStateHandle
        )

        // When
        viewModel.getPlatformAttendanceList()
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(viewModel.notice.value, "출석체크가 완료되었어요")
    }

    @Test
    fun `when exceptional case, notice message is 서버에서 이상한 일이 발생했어요 ㅜ`() {
        // Given
        val fakeAttendanceDao = FakeAttendanceDao().also {
            it.isEnd = false
            it.eventNum = 3
        }

        val savedStateHandle = SavedStateHandle().apply {
            set(EXTRA_SCHEDULE_ID, 0)
        }

        val viewModel = PlatformAttendanceViewModel(
            AttendanceRepository(fakeAttendanceDao),
            savedStateHandle
        )

        // When
        viewModel.getPlatformAttendanceList()
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Then
        assertEquals(viewModel.notice.value, "서버에서 이상한 일이 발생했어요 ㅜ")
    }
}
