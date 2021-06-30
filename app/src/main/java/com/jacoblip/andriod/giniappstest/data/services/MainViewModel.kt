package com.jacoblip.andriod.giniappstest.data.services

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacoblip.andriod.giniappstest.data.models.Number
import com.jacoblip.andriod.giniappstest.data.models.NumberList
import com.jacoblip.andriod.giniappstest.utilities.Resource
import com.jacoblip.andriod.giniappstest.utilities.Util
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class MainViewModel(private val repository: Repository,private val context: Context):ViewModel() {
    var numbersLD:MutableLiveData<Resource<Array<Int>>> = MutableLiveData()
    var viewModelNumbers:Array<Int>? = null
    var numbersThatArePairs:Array<Int> = arrayOf()
    var gotDataFromWeb = false

    init {
       getNumbersFromLocal()
    }


    fun getNumbersFromLocal() = viewModelScope.launch {
       var numbers =  repository.getNumbersFromLocal()
        if(numbers?.size!=0){
          viewModelNumbers = handleListOfNumbers(numbers!!.toList())
        }
    }

    fun getNumbersFromWeb() = viewModelScope.launch {
        if(Util.hasInternet.value!!) {
            try {
                numbersLD.postValue(Resource.Loading())
                var numbers = repository.getNumbers()
                numbersLD.postValue(handleResponse(numbers))
            } catch (e: SocketTimeoutException) {
                Util.requestIsSuccesfull.postValue(false)
            }
        }
    }

    fun handleResponse(response:Response<NumberList>):Resource<Array<Int>>{
        if(response.isSuccessful){
            response.body()?.let {
                var arrayOfNumbers = handleListOfNumbers(it.numbers)
                repository.insertNumbersToLocal(it.numbers.toTypedArray())
                getNumbersFromLocal()
                return Resource.Success(arrayOfNumbers)
            }
        }
        Util.requestIsSuccesfull.postValue(false)
        return Resource.Error("something went wrong... please check your connection")
    }

    fun handleListOfNumbers(listOfNumbers:List<Number>):Array<Int>{
        var numbers:ArrayList<Int> = arrayListOf()
        var numbersThatPair:ArrayList<Int> = arrayListOf()

        for(num in listOfNumbers) {
            numbers.add(num.number)
        }
        numbers.sort()
        var  numbersArray = numbers.toTypedArray()


        var i = 0
        while ( numbersArray[i]<0){
            var number =  numbersArray[i]
            if( numbersArray.contains(Math.abs(number))){
                numbersThatPair.add(number)
                numbersThatPair.add(Math.abs(number))
            }
            i++
        }
        numbersThatArePairs = numbersThatPair.toTypedArray()

        return  numbersArray
    }
}