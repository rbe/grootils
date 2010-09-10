/**
 * /Users/rbe/project/grootils/src/com/bensmann/grootils/swing/SwingHelper.groovy
 * 
 * Copyright (C) 2010 Informationssysteme Ralf Bensmann.
 * Alle Rechte vorbehalten. Nutzungslizenz siehe http://www.bensmann.com/license_de.html
 * All Rights Reserved. Use is subject to license terms, see http://www.bensmann.com/license_en.html
 * 
 * Created by: rbe
 */
package com.bensmann.grootils.swing

/**
 * 
 */
class SwingHelper {
	
	/**
	 * Make a frame full screen.
	 */
	def static makeFrameFullScreen = { frame ->
		java.awt.GraphicsDevice device = java.awt.GraphicsEnvironment.localGraphicsEnvironment.defaultScreenDevice
		if (device.fullScreenSupported) {
			device.fullScreenWindow = frame
		}
	}
	
	/**
	 * Maximize frame.
	 */
	def static maximizeFrame = { frame ->
		frame.extendedState = javax.swing.JFrame.MAXIMIZED_BOTH
	}
	
	/**
	 * Show an info dialog.
	 */
	def static showInfoDialog(arg) {
		javax.swing.JOptionPane.showMessageDialog(
			arg.frame,
			arg.message,
			arg.title ?: "Information",
			javax.swing.JOptionPane.INFORMATION_MESSAGE
		)
		if (arg.exception) {
			arg.exception.printStackTrace()
		}
	}
	
	/**
	 * Show a confirmation dialog.
	 */
	def static showConfirmDialog(arg) {
		def answer = javax.swing.JOptionPane.showConfirmDialog(
			arg.frame,
			arg.message,
			arg.title ?: "Frage",
			javax.swing.JOptionPane.YES_NO_OPTION,
			javax.swing.JOptionPane.INFORMATION_MESSAGE
		)
		if (arg.exception) {
			arg.exception.printStackTrace()
		}
		answer
	}
	
	/**
	 * Show an error dialog.
	 */
	def static showErrorDialog(arg) {
		javax.swing.JOptionPane.showMessageDialog(
			arg.frame,
			arg.message,
			arg.title ?: "Verzeihung!",
			javax.swing.JOptionPane.ERROR_MESSAGE
		)
		if (arg.exception) {
			arg.exception.printStackTrace()
		}
	}
	
	/**
	 * 
	 */
	def static showProgressDialog(arg) {
		def screen = java.awt.Toolkit.defaultToolkit.screenSize
		def w = (screen.width * 0.3) as Integer
		def h = (screen.height * 0.3) as Integer
		def d
		groovy.swing.SwingBuilder.build {
			d = dialog(
				title: "Fortschritt",
				defaultCloseOperation: javax.swing.JFrame.DO_NOTHING_ON_CLOSE,
				size: [w, h], locationRelativeTo: arg.frame
			) {
				panel() {
					borderLayout()
					panel(constraints: CENTER, border: titledBorder(title: "Meldung")) {
						label(text: arg.message)
					}
					panel(constraints: SOUTH) {
						borderLayout()
						progressBar(constraints: NORTH, indeterminate: true)
						//button(constraints: SOUTH, text: "Abbrechen")
					}
				}
			}
		}
		d
	}
	
}
