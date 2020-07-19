package lib.shishkin.microservices.request

import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.data.Account
import lib.shishkin.sl.request.AbsResultMessageRequest

class GetAccountsRequest(subscriber: String) : AbsResultMessageRequest(subscriber) {
    companion object {
        const val NAME = "GetAccountsRequest"
    }

    override fun getData(): List<Account> {
        return ApplicationSingleton.instance.getDao().getAccounts()
    }

    override fun isDistinct(): Boolean {
        return false
    }

    override fun getName(): String {
        return NAME
    }
}
