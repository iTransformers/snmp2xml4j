/*
 * XsltTransformer.java
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

package net.itransformers.snmp2xml4j.snmptoolkit.transform;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class XsltTransformer {
    public void transformXML(InputStream xmlIn, File xslt, OutputStream xmlOut, Map<String,String> params) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        // JAXP reads data using the Source interface
        Source xmlSource = new StreamSource(xmlIn);

        Transformer trans = StylesheetCache.newTransformer(xslt);
        if (params != null) {
            for (String param: params.keySet()){
                trans.setParameter(param, params.get(param));
            }
        }

        trans.transform(xmlSource, new StreamResult(xmlOut));
    }
}
