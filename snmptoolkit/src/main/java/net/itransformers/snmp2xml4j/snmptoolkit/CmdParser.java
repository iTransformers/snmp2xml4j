

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
public class CmdParser {
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
     * <p>printWalkUsage.</p>
     */
    public static void printWalkUsage() {
        System.out.println("Usage: snmpwalk.sh -md <mibs_dir> -a <address>/<port> -c <community> " +
                "-v <version> -t <timeout> -r <retries> -m <max_repetitions> [-f <output_file>] -o <oid_names>");
        System.out.println("snmpwalk.sh -md snmptoolkit/mibs -a 10.129.3.1/161 -c public -v 2c -t 1000 " +
                "-r 1 -m 100 [-f out.xml] -o \"ifIndex ifDescr ifOperStatus ifAdminStatus ifNumber ifAlias ifPhysAddress ifType dot1dTpFdb dot1dTpFdbAddress dot1dTpFdbStatus dot1dTpFdbPort dot1dBasePort dot1dBasePortIfIndex system dot1dBaseBridgeAddress dot1dStpPort ipNetToMediaTable ipAddrTable lldpRemoteSystemsData cdpCacheDevicePort cdpCacheDevicePlatform cdpCacheDeviceId cdpCacheIfIndex\"");
    }

    /**
     * <p>bgpPeeringUpdateGrapherUsage.</p>
     */
    public static void bgpPeeringUpdateGrapherUsage() {
        System.out.println("Usage bgpPeeringUpdateGrapher.sh -a <IP ADDRESS/PORT> -d <DELTA INTERVAL -c <SNMP-COMMUNITY> -r <RETRY -v <SNMP VERSION - V1 (0), V2C(1), V3(2) -t <SNMP TIMEOUT> -o <SNMP OID> 1.3.6.1.2.1.15.3.1.10.<NEIGHBOUR_IP_ADDRESS" +
                "-d 3600000 -a 10.10.10.10.10/161 -c test-r -r 2 -v 0 -t 1000 -o 1.3.6.1.2.1.15.3.1.10.10.10.10.11");
    }
    /**
     * <p>printGetUsage.</p>
     */
    public static void printGetUsage() {
        System.out.println("snmpget.sh -md <mibs_dir> -a <address>/<port> -c <community> " +
                "-v <version> -t <timeout> -r <retries> -m <max_repetitions> [-f <output_file>] -o <oid_names>");
        System.out.println("Example: snmpget.sh -md snmptoolkit/mibs -a 10.129.3.1/161 -c public -v 2c -t 1000 " +
                "-r 1 -m 100 [-f out.xml] -o \"ifIndex ifDescr ifOperStatus ifAdminStatus ifNumber ifAlias ifPhysAddress ifType dot1dTpFdb dot1dTpFdbAddress dot1dTpFdbStatus dot1dTpFdbPort dot1dBasePort dot1dBasePortIfIndex system dot1dBaseBridgeAddress dot1dStpPort ipNetToMediaTable ipAddrTable lldpRemoteSystemsData cdpCacheDevicePort cdpCacheDevicePlatform cdpCacheDeviceId cdpCacheIfIndex\"");

    }

    /**
     * <p>snmpWalktoSnmpSimUsage.</p>
     */
    public static void snmpWalktoSnmpSimUsage() {
        System.out.println("Usage bgpPeeringUpdateGrapher.sh -a <IP ADDRESS/PORT> -d <DELTA INTERVAL -c <SNMP-COMMUNITY> -r <RETRY -v <SNMP VERSION - V1 (0), V2C(1), V3(2) -t <SNMP TIMEOUT> -o <SNMP OID> 1.3.6.1.2.1.15.3.1.10.<NEIGHBOUR_IP_ADDRESS" +
                "-d 3600000 -a 10.10.10.10.10/161 -c test-r -r 2 -v 0 -t 1000 -o 1.3.6.1.2.1.15.3.1.10.10.10.10.11");
    }

}
