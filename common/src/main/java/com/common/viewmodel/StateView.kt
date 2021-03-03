package com.common.viewmodel

import androidx.lifecycle.MutableLiveData


class StateView {
    val isEmpty = MutableLiveData<Boolean>()
    val isErr = MutableLiveData<Boolean>()
    val isContent = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
}
