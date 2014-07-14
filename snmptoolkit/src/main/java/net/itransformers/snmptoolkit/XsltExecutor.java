package net.itransformers.snmptoolkit;

import net.itransformers.snmptoolkit.transform.XsltTransformer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: niau
 * Date: 7/14/14
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class XsltExecutor {




        public static void main(String[] args) throws IOException {

            if(args.length!=3){
                System.out.println("Missing input parameters");
                System.out.println(" Example usage: xsltTransform.sh /home/test/test.xslt /usr/data/Input.xml /usr/data/Output.xml ");
                return;
            }

            String inputXslt = args[0];
            if (inputXslt == null) {
                System.out.println("Missing input xslt file path");
                System.out.println(" Example usage: xsltTransformer.sh /home/test/test.xslt /usr/data/Input.xml /usr/data/Output.xml");
                return;
            }
            String inputFilePath = args[1];
            if (inputFilePath == null) {
                System.out.println("Missing input xml file path");
                System.out.println(" Example usage: xsltTransformer.sh /home/test/test.xslt /usr/data/Input.xml /usr/data/Output.xml");
                return;
            }

            String outputFilePath = args[2];
            if (outputFilePath == null) {
                System.out.println("Missing output file path");
                System.out.println(" Example usage: xsltTransformer.sh /home/test/test.xslt /usr/data/Input.xml /usr/data/Output.xml");
                return;
            }

            String deviceOS = args[3];

            ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
            File xsltFileName1 = new File(System.getProperty("base.dir"), inputXslt);

            FileInputStream inputStream1 =  new FileInputStream(new File(inputFilePath));


            XsltTransformer xsltTransformer = new XsltTransformer();
            Map params = new HashMap<String,String>();
            params.put("DeviceOS",deviceOS);
            try {
                xsltTransformer.transformXML(inputStream1,xsltFileName1,outputStream1,params);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (SAXException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (TransformerException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            System.out.println("Done! please review the transformed file " + outputFilePath);

        }



}
