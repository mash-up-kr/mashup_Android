package com.mashup.feature.setting

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.mashup.core.model.Platform
import com.mashup.core.model.data.local.UserPreference
import org.junit.Rule
import org.junit.Test

@ExperimentalFoundationApi
internal class SettingScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun getString(id: Int) = composeTestRule.activity.resources.getString(id)

    @Test
    fun whenPushAgreedStateIsTrue_SwitchStateIsOn() {
        composeTestRule.setContent {
            SettingScreen(
                userPreference = UserPreference(
                    token = "",
                    name = "",
                    platform = Platform.UNKNOWN,
                    generationNumbers = listOf(),
                    pushNotificationAgreed = true
                ),
                onLogout = {},
                onDeleteUser = {},
                onToggleFcm = {},
                onClickSNS = {},
                onClickBackButton = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("MashUpSwitch")
            .assertIsOn()
    }

    @Test
    fun whenPushAgreedStateIsFalse_SwitchStateIsOff() {
        composeTestRule.setContent {
            SettingScreen(
                userPreference = UserPreference(
                    token = "",
                    name = "",
                    platform = Platform.UNKNOWN,
                    generationNumbers = listOf(),
                    pushNotificationAgreed = false
                ),
                onLogout = {},
                onDeleteUser = {},
                onToggleFcm = {},
                onClickSNS = {},
                onClickBackButton = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("MashUpSwitch")
            .assertIsOff()
    }

    @Test
    fun allSNSLinksDisplayed() {
        composeTestRule.setContent {
            SettingScreen(
                userPreference = UserPreference(
                    token = "",
                    name = "",
                    platform = Platform.UNKNOWN,
                    generationNumbers = listOf(),
                    pushNotificationAgreed = false
                ),
                onLogout = {},
                onDeleteUser = {},
                onToggleFcm = {},
                onClickSNS = {},
                onClickBackButton = {}
            )
        }

        composeTestRule.onNodeWithText(getString(R.string.facebook)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.instagram)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.tistory)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.youtube)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.mHome)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.mRecruit)).assertIsDisplayed()
    }
}
