package lib.shishkin.microservices.screen.accounts

import lib.shishkin.microservices.data.Account
import lib.shishkin.microservices.data.Balance


class AccountsData {
    var accounts: List<Account> = ArrayList()
    var currencies: List<String> = ArrayList()
    var balance: List<Balance> = ArrayList()

    fun getData(): List<Account> {
        return accounts
    }

}
