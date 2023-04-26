package com.mashup.feature.danggn.ranking

import com.mashup.fake.FakeDanggnDao
import com.mashup.feature.danggn.data.dto.DanggnMemberRankResponse
import com.mashup.feature.danggn.data.repository.DanggnRepository
import com.mashup.util.CoroutineRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * 경축
 * 첫 TEST
 */
@ExperimentalCoroutinesApi
class DanggnRankingViewModelTest {
    @get:Rule
    val coroutineRule = CoroutineRule()
    private val fakeRankingDao = FakeDanggnDao()

    private lateinit var rankingViewModel: DanggnRankingViewModel

    @Before
    fun setUp() {
        rankingViewModel = DanggnRankingViewModel(
            DanggnRepository(fakeRankingDao)
        )
    }

    @Test
    fun `데이터가 잘들어오는지에 대한 테스트`() {
        // given

        coroutineRule.testDispatcher.scheduler.runCurrent()
        // when
        val list = listOf(
            DanggnMemberRankResponse(
                39, "정종노드", 150
            ),
            DanggnMemberRankResponse(
                40, "정종드투", 151
            ),
            DanggnMemberRankResponse(
                41, "정종민", 152
            ),
            DanggnMemberRankResponse(
                42, "정종웹", 153
            ),
            DanggnMemberRankResponse(
                43, "정종오스", 154
            ),
            DanggnMemberRankResponse(
                44, "정종자인", 155
            ),
        )
        // then
        assertEquals(rankingViewModel.mashUpRankingList.value, list)
    }
}