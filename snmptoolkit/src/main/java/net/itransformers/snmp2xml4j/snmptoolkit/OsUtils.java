/*
 * OsUtils.java
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
 * <p>OsUtils class.</p>
 *
 * @author niau
 * @version $Id: $Id
 */
public  final class OsUtils
{
   private static String OS = null;
   /**
    * <p>getOsName.</p>
    *
    * @return a {@link java.lang.String} object.
    */
   public static String getOsName()
   {
      if(OS == null) { OS = System.getProperty("os.name"); }
      return OS;
   }
   /**
    * <p>isWindows.</p>
    *
    * @return a boolean.
    */
   public static boolean isWindows()
   {
      return getOsName().startsWith("Windows");
   }

   /**
    * <p>isMac.</p>
    *
    * @return a boolean.
    */
   public static boolean isMac(){return getOsName().startsWith("Mac");}
   /**
    * <p>isLinux.</p>
    *
    * @return a boolean.
    */
   public static  boolean isLinux(){return getOsName().startsWith("Linux");}
}
