

/*
 * CmdParser.java
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

import java.util.HashMap;
import java.util.Map;

/**
 * <p>CmdParser class.</p>
 *
 * @author niau
 * @version $Id: $Id
 */
class CmdParser {
    /**
     * <p>parseCmd.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @return a {@link java.util.Map} object.
     * @throws java.lang.Exception if any.
     */
    public static Map<CmdOptions, String> parseCmd(String[] args) throws Exception {
        Map<CmdOptions, String> result = new HashMap<CmdOptions, String>();
        int i=0;
        while (i < args.length){
            String opt = args[i];
            for (CmdOptions cmdOpt: CmdOptions.values()) {
                if (opt.equals("-"+cmdOpt.getName())) {

                    if (cmdOpt.getValueSize() == 0) {
                        break;
                    } else if (cmdOpt.getValueSize() == 1){
                        if (i+1 < args.length) {
                            result.put(cmdOpt, args[i+1]);
                        } else {
                            throw new Exception("missing value for \"-"+cmdOpt.getName()+"\" option");
                        }
                    } else {
                        throw new RuntimeException("Unsupported value size");
                    }
                }
            }
            i++;
        }
        return result;
    }

    /**
     * <p>printUsage.</p>
     *
     *
     * ADDRESS("a", 1),
     *    COMMUNITY("c", 1),
     *    VERSION("v", 1),
     *    TIMEOUT("t", 1),
     *    RETRIES("r", 1),
     *    MAX_REPETITIONS("m", 1),
     *    OUTPUT_FILE("f", 1),
     *    PROTOCOL("P",0),
     *    OIDS("o", 1),
     *    DELTA("d", 1),
     *    MIBS_DIR("md", 1),
     *    PRINT_LOADED_MIBS("pm", 0),
     *    SECURITY_NAME("u",0),
     *    AUTH_LEVEL("aa",0),
     *    AUTH_PASSPHRASE("A",0),
     *    PRIV_PASSPHRASE("Y",0),
     *    AUTH_PROTOCOL("ap",0),
     *    PRIV_PROTOCOL("pp",0),
     *    OPERATION("o",1),
     *    PORT("P",0),
     *    OUTPUT_XML("f",0);
     */
    public static void printUsage() {

            System.out.println("Usage: java -jar snmp2xml4j.jar -O <get|walk> -v <version> -a <address> -p <port> -pr <protocol> -c <community> " +
                    "-u <security_name> -aa <auth-level> -A <auth_passphrare> -ap <auth_protocol> -pp <priv_protocol> -Y <priv_passphrase> "+
                    "-t <timeout> -r <retries> -m <max_repetitions> [-f <output_file>] -o <oid_names>");
            System.out.println("Example v1/v2c: java -jar snmp2xml4j.jar -O <get|walk>  -v 2c -a 195.218.195.228 -p 161 -pr udp -c public" +
                    " -t 1000 -r 1 -m 100 -f out.xml -o \"sysDescr, sysName\"");
            System.out.println("Example v3c: java -jar snmp2xml4j.jar -O <get|walk> -v 3 -a 195.218.195.228 -p 161 -pr udp -aa AUTH_NOPRIV -u usr-md5-none -A authkey1 -ap MD5" +
                    " -t 1000 -r 1 -m 100 -f out.xml -o \"sysDescr, sysName\"");
    }





}
