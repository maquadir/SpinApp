package com.maq.propertyapp.properties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maq.spinapp.data.ItemRepository
import com.maq.spinapp.viewmodels.ItemViewModel


//viewmodel factory class to create view models

@Suppress("UNCHECKED_CAST")
class ItemViewModelFactory (
    private val repository: ItemRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ItemViewModel(repository) as T
    }

}