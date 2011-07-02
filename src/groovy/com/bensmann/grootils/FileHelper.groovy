/**
 * /Users/rbe/project/grootils/src/groovy/com/bensmann/grootils/FileHelper.
 * 
 * Copyright (C) 2010 Informationssysteme Ralf Bensmann.
 * Nutzungslizenz siehe http://www.bensmann.com/BPL_v10_de.html
 * Use is subject to license terms, see http://www.bensmann.com/BPL_v10_en.html
 * 
 * Created by: rbe
 */
package com.bensmann.grootils

class FileHelper {
	
	/**
	 * 
	 */
	def static zip = { java.util.zip.ZipOutputStream zipOutStream, java.io.File f , String path ->
		def name = path.equals("") ? f.name : path + File.separator + f.name
		if (!f.isDirectory()) {
			def entry = new java.io.ZipEntry(name)
			zipOutStream.putNextEntry(entry)
			new java.io.FileInputStream(f).withStream { inStream ->
				def buffer = new byte[32 * 1024]
				def count
				while ((count = inStream.read(buffer, 0, buffer.length)) != -1) {
					zipOutStream.write(buffer, 0, count)
				}
			}
			zipOutStream.closeEntry()
		} else {
			// write the directory first, in order to allow empty directories
			def entry = new java.io.ZipEntry(name + java.io.File.separator)
			zipOutStream.putNextEntry(entry)
			zipOutStream.closeEntry()
			f.eachFile {
				zip(zipOutStream,it,name)
			}
		}
	}
	
	/**
	 * 
	 */
	def static unzip = { String dest ->
		//in metaclass added methods, 'delegate' is the object on which 
		//the method is called. Here it's the file to unzip
		def result = new java.util.zip.ZipInputStream(new java.io.FileInputStream(delegate))
		def destFile = new java.io.File(dest)
		if (!destFile.exists()) {
			destFile.mkdir()
		}
		result.withStream {
			def entry
			while (entry = result.nextEntry) {
				if (!entry.isDirectory()){
					new java.io.File(dest + java.io.File.separator + entry.name).parentFile?.mkdirs()
					def output = new java.io.FileOutputStream(dest + java.io.File.separator + entry.name)
					output.withStream {
						int len = 0
						byte[] buffer = new byte[32 * 1024]
						while ((len = result.read(buffer)) > 0){
							output.write(buffer, 0, len)
						}
					}
				} else {
					new java.io.File(dest + java.io.File.separator + entry.name).mkdir()
				}
			}
		}
	}
	
	/**
	 * 
	 */
	def static inject() {
		java.io.File.metaClass.zip = { String destination ->
			//cache the delegate (the File Object) as it will be modified in the withStream closure
			def input = delegate
			def result = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(destination))
			result.withStream { zipOutStream ->
				zip(zipOutStream, input, "")
			}
		}
		java.io.File.metaClass.unzip = { String dest ->
			FileHelper.unzip(dest)
		}
	}
	
}