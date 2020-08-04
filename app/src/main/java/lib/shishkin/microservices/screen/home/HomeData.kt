package lib.shishkin.microservices.screen.home

import lib.shishkin.microservices.data.Account
import lib.shishkin.microservices.data.Balance
import lib.shishkin.microservices.data.Card
import lib.shishkin.microservices.data.Deposit


class HomeData {
    var accounts: List<Account>? = null
    var deposits: List<Deposit>? = null
    var cards: List<Card>? = null
    var balance: List<Balance>? = null
    var currencies: List<String>? = null

    fun clear() {
        accounts = null
        deposits = null
        cards = null
        currencies = null
        balance = null
    }

    fun clearAccounts() {
        accounts = null
        balance = null
        currencies = null
    }

    fun clearDeposits() {
        deposits = null
        balance = null
        currencies = null
    }

    fun clearCards() {
        cards = null
        balance = null
        currencies = null
    }

    fun isFull(): Boolean {
        //return (accounts != null && balance != null &&  deposits != null && cards != null && currencies != null)
        return (accounts != null && balance != null && currencies != null)
    }
}
