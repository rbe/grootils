/**
 * /Users/rbe/project/grootils/src/com/bensmann/grootils/swing/GlazedListsHelper.groovy
 * 
 * Copyright (C) 2010 Informationssysteme Ralf Bensmann.
 * Nutzungslizenz siehe http://www.bensmann.com/BPL_v10_de.html
 * Use is subject to license terms, see http://www.bensmann.com/BPL_v10_en.html
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
