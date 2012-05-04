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
package eu.artofcoding.grootils.xml

/**
 * Provide cached DTDs.
 */
class CachedDTD {

    /**
     * Read XML using locally cached DTDs:
     * xmlSlurper = new XmlSlurper()
     * xmlSlurper.setEntityResolver(com.bensmann.grootils.xml.CachedDTD.entityResolver)
     */
    def static entityResolver = [
            resolveEntity: { publicId, systemId ->
                try {
                    new org.xml.sax.InputSource(CachedDTD.class.getResourceAsStream("dtd/" + systemId.split("/").last()))
                } catch (e) {
                    e.printStackTrace()
                    null
                }
            }
    ] as org.xml.sax.EntityResolver

}
