package com.mashup.ui.splash

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel() {

    private val _onLowerAppVersion = MutableSharedFlow<Unit>()
    val onLowerAppVersion: SharedFlow<Unit> = _onLowerAppVersion

    private val _onFinishInit = MutableSharedFlow<Unit>()
    val onFinishInit: SharedFlow<Unit> = _onFinishInit

    fun checkAppVersion(context: Context) = mashUpScope {
        try {
            val localVersionCode = PackageInfoCompat.getLongVersionCode(
                context.packageManager.getPackageInfo(context.packageName, 0)
            )
            val resentAppVersion = firebaseRepository.getRecentAppVersion()
            if (localVersionCode < resentAppVersion) {
                _onLowerAppVersion.emit(Unit)
            } else {
                delay(2000L)
                _onFinishInit.emit(Unit)
            }
        } catch (cancelException: CancellationException) {
            throw cancelException
        } catch (ignore: Exception) {
        }
    }
}
