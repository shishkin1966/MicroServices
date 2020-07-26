package lib.shishkin.microservices.screen.home

import lib.shishkin.microservices.data.Account
import lib.shishkin.microservices.data.Balance


class HomeData {
    var accounts: List<Account> = ArrayList()
    var currencies: List<String> = ArrayList()
    var balance: List<Balance> = ArrayList()
}
