package com.mashup.feature.danggn.ranking

import com.mashup.core.model.data.local.DanggnPreference
import com.mashup.core.model.data.local.UserPreference
import com.mashup.datastore.data.repository.DanggnPreferenceRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.fake.FakeDanggnDao
import com.mashup.feature.danggn.data.repository.DanggnRepository
import com.mashup.util.CoroutineRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

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

    private val defaultUserPreferenceRepository = mock<UserPreferenceRepository> {
        on { getUserPreference() } doReturn flowOf(UserPreference.getDefaultInstance())
    }

    private val defaultDanggnPreferenceRepository = mock<DanggnPreferenceRepository> {
        on { getDanggnPreference() } doReturn flowOf(DanggnPreference.getDefaultInstance())
    }

    @Test
    fun `DanggnMemberRankResponse 올바르게 PlatformRanking으로 변환되는지 테스트`() {
        // given
        rankingViewModel = DanggnRankingViewModel(
            danggnRepository = DanggnRepository(fakeRankingDao),
            userPreferenceRepository = defaultUserPreferenceRepository,
            danggnPreferenceRepository = defaultDanggnPreferenceRepository
        )

        // when
        coroutineRule.testDispatcher.scheduler.advanceTimeBy(3000L)

        val list = listOf(
            DanggnRankingViewModel.RankingItem.PlatformRanking(
                "39", "정종노드", 150
            ),
            DanggnRankingViewModel.RankingItem.PlatformRanking(
                "40", "정종드투", 151
            ),
            DanggnRankingViewModel.RankingItem.PlatformRanking(
                "41", "정종민", 152
            ),
            DanggnRankingViewModel.RankingItem.PlatformRanking(
                "42", "정종웹", 153
            ),
            DanggnRankingViewModel.RankingItem.PlatformRanking(
                "43", "정종오스", 154
            ),
            DanggnRankingViewModel.RankingItem.PlatformRanking(
                "44", "정종자인", 155
            ),
        )
        // then
        assertEquals(rankingViewModel.uiState.value.personalRankingList, list)
    }
}