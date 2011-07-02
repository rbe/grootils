/**
 * /Users/rbe/project/grootils/src/groovy/com/bensmann/grootils/ObjectHelper.groovy
 * 
 * Copyright (C) 2010 Informationssysteme Ralf Bensmann.
 * Nutzungslizenz siehe http://www.bensmann.com/BPL_v10_de.html
 * Use is subject to license terms, see http://www.bensmann.com/BPL_v10_en.html
 * 
 * Created by: rbe
 */
package com.bensmann.grootils

/**
 * 
 */
class ObjectHelper {
	
	/**
	 * Deep copy an object.
	 * @param obj An object.
	 * @return New, cloned object.
	 */
	def static deepCopy(obj) {
		def bos = new java.io.ByteArrayOutputStream()
		// Write obj to stream
		def oos = new java.io.ObjectOutputStream(bos)
		oos.writeObject(obj)
		oos.flush()
		// Read it back
		def ois = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bos.toByteArray()))
		ois.readObject()
	}
	
}
