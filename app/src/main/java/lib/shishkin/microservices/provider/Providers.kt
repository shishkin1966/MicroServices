package lib.shishkin.microservices.provider

import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.data.Account
import lib.shishkin.microservices.request.CreateAccountRequest
import lib.shishkin.microservices.request.GetAccountsRequest
import lib.shishkin.microservices.request.GetBalanceRequest
import lib.shishkin.microservices.request.GetCurrencyRequest
import lib.shishkin.sl.provider.ApplicationProvider

class Providers {
    companion object {
        @JvmStatic
        fun getAccounts(subscriber: String) {
            ApplicationSingleton.instance.dbProvider.request(GetAccountsRequest(subscriber))
        }

        @JvmStatic
        fun getBalance(subscriber: String) {
            ApplicationSingleton.instance.dbProvider.request(GetBalanceRequest(subscriber))
        }

        @JvmStatic
        fun createAccount(account: Account) {
            ApplicationSingleton.instance.dbProvider.request(CreateAccountRequest(account))
        }

        @JvmStatic
        fun getCurrency(subscriber: String) {
            ApplicationSingleton.instance.dbProvider.request(GetCurrencyRequest(subscriber))
        }

        @JvmStatic
        fun exitApplication() {
            //ApplicationProvider.instance.stop()
            ApplicationProvider.instance.toBackground()
        }
    }
}
