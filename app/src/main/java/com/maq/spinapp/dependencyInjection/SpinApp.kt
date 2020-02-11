package com.noorlabs.calcularity.dependencyInjection

import android.app.Application

class SpinApp: Application()  {

    // Instance of AppContainer that will be used by all the Activities of the app
    val appContainer = AppContainer()
}