/*
 * CmdOptions.java
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

/**
 * <p>CmdOptions class.</p>
 *
 * @author niau
 * @version $Id: $Id
 */
public enum CmdOptions {
    ADDRESS("a", 1),
    COMMUNITY("c", 1),
    VERSION("v", 1),
    TIMEOUT("t", 1),
    RETRIES("r", 1),
    OUTPUT_FILE("f", 1),
    PROTOCOL("pr",1),
    OIDS("o", 1),
    DELTA("d", 1),
    MIBS_DIR("md", 1),
    PRINT_LOADED_MIBS("pm", 1),
    SECURITY_NAME("u",1),
    AUTH_LEVEL("aa",1),
    AUTH_PASSPHRASE("A",1),
    PRIV_PASSPHRASE("Y",1),
    AUTH_PROTOCOL("ap",1),
    PRIV_PROTOCOL("pp",1),
    OPERATION("O",1),
    PORT("P",1),
    OUTPUT_XML("f",1);

    private final String name;
    private final int valueSize;

    CmdOptions(String value, int valueSize) {
        this.name = value;
        this.valueSize = valueSize;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Getter for the field <code>valueSize</code>.</p>
     *
     * @return a int.
     */
    public int getValueSize() {
        return valueSize;
    }
}
