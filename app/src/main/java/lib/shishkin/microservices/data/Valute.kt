package lib.shishkin.microservices.data

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Valute")
class Valute {
    @get:Attribute(name = "ID")
    @set:Attribute(name = "ID")
    var id: String? = null

    @get:Element(name = "NumCode")
    @set:Element(name = "NumCode")
    var numCode: String? = null

    @get:Element(name = "CharCode")
    @set:Element(name = "CharCode")
    var charCode: String? = null

    @get:Element(name = "Nominal")
    @set:Element(name = "Nominal")
    var nominal: String? = null

    @get:Element(name = "Name")
    @set:Element(name = "Name")
    var name: String? = null

    @get:Element(name = "Value")
    @set:Element(name = "Value")
    var value: String? = null

}