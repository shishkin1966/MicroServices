package lib.shishkin.microservices.provider

import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.sl.provider.ApplicationProvider
import java.io.File

class Providers {
    companion object {
        @JvmStatic
        fun exitApplication() {
            ApplicationProvider.instance.stop()
            //ApplicationProvider.instance.toBackground()
        }
    }
}
