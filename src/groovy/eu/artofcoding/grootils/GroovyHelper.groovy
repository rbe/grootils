/*
 * Grootils
 * Copyright (C) 2005-2010 Informationssysteme Ralf Bensmann.
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 */
package eu.artofcoding.grootils

/**
 *
 */
class GroovyHelper {

    /**
     * Copy a file using streams with a certain buffer size.
     */
    def static copyFile = { from, to, bufKb = 1 ->
        copyStream from.newInputStream(), to.newOutputStream()
    }

    /**
     * Copy a stream using a certain buffer size.
     */
    def static copyStream = { from, to, bufKb = 1 ->
        byte[] buf = new byte[bufKb * 1024]
        def len = 0
        while ((len = from.read(buf)) > 0) {
            to.write(buf, 0, len)
        }
        to.close()
        from.close()
    }

    /**
     * Clear time; set hours, mins, secs, msecs to 0.
     */
    def static clearTime = { d ->
        def c = java.util.Calendar.instance
        c.time = d
        c.with { [HOUR, MINUTE, SECOND, MILLISECOND].each { set(it, 0) } }
        c.set(java.util.Calendar.AM_PM, java.util.Calendar.AM)
        c.time
    }

    /**
     * Compare days of a date.
     */
    def static compareDay = { one, two ->
        GroovyHelper.clearTime(one).compareTo(GroovyHelper.clearTime(two))
    }

}
