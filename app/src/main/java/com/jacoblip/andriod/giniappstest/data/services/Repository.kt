package com.jacoblip.andriod.giniappstest.data.services

import android.content.Context
import com.jacoblip.andriod.giniappstest.data.models.Number
import com.jacoblip.andriod.giniappstest.data.models.NumberList
import com.jacoblip.andriod.giniappstest.utilities.Util
import io.realm.Realm
import retrofit2.Response
import java.net.SocketTimeoutException

class Repository() {
    suspend fun getNumbers():Response<NumberList>{
            return RetrofitInstance.api.getNumbers()
    }

    fun getNumbersFromLocal():Array<Number>?{
        var numbers:Array<Number>? = null
        val realm = Realm.getDefaultInstance()
        numbers = realm.where(Number::class.java).findAll().toTypedArray()
        return numbers
    }
    fun insertNumbersToLocal(arrayOfNumbers:Array<com.jacoblip.andriod.giniappstest.data.models.Number>){
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.deleteAll()
        realm.insert(arrayOfNumbers.toMutableList())
        realm.commitTransaction()
    }
}