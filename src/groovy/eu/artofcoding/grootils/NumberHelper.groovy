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

import java.math.RoundingMode

/**
 *
 */
class NumberHelper {

    /**
     * Standard rounding mode.
     */
    public static RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP

    /**
     * Number -> Formatted German String
     */
    def static toString2 = { digits = 2, roundingMode = null ->
        def d = delegate
        def r = "0,00"
        // Check against NaN, Infinity
        if (d in [Float.NaN, Double.NaN]) {
            //r = "NaN"
        } else if (d in [Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY]) {
            //r = "Inf"
        } else if (d) {
            def nf = java.text.NumberFormat.getInstance(java.util.Locale.GERMAN)
            // Use fraction digits?
            if (d instanceof Integer) {
                r = "0"
                nf.minimumFractionDigits = 0
                nf.maximumFractionDigits = 0
            } else {
                r = "0," + "0" * digits
                nf.minimumFractionDigits = digits
                nf.maximumFractionDigits = digits
                nf.roundingMode = roundingMode ?: NumberHelper.ROUNDING_MODE
            }
            try {
                r = nf.format(d)
            } catch (e) {
                println "toString2(): Exception while converting number ${d?.dump()} to string: ${e}"
            }
        }
        r
    }

    /**
     * Parse a string with german notation to a double value.
     */
    def static toDouble2 = { digits = 2, roundingMode = null ->
        def d = delegate
        def r = 0.0d
        // Null?
        if (d == null)
            return r
        // Stop in case of we got a float/double
        if (!(d.getClass() in [java.lang.String]) || d.getClass() in [java.lang.Float, java.lang.Double, java.math.BigDecimal]) {
            return d
        }
        // Does String contain a character?
        def charList = (["a".."z"] + ["A".."Z"]).flatten()
        if (d.any { it in charList })
            return d
        // TODO HACK: Convert dot into comma (we expect german format, not english)
        d = d.collect { it == "." ? "," : it }.join()
        // Parse number
        if (d in ["NaN", "Inf"]) {
            //r = 0.0d
        } else if (d) {
            def nf = java.text.NumberFormat.getInstance(java.util.Locale.GERMAN)
            nf.minimumFractionDigits = digits
            nf.maximumFractionDigits = digits
            if (roundingMode)
                nf.roundingMode = roundingMode ?: NumberHelper.ROUNDING_MODE
            try {
                r = nf.parse(d) as Double
            } catch (e) {
                println "toDouble2: Exception while converting string ${d?.dump()} to double: d=${delegate} digits=${digits} e=${e}"
                //e.printStackTrace()
                return d
            }
        }
        r
    }

    /**
     *
     */
    def inject = {->
        // Add .toDouble2 and .toString2 to all types to have a convenient API
        [Integer, Long, Float, Double, BigDecimal, String].each {
            it.metaClass.toDouble2 = NumberHelper.toDouble2
            it.metaClass.toString2 = NumberHelper.toString2
        }
        // String.multiply
        String.metaClass.multiply = { m ->
            def a = delegate.toDouble2()
            def b = m.toDouble2()
            delegate = (a * b).toString2()
        }
        // Override String.toString2
        String.metaClass.toString2 = {
            delegate
        }
    }

}
