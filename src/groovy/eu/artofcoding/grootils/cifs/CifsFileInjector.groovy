/*
 * Grootils
 * Copyright (C) 2005-2010 Informationssysteme Ralf Bensmann.
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 */
package eu.artofcoding.grootils.cifs

import eu.artofcoding.grootils.GroovyHelper as GH

/**
 * Inject CifsCategory methods into java.io.File. All files with URLs starting with smb:/ will
 * be handled using CifsCategory.
 */
class CifsFileInjector {

    /**
     * Dynamically call SMB-methods using invokeMethod.
     */
    def static inject = {->
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
                use(CifsCategory) {
                    // Prefix method call with 'smb'
                    def n = name[0].toUpperCase() + name.substring(1)
                    delegate."smb${n}"(delegate.ntlmAuth, * args)
                }
            } else {
                def m = delegate.metaClass.getMetaMethod(name, * args)
                if (m) {
                    m.invoke(delegate, * args)
                } else {
                    throw new MissingMethodException(name, delegate.class, args)
                }
            }
        }
    }

}
