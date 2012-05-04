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
 * Helper for arrays and lists.
 */
class ListHelper {

    /**
     * Return all elements except the last one.
     */
    def static butLast = {
        delegate - delegate.last()
    }

    /**
     * Inject methods.
     */
    def static inject() {
        [String[], java.util.List].each {
            it.metaClass.butLast = ListHelper.butLast
        }
    }

}
