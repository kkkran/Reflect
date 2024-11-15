package com.shijie.reflect

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OtherViewModel : ViewModel() {

    val viewWidth: MutableLiveData<Int> = MutableLiveData(0)
    val viewHeight: MutableLiveData<Int> = MutableLiveData(0)

}