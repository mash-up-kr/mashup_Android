package com.mashup.common.utils.keyboard

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

class TranslateDeferringInsetsAnimationCallback(
    private val view: View, // 애니메이션되어 움직일 뷰
    private val persistentInsetTypes: Int, // 레이아웃의 일부로 처리된 InsetType (항상 적용되는 InsetType)
    private val deferredInsetTypes: Int, // 애니메이션이 끝날 때까지 연기되어야 하는 InsetType
    dispatchMode: Int = DISPATCH_MODE_STOP // 이 콜벡의 dispatch 모드
) : WindowInsetsAnimationCompat.Callback(dispatchMode) {

    // onProgress()는 애니메이션 실행중에 insets가 변경될 때 호출됨
    override fun onProgress(
        insets: WindowInsetsCompat,
        runningAnimations: List<WindowInsetsAnimationCompat>
    ): WindowInsetsCompat {
        val typesInset = insets.getInsets(deferredInsetTypes)
        val otherInset = insets.getInsets(persistentInsetTypes)

        // 두 insets을 빼서(note: subtract 메서드 사용) 차이를 계산
        // 또한 음수 inset을 사용하지 않도록 inset을 >= 0으로 강제 설정함
        val diff = Insets.subtract(typesInset, otherInset).let {
            Insets.max(it, Insets.NONE)
        }

        view.translationX = (diff.left - diff.right).toFloat()
        view.translationY = (diff.top - diff.bottom).toFloat()

        return insets

        /* [입력 예시]
        I/System.out: typesInset: Insets{left=0, top=0, right=0, bottom=662}
        I/System.out: otherInset: Insets{left=0, top=66, right=0, bottom=132}
        I/System.out: diff: Insets{left=0, top=0, right=0, bottom=530}
        I/System.out:
        I/System.out: typesInset: Insets{left=0, top=0, right=0, bottom=698}
        I/System.out: otherInset: Insets{left=0, top=66, right=0, bottom=132}
        I/System.out: diff: Insets{left=0, top=0, right=0, bottom=566}
        I/System.out:
        I/System.out: typesInset: Insets{left=0, top=0, right=0, bottom=753}
        I/System.out: otherInset: Insets{left=0, top=66, right=0, bottom=132}
        I/System.out: diff: Insets{left=0, top=0, right=0, bottom=621}
        */
    }

    override fun onEnd(animation: WindowInsetsAnimationCompat) {
        // 애니메이션이 끝나면 translation 값을 재설정함
        view.translationX = 0f
        view.translationY = 0f
    }
}
