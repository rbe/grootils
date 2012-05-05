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
 * Deal with SMB files using CIFS.
 * Usage:
 * <pre>
 * File f = new File("smb://server/share/path/file.ext")
 * def n = ntlmAuth = new jcifs.smb.NtlmPasswordAuthentication("domain", "user", "pwd")
 * use(CifsCategory) {*     println f.isSmb(n)
 *     println f.smbList(n)
 *}* </pre>
 */
class CifsCategory {

    /**
     * Initialize with empty NTLM auth, due to:
     * groovy.lang.GroovyRuntimeException: Ambiguous method overloading for method jcifs.smb.SmbFile#<init>.
     * Cannot resolve which method to invoke for [class java.lang.String, null] due to overlapping prototypes between:
     */
    private static final NTLM_AUTH = new jcifs.smb.NtlmPasswordAuthentication('', '', '')

    /**
     * Is this a SmbFile?
     */
    static isSmb(self) {
        switch (self.class) {
            case jcifs.smb.SmbFile:
                true
                break
            case java.io.File:
                self.path ==~ /smb:.*/
                break
            case java.lang.String:
                self ==~ /smb:.*/
                break
        }
    }

    /**
     * Create a SmbFile instance.
     */
    static File toSmbFile(self, ntlmAuth = NTLM_AUTH) {
        // Check argument
        if (self instanceof jcifs.smb.SmbFile) {
            return self
        } else /*if (self instanceof java.io.File)*/ {
            // Missing slashes; path will begin with smb:/ only and have to trailing slash
            // Windows: replace backslash
            def p = "smb://" + (self.path.replace('\\', '/') - "smb:/") + "/"
            new jcifs.smb.SmbFile(p, ntlmAuth)
        }
    }

    /**
     * Create a new empty file.
     */
    static smbCreateNewFile(self, ntlmAuth = NTLM_AUTH) {
        toSmbFile(self, ntlmAuth).createNewFile()
    }

    /**
     * List all files as String[].
     */
    static smbList(self, ntlmAuth = NTLM_AUTH) {
        toSmbFile(self, ntlmAuth).list()
    }

    /**
     * List all files as File[].
     */
    static smbListFiles(self, ntlmAuth = NTLM_AUTH) {
        // Convert all files to java.io.File to support our methods
        toSmbFile(self, ntlmAuth).listFiles().collect {
            // Create java.io.File and set NTLM authentication
            def f = new java.io.File(it.path)
            f.ntlmAuth = ntlmAuth
            f
        }
    }

    /**
     * Delete a file.
     */
    static smbDelete(self, ntlmAuth = NTLM_AUTH) {
        toSmbFile(self, ntlmAuth).delete()
    }

    /**
     * Get an OutputStream.
     */
    static smbGetOutputStream(self, ntlmAuth = NTLM_AUTH) {
        new jcifs.smb.SmbFileOutputStream(toSmbFile(self, ntlmAuth))
    }

    /**
     * Get an OutputStream.
     */
    static smbNewOutputStream(self, ntlmAuth = NTLM_AUTH) {
        smbGetOutputStream(self, ntlmAuth)
    }

    /**
     * Get an InputStream.
     */
    static smbGetInputStream(self, ntlmAuth = NTLM_AUTH) {
        new jcifs.smb.SmbFileInputStream(toSmbFile(self, ntlmAuth))
    }

    /**
     * Get an InputStream.
     */
    static smbNewInputStream(self, ntlmAuth = NTLM_AUTH) {
        smbGetInputStream(self, ntlmAuth)
    }

    /**
     * Rename a file.
     */
    static smbRenameTo(self, ntlmAuth = NTLM_AUTH, toFile) {
        toSmbFile(self, ntlmAuth).renameTo(toSmbFile(toFile, ntlmAuth))
    }

    /**
     * Copy a file, even to a local file:// URL.
     */
    static smbCopyTo(self, ntlmAuth = NTLM_AUTH, toFile) {
        def to
        if (isSmb(toFile)) {
            to = toSmbFile(toFile, ntlmAuth)
        } else {
            to = toFile instanceof java.io.File ? toFile : new java.io.File(toFile)
        }
        println "smbCopyTo: ${self.path} to ${to.path}"
        to.createNewFile()
        // Copy file
        GH.copyStream smbGetInputStream(self, ntlmAuth), to.newOutputStream()
    }

}
