/*
 * Launcher.java
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

import net.percederberg.mibble.MibLoaderException;
import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.VariableBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by niau on 3/29/16.
 */
class Launcher {

    public static  void run(String [] args) throws IOException, MibLoaderException {
        SnmpManager snmpManager = null;

        SnmpXmlPrinter snmpXmlPrinter;

        Map<CmdOptions, String> opts;

        try {
            opts = CmdParser.parseCmd(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            CmdParser.printUsage("GET");
            return;
        }

        String operation = opts.get(CmdOptions.OPERATION);
        if (operation == null){
            System.out.println("Missing option \"-" + CmdOptions.OPERATION.getName()  +" Possible options are get,set,walk "+ "\"");
            CmdParser.printUsage("GET");
            return;

        }

        String mibDir = opts.get(CmdOptions.MIBS_DIR);
        if (mibDir == null) {
            System.out.println("Missing option \"-" + CmdOptions.MIBS_DIR.getName() + "\"");
            CmdParser.printUsage(operation);
            return;
        }
        MibLoaderHolder mibLoaderHolder = new MibLoaderHolder(new File(mibDir), false);




        String address = opts.get(CmdOptions.ADDRESS);
        if (address != null) {
        } else {
            System.out.println("Missing option \"-" + CmdOptions.ADDRESS.getName()  +"\"");
            CmdParser.printWalkUsage(operation);
            return;
        }

        String port = opts.get(CmdOptions.PORT);
        int portInt = 161;
        if (port != null) {

            try {
                portInt = Integer.parseInt(port);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid parameter value for \"-" + CmdOptions.PORT + "\", int value is required");
                CmdParser.printUsage(operation);
            }
        }

        String protocol = opts.get(CmdOptions.PROTOCOL);
        if (protocol != null) {
        } else {
            protocol = "udp";
        }


        String timeout = opts.get(CmdOptions.TIMEOUT);
        int timeoutInt = 1000;
        if (timeout != null) {

            try {
                timeoutInt = Integer.parseInt(timeout);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid parameter value for \"-" + CmdOptions.TIMEOUT + "\", int value is required");
                CmdParser.printUsage(operation);
            }
        } else {
            System.out.println("Using default timeout: 1000");

        }



        String retries = opts.get(CmdOptions.RETRIES);
        int  retriesInt = 1;



        if (retries != null) {
            try {
                retriesInt = Integer.parseInt(retries);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid parameter value for \"-" + CmdOptions.RETRIES + "\", int value is required");
                CmdParser.printWalkUsage(operation);
            }
        } else {
            System.out.println("Using default retries: 1");
        }



