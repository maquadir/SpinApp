package com.noorlabs.calcularity.dependencyInjection

import android.app.Application
import com.maq.propertyapp.properties.ItemViewModelFactory
import com.maq.spinapp.data.ItemRepository
import com.maq.spinapp.database.ItemDatabase


// Container of objects shared across the whole app
class AppContainer {

    // Since you want to expose userRepository out of the container, you need to satisfy
    // its dependencies as you did before



    // LoginContainer will be null when the user is NOT in the login flow
    var repositoryContainer: RepositoryContainer? = null

    // Network Container will be null when the user is NOT in the login flow
//    var networkContainer: NetworkContainer? = null




}



class RepositoryContainer(application: Application){

    var itemDao = ItemDatabase.getDatabase(application).getItemDao()
    val repository = ItemRepository(itemDao)

    //setup view model
    var factory = ItemViewModelFactory(repository)



}
