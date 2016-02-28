/*
 * StylesheetCache.java
 *
 * This work is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 *
 * Copyright (c) 2010-2016 iTransformers Labs. All rights reserved.
 */

package net.itransformers.snmptoolkit.transform;

import net.sf.saxon.TransformerFactoryImpl;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class that caches XSLT
 * stylesheets in memory.
 */
public class StylesheetCache {
    // map xslt file names to MapEntry instances
    // (MapEntry is defined below)
    private static Map cache = new HashMap();

    /**
     * Flush all cached stylesheets from
     * memory, emptying the cache.
     */
    public static synchronized void flushAll() {
        cache.clear();
    }

    /**
     * Flush a specific cached stylesheet from memory.
     *
     * @param xsltFileName the file name of
     *                     the stylesheet to remove.
     */
    public static synchronized void flush(String xsltFileName) {
        cache.remove(xsltFileName);
    }

    /**
     * Obtain a new Transformer instance for the
     * specified XSLT file name.
     * A new entry will be added to the
     * cache if this is the first request
     * for the specified file name.
     *
     * @param xsltFile the file name
     *                     of an XSLT stylesheet.
     * @return a transformation context
     *         for the given stylesheet.
     */
    public static synchronized Transformer newTransformer(File xsltFile)
            throws TransformerConfigurationException {

        // determine when the file was last modified on disk
        long xslLastModified = xsltFile.lastModified();
        String xsltFileName = xsltFile.getName();
        MapEntry entry = (MapEntry) cache.get(xsltFileName);

        if (entry != null) {
            // if the file has been modified more recently than the
            // cached stylesheet, remove the entry reference
            if (xslLastModified > entry.lastModified) {
                entry = null;
            }
        }

        // create a new entry in the cache if necessary
        if (entry == null) {
            Source xslSource = new StreamSource(xsltFile);

            TransformerFactory transFact = TransformerFactory.newInstance();
            ((TransformerFactoryImpl) transFact).getConfiguration().setMessageEmitterClass(Log4jEmitter.class.getName());
            Templates templates = transFact.newTemplates(xslSource);

            entry = new MapEntry(xslLastModified, templates);
            cache.put(xsltFileName, entry);
        }

        return entry.templates.newTransformer();
    }

    // prevent instantiation of this class
    private StylesheetCache() {
    }

    /**
     * This class represents a value in the cache Map.
     */
    static class MapEntry {
        long lastModified; // when the file was modified
        Templates templates;

        MapEntry(long lastModified, Templates templates) {
            this.lastModified = lastModified;
            this.templates = templates;
        }
    }

}
