package com.mashup.feature.danggn.ranking

import app.cash.turbine.test
import com.mashup.datastore.model.DanggnPreference
import com.mashup.datastore.model.UserPreference
import com.mashup.datastore.data.repository.DanggnPreferenceRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.fake.FakeDanggnDao
import com.mashup.feature.danggn.data.repository.DanggnRepository
import com.mashup.util.CoroutineRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
    fun `DanggnMemberRankResponse 올바르게 PlatformRanking으로 변환되는지 테스트`() = runTest {
        // given
        rankingViewModel = DanggnRankingViewModel(
            danggnRepository = DanggnRepository(fakeRankingDao),
            userPreferenceRepository = defaultUserPreferenceRepository,
            danggnPreferenceRepository = defaultDanggnPreferenceRepository
        )

        coroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        val list = listOf(
            DanggnRankingViewModel.RankingItem.Ranking(
                "39",
                "정종노드",
                150
            ),
            DanggnRankingViewModel.RankingItem.Ranking(
                "40",
                "정종드투",
                151
            ),
            DanggnRankingViewModel.RankingItem.Ranking(
                "41",
                "정종민",
                152
            ),
            DanggnRankingViewModel.RankingItem.Ranking(
                "42",
                "정종웹",
                153
            ),
            DanggnRankingViewModel.RankingItem.Ranking(
                "43",
                "정종오스",
                154
            ),
            DanggnRankingViewModel.RankingItem.Ranking(
                "44",
                "정종자인",
                155
            )
        )

        // then
        rankingViewModel.uiState.test {
            assertEquals(
                awaitItem().personalRankingList,
                emptyList<DanggnRankingViewModel.RankingItem>()
            )
            assertEquals(
                awaitItem().personalRankingList.filter { it !is DanggnRankingViewModel.RankingItem.EmptyRanking },
                list
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}
