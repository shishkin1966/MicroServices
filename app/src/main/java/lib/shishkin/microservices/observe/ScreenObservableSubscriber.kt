package lib.shishkin.microservices.observe

import android.content.Intent
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.sl.observe.AbsObservableSubscriber
import lib.shishkin.sl.observe.ScreenBroadcastReceiverObservable

class ScreenObservableSubscriber : AbsObservableSubscriber() {
    companion object {
        const val NAME = "ScreenObservableSubscriber"
    }

    override fun getObservable(): List<String> {
        return listOf(ScreenBroadcastReceiverObservable.NAME)
    }

    override fun onChange(name: String, obj: Any) {
        val intent = obj as Intent
        if (intent.action == Intent.ACTION_SCREEN_ON) {
            ApplicationSingleton.instance.onScreenOn()
        } else if (intent.action == Intent.ACTION_SCREEN_OFF) {
            ApplicationSingleton.instance.onScreenOff()
        }
    }

    override fun getName(): String {
        return NAME
    }

}