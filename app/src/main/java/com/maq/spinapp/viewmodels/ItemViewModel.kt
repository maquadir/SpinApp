package com.maq.spinapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.maq.propertyapp.util.Coroutines
import com.maq.spinapp.data.Item
import com.maq.spinapp.data.ItemRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class ItemViewModel(val repository: ItemRepository) : ViewModel() {

    private lateinit var job: Job

    private val _itemsfetch = MutableLiveData<List<Item>>()
    private val _count = MutableLiveData<Int>()

    val itemsfetch : LiveData<List<Item>>
        get() = _itemsfetch

    val count : LiveData<Int>
        get() = _count

    init{
        Log.i("View Model","View Model created")
    }

    //to keep track of lifetime of view model
    override fun onCleared() {
        super.onCleared()
        Log.i("View Model","View Model destroyed")
        if(::job.isInitialized) job.cancel()
    }

    //function to insert data to Room database
    fun insertItems(item: Item){

        job =

            viewModelScope.launch {
                Coroutines.ioThenMain(
                    { repository.insertItems(item) }
                )
            }
    }

    //function to get data from Room database
    fun getItemsDb(){

        job =

            viewModelScope.launch {
                Coroutines.ioThenMain(
                    { repository.getItemsDb() },
                    { _itemsfetch.value =  it }
                )
            }
    }

    //function to get count from Room database
    fun getCount(){

        job =

            viewModelScope.launch {
                Coroutines.ioThenMain(
                    { repository.getCount() },
                    { _count.value =  it }
                )
            }
    }


}