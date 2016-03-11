/*
 * ParemetersAssembler.java
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

import org.snmp4j.util.SnmpConfigurator;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * Created by niau on 3/11/16.
 */
public class ParemetersAssembler {

    Properties properties = new Properties();

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public ParemetersAssembler(Properties properties) {
        this.properties = properties;
    }
    public ParemetersAssembler(Map<String, String> settings) {


        properties.put(SnmpConfigurator.O_ADDRESS, Arrays.asList(settings.get("ipAddress")));
        properties.put(SnmpConfigurator.O_COMMUNITY, Arrays.asList(settings.get("community-ro")));

        String version = settings.get("version") == null ? "2c" : settings.get("version");
        int retriesInt = settings.get("retries") == null ? 3 : Integer.parseInt(settings.get("retries"));
        int timeoutInt = settings.get("timeout") == null ? 1200 : Integer.parseInt(settings.get("timeout"));
        int maxrepetitions = settings.get("max-repetitions") == null ? 100 : Integer.parseInt(settings.get("max-repetitions"));
        int nonrepeaters = settings.get("non-repeaters") == null ? 10 : Integer.parseInt(settings.get("max-repetitions"));


        properties.put(SnmpConfigurator.O_VERSION, Arrays.asList(version));
        properties.put(SnmpConfigurator.O_TIMEOUT, Arrays.asList(timeoutInt));
        properties.put(SnmpConfigurator.O_RETRIES, Arrays.asList(retriesInt));
        properties.put(SnmpConfigurator.O_MAX_REPETITIONS, Arrays.asList(maxrepetitions));
        properties.put(SnmpConfigurator.O_NON_REPEATERS, Arrays.asList(nonrepeaters));
    }

}
