package com.mashup.core.common.utils.keyboard

import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

class RootViewDeferringInsetsCallback(
    private val persistentInsetTypes: Int, // 레이아웃의 일부로 처리된 InsetType (항상 적용되는 InsetType)
    private val deferredInsetTypes: Int // 애니메이션이 끝날 때까지 연기되어야 하는 InsetType

    // 이후에 TranslateDeferringInsetsAnimationCallback로 애니메이션 처리를 위해
    // 하위 뷰까지 디스패치가 전해져야 하므로 `DISPATCH_MODE_CONTINUE_ON_SUBTREE` 사용
) : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE),
    OnApplyWindowInsetsListener {

    private var view: View? = null
    private var lastWindowInsets: WindowInsetsCompat? = null

    // insets는 적어도 한 번은 UI에 표시돼야 구해짐
    // 시스템 윈도우 insets을 제외한 다른 insets은 초기값이 {0, 0, 0, 0} 임 (보여진적이 없으므로 아직 계산되지 않음)
    // -> 초기 연기값 false도 괜찮음
    private var deferredInsets = false

    // https://developer.android.com/reference/android/view/View#onApplyWindowInsets(android.view.WindowInsets)
    // 내부의 정책에 따라 뷰가 insets을 적용해야 할 때 호출됨
    override fun onApplyWindowInsets(
        v: View,
        windowInsets: WindowInsetsCompat
    ): WindowInsetsCompat {
        // 아래의 onEnd()를 위해 뷰와 insets을 저장합니다.
        view = v
        lastWindowInsets = windowInsets

        val types = when {
            // deferredInsets가 true면 systemBars() insets만 사용
            deferredInsets -> persistentInsetTypes
            // 그렇지 않으면 systemBars() 및 ime() insets의 조합을 사용
            else -> persistentInsetTypes or deferredInsetTypes

            // NOTE: 5 or 2
            // 5(2)의 경우에는 0101, 2(2)의 경우 0010. or 연산할 경우 0111. 즉, 7입니다.
        }

        // 패딩으로 설정하여, 결정된 insets을 적용
        val typeInsets = windowInsets.getInsets(types)
        v.setPadding(typeInsets.left, typeInsets.top, typeInsets.right, typeInsets.bottom)

        // 새로운 WindowInsetsCompat.CONSUMED를 반환하여 insets가 뷰 계층 구조로 더 이상 전달되지 않도록 함
        // 이것은 deprecated된 WindowInsetsCompat.consumeSystemWindowInsets() 및 관련 함수를 대체합니다.
        return WindowInsetsCompat.CONSUMED
    }

    // insets 애니메이션이 시작되려고 할 때 그리고 애니메이션이 끝나고 뷰가 배치되기 전에 호출됩니다.
    // Note: onPrepare 다음에 onApplyWindowInsets 호출됨
    override fun onPrepare(animation: WindowInsetsAnimationCompat) {
        if (animation.typeMask and deferredInsetTypes != 0) {
            // Note: 15 and 7
            // 15(2)의 경우에는 1111, 7(2)의 경우 0111.
            // and 연산을 할 경우 둘 다 1인 비트만 남게 되어 0111. 즉, 7 입니다.

            // IME가 현재 표시되지 않는 경우 WindowInsetsCompat.Type.ime() inset을 연기함
            // 그 결과 WindowInsetsCompat.Type.systemBars()만 적용되어
            // conversation_recyclerview가 더 큰 크기로 유지됩니다

            // 애니메이션이 끝나기 전까진, IME가 insets 되는걸 막음
            deferredInsets = true
        }
    }

    // 애니메이션 실행의 일부로 insets이 변경될 때 호출됩니다.
    override fun onProgress(
        insets: WindowInsetsCompat,
        runningAnims: List<WindowInsetsAnimationCompat>
    ): WindowInsetsCompat {
        // 이 클래스에서는 onProgress는 별로 처리가 필요 없습니다.
        return insets
    }

    // insets 애니메이션이 종료되면 호출됩니다.
    override fun onEnd(animation: WindowInsetsAnimationCompat) {
        if (deferredInsets && (animation.typeMask and deferredInsetTypes) != 0) {
            // IME insets을 연기했고 IME 애니메이션이 완료되면 deferredInsets 변수를 초기화 해야 함
            deferredInsets = false

            // 그리고 마지막으로, 지연된 inset을 지금 뷰에 디스패치합니다.
            // 이상적으로는 view.requestApplyInsets()를 호출하고 정상적인 디스패치 주기가
            // 발생하도록 하는 것이 좋지만 너무 늦게 발생하여 flicker가 발생합니다.
            // 대신 가장 최근의 WindowInsets를 뷰에 수동으로 디스패치합니다.
            if (lastWindowInsets != null && view != null) {
                // 주어진 window insets을 이 뷰 또는 하위 트리의 다른 뷰에 적용하도록 요청합니다.
                ViewCompat.dispatchApplyWindowInsets(view!!, lastWindowInsets!!)
            }
        }
    }
}
