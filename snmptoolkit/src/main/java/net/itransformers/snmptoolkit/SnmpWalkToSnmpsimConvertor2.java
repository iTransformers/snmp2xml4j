package net.itransformers.snmptoolkit;


import java.io.*;
import java.util.regex.Pattern;

/**
* Created by niau on 5/15/14.
*/
public class SnmpWalkToSnmpsimConvertor2 {

    static Pattern startPattern = Pattern.compile("^((\\.\\d+)+)\\.?.*");
    static Pattern oidPattern = Pattern.compile("^((\\.\\d+)+)\\.?\\s?");

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
