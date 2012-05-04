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
            if (closure)
                closure(evt)
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
            def kstr = "${prefix$ {prefix ? '.' : ''} $v.key}"
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
