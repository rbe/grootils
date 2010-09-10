/**
 * /Users/rbe/project/grootils/src/com/bensmann/grootils/cifs/CifsInjector.groovy
 * 
 * Copyright (C) 2010 Informationssysteme Ralf Bensmann.
 * Alle Rechte vorbehalten. Nutzungslizenz siehe http://www.bensmann.com/license_de.html
 * All Rights Reserved. Use is subject to license terms, see http://www.bensmann.com/license_en.html
 * 
 * Created by: rbe
 */
package com.bensmann.grootils.cifs

import com.bensmann.grootils.GroovyHelper as GH

/**
 * Inject CifsCategory methods into java.io.File. All files with URLs starting with smb:/ will
 * be handled using CifsCategory.
 */
class CifsFileInjector {
	
	/**
	 * Dynamically call SMB-methods using invokeMethod.
	 */
	def static inject = { ->
		// Copy a file.
		java.io.File.metaClass.copyTo = { toFile ->
			def to = toFile instanceof java.io.File ? toFile : new java.io.File(toFile)
			to.createNewFile()
			GH.copyStream delegate.newInputStream(), to.newOutputStream()
		}
		// Move a file.
		java.io.File.metaClass.moveTo = { toFile ->
			delegate.copyTo(toFile)
			delegate.delete()
		}
		// Delegate methods
		java.io.File.metaClass.ntlmAuth = CifsCategory.NTLM_AUTH
		java.io.File.metaClass.invokeMethod = { String name, args ->
			// Is this a SMB file; path starts with smb?
			if (CifsCategory.isSmb(delegate)) {
				use (CifsCategory) {
					// Prefix method call with 'smb'
					def n = name[0].toUpperCase() + name.substring(1)
					delegate."smb${n}"(delegate.ntlmAuth, *args)
				}
			} else {
				def m = delegate.metaClass.getMetaMethod(name, *args)
				if (m) {
					m.invoke(delegate, *args)
				} else {
					throw new MissingMethodException(name, delegate.class, args)
				}
			}
		}
	}
	
}
