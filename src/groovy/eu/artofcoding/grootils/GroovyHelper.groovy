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
package eu.artofcoding.grootils

/**
 *
 */
class GroovyHelper {

    /**
     * Copy a file using streams with a certain buffer size.
     */
    def static copyFile = { from, to, bufKb = 1 ->
        copyStream from.newInputStream(), to.newOutputStream()
    }

    /**
     * Copy a stream using a certain buffer size.
     */
    def static copyStream = { from, to, bufKb = 1 ->
        byte[] buf = new byte[bufKb * 1024]
        def len = 0
        while ((len = from.read(buf)) > 0) {
            to.write(buf, 0, len)
        }
        to.close()
        from.close()
    }

    /**
     * Clear time; set hours, mins, secs, msecs to 0.
     */
    def static clearTime = { d ->
        def c = java.util.Calendar.instance
        c.time = d
        c.with { [HOUR, MINUTE, SECOND, MILLISECOND].each { set(it, 0) } }
        c.set(java.util.Calendar.AM_PM, java.util.Calendar.AM)
        c.time
    }

    /**
     * Compare days of a date.
     */
    def static compareDay = { one, two ->
        GroovyHelper.clearTime(one).compareTo(GroovyHelper.clearTime(two))
    }

}
