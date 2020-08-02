package lib.shishkin.microservices.data

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.util.*

@Root(name = "ValCurs")
class ValCurs {
    @get:Attribute(name = "Date")
    @set:Attribute(name = "Date")
    var date: String? = null

    @get:Attribute(name = "name")
    @set:Attribute(name = "name")
    var name: String? = null

    @get:ElementList(inline = true)
    @set:ElementList(inline = true)
    var valute: ArrayList<Valute>? = null
}