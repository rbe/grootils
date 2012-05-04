/*
 * Grootils, https://github.com/rbe/grootils
 * Copyright (C) 2005-2010 Informationssysteme Ralf Bensmann.
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eu.artofcoding.grootils.xml

/**
 * Helper for XML.
 */
class XmlHelper {

    /**
     * Groovy DOM builder.
     */
    def static domBuilder

    /**
     * Create a validator to validate an XML against a XSD.
     * Usage:
     * <code>
     * def f = new File("any.xsd")
     * validator.validate(new javax.xml.transform.stream.StreamSource(new java.io.StringReader(f.text)))
     * </code>
     */
    def static createaValidator = { /*java.io.InputStream or java.io.File*/ xsd ->
        javax.xml.validation.SchemaFactory
                .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI)
                .newSchema(new javax.xml.transform.stream.StreamSource(xsd))
                .newValidator()
    }

    /**
     * Try to create a node.
     */
    def static tc = { valueClosure, defaultClosure = null ->
        try {
            valueClosure()
        } catch (e) {
            println "tc: CATCHED: ${e}"
            // Default?
            if (defaultClosure) {
                defaultClosure()
            }
        }
    }

    /**
     *
     */
    def static m = { keys, map ->
        if (map) {
            keys.each { k ->
                XmlHelper.tc { domBuilder."${k}"(map[k] ?: "") } { XmlHelper.domBuilder."${k}"() }
            }
        }
    }

    /**
     * XML value to String
     */
    def static vs = { closure ->
        def std = ""
        try {
            return closure() ?: std
        } catch (e) { /*println e*/ }
        std
    }

    /**
     * XML value as Integer
     */
    def static vi = { closure ->
        def std = 0
        try {
            return (closure() as Integer) ?: std
        } catch (e) { /*println e*/ }
        std
    }

    /**
     * XML value as Double
     */
    def static vd = { closure ->
        def std = 0.0d
        try {
            return (closure() as Double) ?: std
        } catch (e) { /*println e*/ }
        std
    }

    /**
     * XML value as Boolean
     */
    def static vb = { closure ->
        def std = false
        try {
            return (closure() as Boolean) ?: std
        } catch (e) { /*println e*/ }
        std
    }

}
