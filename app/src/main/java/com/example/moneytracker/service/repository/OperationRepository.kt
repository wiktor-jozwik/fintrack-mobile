package com.example.moneytracker.service.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moneytracker.service.model.Operation

class OperationRepository {
    private val moneyTrackerApi: IMoneyTrackerApi = MoneyTrackerApi()

    fun getOperationList(): LiveData<List<Operation>> {

        val data: MutableLiveData<List<Operation>> = MutableLiveData<List<Operation>>()

        moneyTrackerApi.getOperationList()[0].let { Log.i("operations2", it.toString()) }

        data.value = moneyTrackerApi.getOperationList()

//        moneyTrackerApi.getProjectList(userId).enqueue(object : Callback<List<Project?>?>() {
//            fun onResponse(call: Call<List<Project?>?>?, response: Response<List<Project?>?>) {
//                data.setValue(response.body())
//            } // Error handling will be explained in the next article …
//        })
        Log.i("operations", data.toString())

        return data
    }

}