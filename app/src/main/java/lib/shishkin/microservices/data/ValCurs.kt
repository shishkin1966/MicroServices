package lib.shishkin.microservices.data

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.util.*

@Root(name = "ValCurs")
class ValCurs {
    @Attribute(name = "Date")
    var date: String? = null
        private set

    @Attribute(name = "name")
    var name: String? = null
        private set

    @ElementList(inline = true)
    private var valute: ArrayList<Valute>? = null

    fun getValute(): ArrayList<Valute>? {
        return valute
    }
}