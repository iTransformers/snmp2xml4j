package net.itransformers.snmptoolkit.transform;

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
