package lib.shishkin.microservices.request

import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.data.Account
import lib.shishkin.sl.request.AbsRequest

class CreateAccountRequest(private val account: Account) : AbsRequest() {
    companion object {
        const val NAME = "CreateAccountRequest"
    }

    override fun isDistinct(): Boolean {
        return false
    }

    override fun getName(): String {
        return NAME
    }

    override fun run() {
        account.openDate = System.currentTimeMillis()
        try {
            ApplicationSingleton.instance.getDao().insertAccount(account)
            ApplicationSingleton.instance.onChange(Account.TABLE)
        } catch (e: Exception) {
            ApplicationSingleton.instance.onError(getName(), e)
        }
    }

}
