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
package eu.artofcoding.grootils.swing

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
