/*
 * SnmpWalkToSnmpsimConvertor2.java
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


import java.io.*;
import java.util.regex.Pattern;

/**
 * Created by niau on 5/15/14.
 *
 * @author niau
 * @version $Id: $Id
 */
public class SnmpWalkToSnmpsimConvertor2 {

    static Pattern startPattern = Pattern.compile("^((\\.\\d+)+)\\.?.*");
    static Pattern oidPattern = Pattern.compile("^((\\.\\d+)+)\\.?\\s?");

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.io.IOException if any.
     */
    public static void main(String[] args) throws IOException {

        if(args.length!=2){
            System.out.println("Missing input parameters");
            System.out.println(" Example usage: /home/test/huawei.snmpwalout /usr/snmpsim/data/huawei.snmpwalk");
            return;
        }

        String inputFile = args[0];
        if (inputFile == null) {
            System.out.println("Missing input snmpwalk file");
            System.out.println(" Example usage: /home/test/huawei.snmpwalout /usr/snmpsim/data/huawei.snmpwalk");
            return;
        }
        String outputFile = args[1];
        if (inputFile == null) {
            System.out.println("Missing output snmpsim file");
            System.out.println(" Example usage: /home/test/huawei.snmpwalout /usr/snmpsim/data/huawei.snmpwalk");
            return;
        }


        File tmpFile = File.createTempFile("temp", ".snmpawalk");
        try {
            BufferedReader reader = null;
            PrintWriter printWriter = null;
            try {
                reader = new BufferedReader(new FileReader(inputFile));
                printWriter = new PrintWriter(tmpFile);
                String lastLine = null;
                String line;
                while ((line = reader.readLine()) != null) {
                    if (lastLine != null) {
                        if (!startPattern.matcher(line).matches()) {
                            printWriter.print(lastLine);
                        } else {
                            printWriter.println(lastLine);
                        }
                    }
                    lastLine = line;
                }
            } finally {
                if (reader != null) reader.close();
                if (printWriter != null) printWriter.close();
            }
            reader = null;
            printWriter = null;
            try {
                reader = new BufferedReader(new FileReader(tmpFile));
                printWriter = new PrintWriter(new File(outputFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (oidPattern.matcher(line).matches()) {
                        printWriter.print(line);
                    } else {
                        printWriter.println(line);
                    }
                }
            } finally {
                if (reader != null) reader.close();
                if (printWriter != null) printWriter.close();
            }
        } finally {
            tmpFile.delete();
        }
        System.out.println("Done! please review the converted file " + outputFile );
    }

}
