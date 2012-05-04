/*
 * Grootils
 * Copyright (C) 2005-2010 Informationssysteme Ralf Bensmann.
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 */
package eu.artofcoding.grootils.swing

/**
 *
 */
class GlazedListsHelper {

    /**
     *
     */
    def static makeComboboxCellEditor = { column, list ->
        def eventList = ca.odell.glazedlists.GlazedLists.eventList(list) as ca.odell.glazedlists.EventList
        javax.swing.DefaultCellEditor cellEditor = ca.odell.glazedlists.swing.AutoCompleteSupport.createTableCellEditor(eventList)
        column.setCellEditor(cellEditor)
    }

}
