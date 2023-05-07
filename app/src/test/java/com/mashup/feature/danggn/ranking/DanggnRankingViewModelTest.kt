package com.mashup.feature.danggn.ranking

import com.mashup.core.model.data.local.UserPreference
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.fake.FakeDanggnDao
import com.mashup.feature.danggn.data.repository.DanggnRepository
import com.mashup.util.CoroutineRule
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
    private val mockUserPreferenceRepository: UserPreferenceRepository = mockk()

    @Before
    fun setUp() {
        every { mockUserPreferenceRepository.getUserPreference() } returns flowOf(UserPreference.getDefaultInstance())

        rankingViewModel = DanggnRankingViewModel(
            danggnRepository = DanggnRepository(fakeRankingDao),
            userPreferenceRepository = mockUserPreferenceRepository
        )
    }

    @Test
    fun `데이터가 잘들어오는지에 대한 테스트`() {
        // given

        coroutineRule.testDispatcher.scheduler.runCurrent()
        // when
        val list = listOf(
            DanggnRankingViewModel.RankingUiState.Ranking(
                "39", "정종노드", 150
            ),
            DanggnRankingViewModel.RankingUiState.Ranking(
                "40", "정종드투", 151
            ),
            DanggnRankingViewModel.RankingUiState.Ranking(
                "41", "정종민", 152
            ),
            DanggnRankingViewModel.RankingUiState.Ranking(
                "42", "정종웹", 153
            ),
            DanggnRankingViewModel.RankingUiState.Ranking(
                "43", "정종오스", 154
            ),
            DanggnRankingViewModel.RankingUiState.Ranking(
                "44", "정종자인", 155
            ),
        )
        // then
        assertEquals(rankingViewModel.mashUpRankingList.value.take(6), list)
    }
}