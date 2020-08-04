package lib.shishkin.microservices.screen.net_json

import com.annimon.stream.function.Predicate
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.PreferencesUtils
import lib.shishkin.microservices.data.Ticker
import lib.shishkin.sl.provider.ApplicationProvider
import java.util.*
import kotlin.collections.ArrayList


class TickerData {
    companion object {
        const val TickerDataFilter = "TickerDataFilter"
    }

    var tickers: ArrayList<Ticker>? = null
    var filter: String? = null

    init {
        filter = loadFilter()
    }

    fun getData(): List<Ticker> {
        if (tickers == null) return ArrayList()

        if (filter.isNullOrEmpty()) {
            tickers!!.sortWith(Comparator { o1, o2 -> o1.symbol!!.compareTo(o2.symbol!!, true) })
            return tickers!!
        } else {
            val list: ArrayList<Ticker> = ArrayList();
            list.addAll(
                ApplicationUtils.filter<Ticker>(
                    tickers as Collection<Ticker>,
                    Predicate { input ->
                        input?.name?.contains(filter!!, true)!!
                    }).toList()
            )
            list.sortWith(Comparator { o1, o2 -> o1.symbol!!.compareTo(o2.symbol!!, true) })
            return list
        }
    }

    fun saveFilter() {
        PreferencesUtils.putString(ApplicationProvider.appContext, TickerDataFilter, filter)
    }

    private fun loadFilter(): String? {
        return PreferencesUtils.getString(ApplicationProvider.appContext, TickerDataFilter)
    }
}
