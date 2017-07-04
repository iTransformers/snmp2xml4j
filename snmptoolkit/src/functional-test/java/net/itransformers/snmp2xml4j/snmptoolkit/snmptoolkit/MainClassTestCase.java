/*
 * MainClassTestCase.java
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

package net.itransformers.snmp2xml4j.snmptoolkit.snmptoolkit;

import net.itransformers.snmp2xml4j.snmptoolkit.MainClass;
import net.percederberg.mibble.MibLoaderException;
import org.junit.Test;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by niau on 3/29/16.
 */
public class MainClassTestCase {


    @Test
    public void snmpGetV3AuthNoPrivMainTestCase() throws TransformerException, IOException, MibLoaderException {
       String  mainArgs = "-O snmpGet -md mibs -v 3 -a 193.19.175.150 -P 161 -pr udp -aa AUTH_NOPRIV -ap MD5 -u usr-md5-none -A authkey1 -t 1000 -r 1 -m 100 -o sysName,sysDescr";
        System.out.println("snmpGetV3AuthNoPrivMainTestCase");

        StringTokenizer tokenizer = new StringTokenizer(mainArgs, " ");
        ArrayList<String> includesList = new ArrayList<String>();

        while (tokenizer.hasMoreTokens()) {
            includesList.add(tokenizer.nextToken());
        }
        String[] args = new String[includesList.size()];
        args = includesList.toArray(args);
        MainClass.main(args);
    }

    @Test
    public void snmpGetV3NoAuthNoPrivMainTestCase() throws TransformerException, IOException, MibLoaderException {
        System.out.println("snmpGetV3NoAuthNoPrivMainTestCase");
        String  mainArgs = "-O snmpGet -md mibs -v 3 -a 193.19.175.150 -P 161 -pr udp -aa NOAUTH_NOPRIV -ap MD5 -u usr-none-none -t 1000 -r 1 -m 100 -o sysDescr,sysName";

        StringTokenizer tokenizer = new StringTokenizer(mainArgs, " ");
        ArrayList<String> includesList = new ArrayList<String>();

        while (tokenizer.hasMoreTokens()) {
            includesList.add(tokenizer.nextToken());
        }
        String[] args = new String[includesList.size()];
        args = includesList.toArray(args);
        MainClass.main(args);
    }

    @Test
    public void snmpGetV3AuthPrivMainTestCase() throws TransformerException, IOException, MibLoaderException {
        String  mainArgs = "-O snmpGet -md mibs -v 3 -a 193.19.175.150 -P 161 -pr udp -aa AUTH_PRIV -ap SHA -u usr-sha-aes -A authkey1 -Y privkey1 -pp AES  -t 1000 -r 1 -m 100 -o sysDescr,sysName";
        System.out.println("snmpGetV3AuthPrivMainTestCase");
        StringTokenizer tokenizer = new StringTokenizer(mainArgs, " ");
        ArrayList<String> includesList = new ArrayList<String>();

        while (tokenizer.hasMoreTokens()) {
            includesList.add(tokenizer.nextToken());
        }
        String[] args = new String[includesList.size()];
        args = includesList.toArray(args);
        MainClass.main(args);
    }
}
