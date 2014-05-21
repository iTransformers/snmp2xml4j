/*
 * iTransformer is an open source tool able to discover IP networks
 * and to perform dynamic data data population into a xml based inventory system.
 * Copyright (C) 2010  http://itransformers.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.itransformers.snmptoolkit;

import java.util.HashMap;
import java.util.Map;

public class CmdParser {
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

    public static void printWalkUsage() {
        System.out.println("Usage: java -classpath=\"%CLASSPATH%\" Walk -md <mibs_dir> -a <address>/<port> -c <community> " +
                "-v <version> -t <timeout> -r <retries> -m <max_repetitions> [-f <output_file>] -o <oid_names>");
        System.out.println("Example: java -classpath=\"%CLASSPATH%\" Walk -md snmptoolkit/mibs -a 10.129.3.1/161 -c public -v 2c -t 1000 " +
                "-r 1 -m 100 [-f out.xml] -o \"ifIndex ifDescr ifOperStatus ifAdminStatus ifNumber ifAlias ifPhysAddress ifType dot1dTpFdb dot1dTpFdbAddress dot1dTpFdbStatus dot1dTpFdbPort dot1dBasePort dot1dBasePortIfIndex system dot1dBaseBridgeAddress dot1dStpPort ipNetToMediaTable ipAddrTable lldpRemoteSystemsData cdpCacheDevicePort cdpCacheDevicePlatform cdpCacheDeviceId cdpCacheIfIndex\"");
    }

    public static void printGetUsage() {
        System.out.println("Usage bgpPeeringUpdateGrapher.sh -a <IP ADDRESS/PORT> -d <DELTA INTERVAL -c <SNMP-COMMUNITY> -r <RETRY -v <SNMP VERSION - V1 (0), V2C(1), V3(2) -t <SNMP TIMEOUT> -o <SNMP OID> 1.3.6.1.2.1.15.3.1.10.<NEIGHBOUR_IP_ADDRESS" +
                "-d 3600000 -a 10.10.10.10.10/161 -c test-r -r 2 -v 0 -t 1000 -o 1.3.6.1.2.1.15.3.1.10.10.10.10.11");
    }
}
