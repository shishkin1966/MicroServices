package lib.shishkin.microservices.provider

import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.data.Account
import lib.shishkin.microservices.request.*
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
            ApplicationProvider.instance.stop()
        }

        @JvmStatic
        fun getTickers(subscriber: String) {
            ApplicationSingleton.instance.get<NetProvider>(NetProvider.NAME)
                ?.request(GetTickersRequest(subscriber))
        }

        @JvmStatic
        fun getValCurs(subscriber: String, date: String) {
            ApplicationSingleton.instance.get<NetCbProvider>(NetCbProvider.NAME)
                ?.request(GetValCursRequest(subscriber, date))
        }

    }
}
