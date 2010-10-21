/**
 * /Users/rbe/project/grootils/src/com/bensmann/grootils/MapHelper.groovy
 * 
 * Copyright (C) 2010 Informationssysteme Ralf Bensmann.
 * Nutzungslizenz siehe http://www.bensmann.com/BPL_v10_de.html
 * Use is subject to license terms, see http://www.bensmann.com/BPL_v10_en.html
 * 
 * Created by: rbe
 */
package com.bensmann.grootils

/**
 * 
 */
class MapHelper {
	
	/**
	 * Invert a map: change key: value to value: key.
	 * Taken from http://jira.codehaus.org/browse/GROOVY-4294
	 * http://jira.codehaus.org/secure/attachment/49994/patchfile.txt
	 */
	public static <K, V> Map<V, K> invertMap(Map<K, V> self) {
		Map<V, K> answer = new HashMap<V, K>()
		Iterator<Map.Entry<K, V>> it = self.entrySet().iterator()
		while (it.hasNext()) {
			Map.Entry entry = it.next()
			answer.put((V) entry.getValue(), (K) entry.getKey())
		}
		return answer
	}
	
	/**
	 * Recursively add PropertyChangeListener to the map itself and all nested maps.
	 */
	def static addMapPropertyChangeListener = { name, map, closure = null ->
		// This map
		map.addPropertyChangeListener({ evt ->
				if (closure) closure(evt)
			} as java.beans.PropertyChangeListener)
		// All nested maps
		map.each { k, v ->
			if (v instanceof ObservableMap) {
				MapHelper.addMapPropertyChangeListener("${name}.${k}", v, closure)
			}
		}
	}
	
	/**
	 * Copy all values from a map taking nested maps into account.
	 */
	def static deepCopyMap = { m, x ->
		x.each { k, v ->
			if (v instanceof Map) {
				// TODO Create a nested map if missing? m[k] = [:] as ObservableMap
				MapHelper.deepCopyMap m[k], v
			} else {
				m[k] = v
			}
		}
	}
	
	/**
	 * Flatten a map containing map(s), ...
	 */
	def static flatten = { String prefix = '' ->
		delegate.inject([:]) { map, v ->
			def kstr = "${prefix${prefix ? '.' : ''}$v.key}"
			if (v.value instanceof Map) {
				map += v.value.flatten(kstr)
			} else {
				map[kstr] = v.value
			}
			map
		}
	}
	
	/**
	 * 
	 */
	def static inject = {
		java.util.Map.metaClass.flatten = MapHelper.flatten
	}
	
}
