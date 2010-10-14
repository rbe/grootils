/**
 * /Users/rbe/project/grootils/src/com/bensmann/grootils/GList.groovy
 * 
 * Copyright (C) 2010 Informationssysteme Ralf Bensmann.
 * Alle Rechte vorbehalten. Nutzungslizenz siehe http://www.bensmann.com/license_de.html
 * All Rights Reserved. Use is subject to license terms, see http://www.bensmann.com/license_en.html
 * 
 * Created by: rbe
 */
package com.bensmann.grootils

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
	def static inject = { ->
		[String[], java.util.List].each {
			it.metaClass.butLast = ListHelper.butLast
		}
	}
	
}