//        if (maxRepetitions != null) {
//            try {
//                maxRepetitionsInt = Integer.parseInt(maxRepetitions);
//            } catch (NumberFormatException nfe) {
//                System.out.println("Invalid parameter value for \"-" + CmdOptions.MAX_REPETITIONS + "\", int value is required");
//                CmdParser.printWalkUsage(operation);
//            }
//        } else {
//            System.out.println("Using default maxRepetitions: 10");
//        }

        String version = opts.get(CmdOptions.VERSION);
        if (version != null) {
        } else {
            System.out.println("Missing option \"-" + CmdOptions.VERSION.getName() + "\"");
            CmdParser.printWalkUsage(operation);
            return;
        }

        if (version.equalsIgnoreCase("1")) {
            String community = opts.get(CmdOptions.COMMUNITY);
            if (community != null) {
                if ("udp".equals(protocol)){
                    snmpManager = new SnmpUdpV1Manager(mibLoaderHolder.getLoader(),address,community,retriesInt,timeoutInt,65535,portInt);

                } else{
                    snmpManager = new SnmpTcpV1Manager(mibLoaderHolder.getLoader(),address,community,retriesInt,timeoutInt,65535,portInt);

                }            } else {
                System.out.println("Missing option \"-" + CmdOptions.COMMUNITY.getName() + "\"");
                CmdParser.printWalkUsage(operation);
                return;
            }
        } else if (version.equalsIgnoreCase("2") || version.equalsIgnoreCase("2c")){
            String community = opts.get(CmdOptions.COMMUNITY);
            if (community != null) {
                if ("udp".equals(protocol)){
                    snmpManager = new SnmpUdpV2Manager(mibLoaderHolder.getLoader(),address,community,retriesInt,timeoutInt,65535,portInt);

                } else{
                    snmpManager = new SnmpTcpV2Manager(mibLoaderHolder.getLoader(),address,community,retriesInt,timeoutInt,65535,portInt);

                }
            } else {
                System.out.println("Missing option \"-" + CmdOptions.COMMUNITY.getName() + "\"");
                CmdParser.printUsage(operation);
                return;
            }
        } else if (version.equalsIgnoreCase("3")){

            String authLevel = opts.get(CmdOptions.AUTH_LEVEL);
            String securityName = opts.get(CmdOptions.SECURITY_NAME);
            String authPassshare = opts.get(CmdOptions.AUTH_PASSPHRASE);
            String authProtocol = opts.get(CmdOptions.AUTH_PROTOCOL);
            String privacyPassshare = opts.get(CmdOptions.PRIV_PASSPHRASE);
            String privacyProtocol = opts.get(CmdOptions.PRIV_PROTOCOL);

            if (securityName != null) {

            } else {
                System.out.println("Missing option \"-" + CmdOptions.SECURITY_NAME.getName() + "\"");
                CmdParser.printUsage(operation);
                return;
            }

            int authLevelInt;
            if (authLevel != null) {

                if (authLevel.equalsIgnoreCase("AUTH_NOPRIV")){
                    authLevelInt = SecurityLevel.AUTH_NOPRIV;
                    if (authPassshare != null){

                    } else {
                        System.out.println("Missing option \"-" + CmdOptions.AUTH_PASSPHRASE.getName() + "\"");
                        CmdParser.printUsage(operation);
                        return;
                    }

                    if (authProtocol !=null){

                    } else {
                        System.out.println("Missing option \"-" + CmdOptions.AUTH_PROTOCOL.getName() + "\"");
                        CmdParser.printUsage(operation);
                        return;
                    }

                } else if (authLevel.equalsIgnoreCase("AUTH_PRIV"))  {
                    authLevelInt = SecurityLevel.AUTH_PRIV;
                    if (authPassshare != null){

                    } else {
                        System.out.println("Missing option \"-" + CmdOptions.AUTH_PASSPHRASE.getName() + "\"");
                        CmdParser.printUsage(operation);
                        return;
                    }

                    if (authProtocol !=null){

                    } else {
                        System.out.println("Missing option \"-" + CmdOptions.AUTH_PROTOCOL.getName() + "\"");
                        CmdParser.printUsage(operation);
                        return;
                    }
                    if (privacyProtocol!=null){

                    } else {
                        System.out.println("Missing option \"-" + CmdOptions.PRIV_PROTOCOL.getName() + "\"");
                        CmdParser.printUsage(operation);
                        return;
                    }
                    if (privacyPassshare!=null){

                    } else {
                        System.out.println("Missing option \"-" + CmdOptions.PRIV_PASSPHRASE.getName() + "\"");
                        CmdParser.printUsage(operation);
                        return;
                    }


                } else if (authLevel.equalsIgnoreCase("NOAUTH_NOPRIV")){
                    authLevelInt = SecurityLevel.NOAUTH_NOPRIV;

                } else {
                    System.out.println("Unrecognized \"-" + CmdOptions.AUTH_LEVEL + "\"");
                    CmdParser.printUsage(operation);
                    return;
                }


            } else {
                System.out.println("Missing option \"-" + CmdOptions.AUTH_LEVEL.getName() + "\"");
                CmdParser.printUsage(operation);
                return;
            }




            if ("udp".equals(protocol)){
                snmpManager = new SnmpUdpV3Manager(mibLoaderHolder.getLoader(),address,authLevelInt,securityName,authPassshare,authProtocol,privacyProtocol,privacyPassshare,retriesInt,timeoutInt,65535,portInt);

            } else{
                snmpManager = new SnmpTcpV3Manager(mibLoaderHolder.getLoader(),address,authLevelInt,securityName,authPassshare,authProtocol,privacyProtocol,privacyPassshare,retriesInt,timeoutInt,65535,portInt);

            }

        }
        if (snmpManager !=null){
            snmpManager.init();
        }  else {

            System.out.println("Unable to initialize SNMP manager");

            return;
        }
        ArrayList<String> includesList = new ArrayList<String>();
        String oids = opts.get(CmdOptions.OIDS);
        if (oids == null) {
            System.out.println("Missing option \"-" + CmdOptions.OIDS.getName());
            CmdParser.printWalkUsage(operation);
            return;
        }

        StringTokenizer tokenizer = new StringTokenizer(oids, " ,;");
        while (tokenizer.hasMoreTokens()) {
            includesList.add(tokenizer.nextToken());
        }

        if (operation.equalsIgnoreCase("walk")) {
            Node root = snmpManager.walk(includesList.toArray(new String[includesList.size()]));
            snmpXmlPrinter = new SnmpXmlPrinter(mibLoaderHolder.getLoader(),root);
            String xml =snmpXmlPrinter.printTreeAsXML();

            outputXml(opts, xml);

        }else if (operation.equalsIgnoreCase("get")) {
            ResponseEvent responseEvent = snmpManager.get( includesList);

            PDU response;
            if (responseEvent!=null) {
                response = responseEvent.getResponse();
                if (response != null) {

                    for (int i = 0; i < response.size(); i++) {
                        VariableBinding vb1 = response.get(i);
                        System.out.println(vb1.getVariable().toString());

                    }
                }
            } else {
                System.out.println("PDU response event is null");
            }
        } else {
            //TODO add snmpset
            //  snmpManager.set(oids,1);
        }

    }
    private static void outputXml(Map<CmdOptions, String> opts, String finalXml) throws FileNotFoundException {
        String outputFile = opts.get(CmdOptions.OUTPUT_FILE);
        if (outputFile == null) {
            System.out.println(finalXml);
        } else {
            PrintWriter writer = new PrintWriter(outputFile);
            writer.print(finalXml);
            writer.flush();
            writer.close();
        }
    }
}
