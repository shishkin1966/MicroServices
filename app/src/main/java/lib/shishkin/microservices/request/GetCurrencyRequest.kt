package lib.shishkin.microservices.request

import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.sl.request.AbsResultMessageRequest

class GetCurrencyRequest(subscriber: String) : AbsResultMessageRequest(subscriber) {
    companion object {
        const val NAME = "GetCurrencyRequest"
    }

    override fun getData(): List<String> {
        return ApplicationSingleton.instance.getDao().getCurrency()
    }

    override fun isDistinct(): Boolean {
        return false
    }

    override fun getName(): String {
        return NAME
    }
}
