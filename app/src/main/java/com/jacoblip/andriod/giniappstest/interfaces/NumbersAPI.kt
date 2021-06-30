package com.jacoblip.andriod.giniappstest.interfaces

import com.jacoblip.andriod.giniappstest.data.models.NumberList
import retrofit2.Response
import retrofit2.http.GET

interface NumbersAPI {

    @GET("raw/8wJzytQX")
    suspend fun getNumbers():Response<NumberList>
}