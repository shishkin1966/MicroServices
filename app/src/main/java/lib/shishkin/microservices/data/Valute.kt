package lib.shishkin.microservices.data

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Valute")
class Valute  {
    @Attribute(name = "ID")
    var id: String? = null
        private set

    @Element(name = "NumCode")
    var numCode: String? = null
        private set

    @Element(name = "CharCode")
    var charCode: String? = null
        private set

    @Element(name = "Nominal")
    var nominal: String? = null
        private set

    @Element(name = "Name")
    var name: String? = null
        private set

    @Element(name = "Value")
    var value: String? = null
        private set

}