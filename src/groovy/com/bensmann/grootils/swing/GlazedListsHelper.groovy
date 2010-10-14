/**
 * /Users/rbe/project/grootils/src/com/bensmann/grootils/swing/GlazedListsHelper.groovy
 * 
 * Copyright (C) 2010 Informationssysteme Ralf Bensmann.
 * Alle Rechte vorbehalten. Nutzungslizenz siehe http://www.bensmann.com/license_de.html
 * All Rights Reserved. Use is subject to license terms, see http://www.bensmann.com/license_en.html
 * 
 * Created by: rbe
 */
package com.bensmann.swing

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
