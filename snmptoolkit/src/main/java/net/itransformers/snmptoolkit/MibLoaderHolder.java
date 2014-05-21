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

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.MibValueSymbol;
import org.apache.log4j.Logger;

import java.io.*;

public class MibLoaderHolder {
    static Logger logger = Logger.getLogger(MibLoaderHolder.class);

    private MibLoader loader;

    public MibLoaderHolder(MibLoader loader) throws IOException, MibLoaderException {
        this.loader = loader;
    }

    public MibLoaderHolder(File mibDir, boolean failOnError) throws IOException, MibLoaderException {
        this.loader = new MibLoader();
        loader.addDir(mibDir);
        File[] files = mibDir.listFiles(new FilenameFilter(){
            public boolean accept(File file, String s) {
                return !s.equals(".svn");
            }
        });
        if (files == null) {
            logger.error("Can not load mib files from dir: "+mibDir);
            throw new IOException("Can not load mib files from dir: "+mibDir.getAbsolutePath());
        }
        for (File file : files) {
            try {
                loader.load(file);
            } catch (MibLoaderException mle) {
                if (failOnError) {
                    throw mle;
                } else {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    mle.getLog().printTo(new PrintWriter(bos));
                    logger.warn(new String(bos.toByteArray()));
                }
            }
        }
    }

    public MibLoaderHolder(String[] mibFiles) throws IOException, MibLoaderException {
        loader = new MibLoader();
        loader.addDir(new File("mibs"));
        for (int i = 0; i < mibFiles.length; i++) {
            loader.load(mibFiles[i]);
        }
    }

    public MibLoader getLoader() {
        return loader;
    }

    public String getSymbolByOid(String mibName, String oid){
        logger.debug("getSymbolByOid, mibName="+mibName + ", oid="+oid);
        Mib mib = loader.getMib(mibName);
        if (mib == null) {
             logger.error("Can not find symbol by mib, mibName="+mibName + ", oid="+oid);
        }
        final MibValueSymbol symbolByOid = mib.getSymbolByOid(oid);
        if (symbolByOid == null) {
            logger.error("Can not find symbol by OID, mibName="+mibName + ", oid="+oid);
            return null;
        }
        logger.info(symbolByOid.getName());

        return symbolByOid.getName();
    }
    public static void main(String[] args) throws IOException, MibLoaderException {
        String oid = "1.3.6.1.4.1.2636.1.1.1.2.1";
        MibLoaderHolder holder = new MibLoaderHolder(new File("snmptoolkit/mibs"), false);
        System.out.println(holder.getSymbolByOid( "JUNIPER-CHASSIS-DEFINES-MIB", oid));
    }
    public static void main1(String[] args) throws IOException {
        String[] mibFiles = new String[]{"CISCO-CDP-MIB"};
        MibLoader loader = new MibLoader();
        loader.addDir(new File("mibs"));
        for (int i = 0; i < mibFiles.length; i++) {
            try {
                loader.load(mibFiles[i]);
            } catch (MibLoaderException e) {
                e.printStackTrace();
                e.getLog().printTo(System.out);
            }
        }
    }

}
