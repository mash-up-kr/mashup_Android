package com.mashup.feature.danggn.ranking

import com.mashup.core.common.base.BaseViewModel
import com.mashup.feature.danggn.data.dto.DanggnMemberRankResponse
import com.mashup.feature.danggn.data.repository.DanggnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DanggnRankingViewModel @Inject constructor(
    private val danggnRepository: DanggnRepository
) : BaseViewModel() {
    companion object {
        private const val GENERATION_NUMBER = 13
    }

    private val _mashUpRankingList: MutableStateFlow<List<DanggnMemberRankResponse>> =
        MutableStateFlow(
            listOf()
        )
    val mashUpRankingList = _mashUpRankingList.asStateFlow()

    init {
        mashUpScope {
            updateAllRankingList()
        }
    }

    /**
     * 밖에서도 호출할 수 있도록 private가 아닌 internal로 만들었습니다.
     */
    internal suspend fun updateAllRankingList() {
        val allMemberRankingResult = danggnRepository.getAllDanggnRank(GENERATION_NUMBER)
        if (allMemberRankingResult.isSuccess()) {
            val rankingList = danggnRepository.getAllDanggnRank(GENERATION_NUMBER)
                .data?.allMemberRankList ?: listOf()
            _mashUpRankingList.update { rankingList }
        }
    }
}