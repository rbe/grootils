/**
 * /Users/rbe/project/grootils/src/com/bensmann/grootils/NumberHelper.groovy
 * 
 * Copyright (C) 2010 Informationssysteme Ralf Bensmann.
 * Alle Rechte vorbehalten. Nutzungslizenz siehe http://www.bensmann.com/license_de.html
 * All Rights Reserved. Use is subject to license terms, see http://www.bensmann.com/license_en.html
 * 
 * Created by: rbe
 */
package com.bensmann.grootils

/**
 * 
 */
class NumberHelper {
	
	/**
	 * Rounding mode.
	 */
	def static roundingMode
	
	/**
	 * Number -> formatted German string.
	 */
	def static toString2 = { digits = 2, roundingMode = null ->
		def d = delegate
		def r
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
				if (roundingMode) nf.roundingMode = roundingMode
			}
			try {
				r = nf.format(d)
			} catch (e) {
				println "toString2(): Exception while converting number ${d?.dump()}: ${e}"
			}
		}
		r
	}
	
	/**
	 * Show number with 2 fraction digits.
	 */
	def static toString2Converter = { v ->
		if (v && v instanceof Number) {
			v.toString2()
		} else {
			"0,00"
		}
	}
	
	/**
	 * Show number with 3 fraction digits.
	 */
	def static toString3Converter = { v ->
		if (v && v instanceof Number) {
			v.toString2(3)
		} else {
			"0,000"
		}
	}
	
	/**
	 * Parse a string with german notation to a float value.
	 */
	def static toDouble2 = { digits = 2, roundingMode = null ->
		def d = delegate
		def r = 0.0d
		// Stop in case of we got a float/double
		if (d.class in [Float, Double]) {
			return d
		}
		if (d in ["NaN", "Inf"]) {
			//r = 0.0d
		} else if (d) {
			def nf = java.text.NumberFormat.getInstance(java.util.Locale.GERMAN)
			nf.minimumFractionDigits = digits
			nf.maximumFractionDigits = digits
			if (roundingMode) nf.roundingMode
			try {
				r = nf.parse(d) as Double
			} catch (e) {
				e.printStackTrace()
			}
		}
		//println "toDouble2(): ${d?.dump()} -> ${r?.dump()}"
		r
	}
	
	/**
	 * 
	 */
	def inject = { ->
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
