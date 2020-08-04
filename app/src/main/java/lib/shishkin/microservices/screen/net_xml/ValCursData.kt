package lib.shishkin.microservices.screen.net_xml

import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.data.ValCurs
import lib.shishkin.microservices.data.Valute

class ValCursData {
    var valCurs: ValCurs? = null
    private val nameComparator = Comparator()
    { o1: Valute, o2: Valute -> o1.name!!.compareTo(o2.name!!) }

    fun getData(): List<Valute> {
        val data = ArrayList<Valute>()
        if (valCurs?.valute != null) {
            data.addAll(ApplicationUtils.sort(valCurs?.valute!!, nameComparator).toList())
        }
        return data
    }

}
