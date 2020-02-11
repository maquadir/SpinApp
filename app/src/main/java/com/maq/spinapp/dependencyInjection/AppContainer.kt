package com.noorlabs.calcularity.dependencyInjection

import android.app.Application
import com.maq.propertyapp.properties.ItemViewModelFactory
import com.maq.spinapp.data.ItemRepository
import com.maq.spinapp.database.ItemDatabase


// Container of objects shared across the whole app
class AppContainer {


    // repositoryContainer will be null when the user is NOT in the login flow
    var repositoryContainer: RepositoryContainer? = null


}



class RepositoryContainer(application: Application){

    var itemDao = ItemDatabase.getDatabase(application).getItemDao()
    val repository = ItemRepository(itemDao)

    //setup view model
    var factory = ItemViewModelFactory(repository)



}
