/*
 * Grootils, https://github.com/rbe/grootils
 * Copyright (C) 2005-2010 Informationssysteme Ralf Bensmann.
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
