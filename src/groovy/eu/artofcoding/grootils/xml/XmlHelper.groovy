/*
 * Grootils
 * Copyright (C) 2005-2010 Informationssysteme Ralf Bensmann.
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
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
