/*
 * TestResources.java
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

import net.itransformers.snmp2xml4j.snmptoolkit.MibLoaderHolder;
import net.percederberg.mibble.MibLoaderException;
import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.IOException;

/**
 * Created by niau on 4/1/16.
 */
public class TestResources extends ExternalResource {
    private static MibLoaderHolder mibLoaderHolder;

    static {
        try {
            String basedir = (String) System.getProperties().get("baseDir");
            if (basedir==null) {
                System.out.println("Basedir not set! Setting it to "+new File(".").getAbsolutePath());
                basedir = ".";
            }else {
                System.out.println(basedir);
            }
            File mibsPath = new File(basedir,"mibs");
            mibLoaderHolder = new MibLoaderHolder(mibsPath, false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MibLoaderException e) {
            e.printStackTrace();
        }
    }


    public static MibLoaderHolder getMibLoaderHolder() {
        return mibLoaderHolder;
    }

    public static void setMibLoaderHolder(MibLoaderHolder mibLoaderHolder) {
        TestResources.mibLoaderHolder = mibLoaderHolder;
    }
}
