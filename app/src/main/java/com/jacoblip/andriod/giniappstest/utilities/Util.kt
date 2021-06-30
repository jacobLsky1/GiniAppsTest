package com.jacoblip.andriod.giniappstest.utilities

import androidx.lifecycle.MutableLiveData

class Util {
    companion object{
        var hasInternet:MutableLiveData<Boolean> = MutableLiveData(true)
        var requestIsSuccesfull:MutableLiveData<Boolean> = MutableLiveData(true)
    }
}