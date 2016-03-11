/*
 * XsltExecutor.java
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

package net.itransformers.snmp2xml4j.snmptoolkit;

import net.itransformers.snmp2xml4j.snmptoolkit.transform.XsltTransformer;
import org.apache.commons.io.FileUtils;
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

            if(args.length!=3 && args.length !=4 ){
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
            Map params = new HashMap<String,String>();
            if (args.length==4) {
                String deviceOS = args[3];
                if (deviceOS != null) {
                    params.put("DeviceOS", deviceOS);

                }
            }
            ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
            File xsltFileName1 = new File(inputXslt);

            FileInputStream inputStream1 =  new FileInputStream(new File(inputFilePath));


            XsltTransformer xsltTransformer = new XsltTransformer();
            try {
                xsltTransformer.transformXML(inputStream1,xsltFileName1,outputStream1,params);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (SAXException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (TransformerException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            FileUtils.writeStringToFile(new File(outputFilePath),new String(outputStream1.toByteArray()));

            System.out.println("Done! please review the transformed file " + outputFilePath);

        }



}
