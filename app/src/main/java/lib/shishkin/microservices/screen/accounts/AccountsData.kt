package lib.shishkin.microservices.screen.accounts

import com.annimon.stream.function.Predicate
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.data.Account
import lib.shishkin.microservices.data.Balance
import java.util.*
import kotlin.collections.ArrayList


class AccountsData {
    var sort = 0
    var filter: String? = null
    var accounts: List<Account> = ArrayList()
    var currencies: List<String> = ArrayList()
    var balance: List<Balance> = ArrayList()
    var isShowMessage = false
    var message: String? = null
    var messageType: Int = 0
    private val nameComparator = Comparator()
    { o1: Account, o2: Account -> o1.friendlyName!!.compareTo(o2.friendlyName!!) }
    private val currencyComparator = Comparator()
    { o1: Account, o2: Account -> o1.currency.compareTo(o2.currency) }
    private val currencyFilter = Predicate<Account> { value ->
        value?.currency.equals(filter)
    }

    fun isSortMenuEnabled(): Boolean {
        return accounts.size > 1
    }

    fun isFilterMenuEnabled(): Boolean {
        return balance.size > 1
    }

    fun getData(): List<Account> {
        val list = ArrayList<Account>()
        if (filter.isNullOrBlank()) {
            list.addAll(accounts)
        } else {
            list.addAll(ApplicationUtils.filter(accounts, currencyFilter).toList())
        }
        when (sort) {
            1 -> ApplicationUtils.sort(list, nameComparator)

            2 -> ApplicationUtils.sort(list, currencyComparator)
        }
        return list
    }

}
