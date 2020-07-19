package lib.shishkin.microservices.request

import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.data.Balance
import lib.shishkin.sl.request.AbsResultMessageRequest

class GetBalanceRequest(subscriber: String) : AbsResultMessageRequest(subscriber) {
    companion object {
        const val NAME = "GetBalanceRequest"
    }

    override fun getData(): List<Balance> {
        return ApplicationSingleton.instance.getDao().getBalance()
    }

    override fun isDistinct(): Boolean {
        return false
    }

    override fun getName(): String {
        return NAME
    }
}
