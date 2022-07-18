package com.mashup.ui.event

import com.mashup.base.BaseViewModel
import com.mashup.data.datastore.UserDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val userDataSource: UserDataSource
) : BaseViewModel() {

}