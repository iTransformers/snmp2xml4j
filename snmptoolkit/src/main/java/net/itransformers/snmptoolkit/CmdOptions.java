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

public enum CmdOptions {
    ADDRESS("a", 1),
    COMMUNITY("c", 1),
    VERSION("v", 1),
    TIMEOUT("t", 1),
    RETRIES("r", 1),
    MAX_REPETITIONS("m", 1),
    OUTPUT_FILE("f", 1),
    PORT ("p",1),
    OIDS("o", 1),
    DELTA("d", 1),
    MIBS_DIR("md", 1),
    PRINT_LOADED_MIBS("pm", 0);

    private final String name;
    private final int valueSize;

    CmdOptions(String value, int valueSize) {
        this.name = value;
        this.valueSize = valueSize;
    }

    public String getName() {
        return name;
    }

    public int getValueSize() {
        return valueSize;
    }
}
