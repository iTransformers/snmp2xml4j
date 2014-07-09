<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ Copyright (c) 2014. iTransformers Labs http://itransformers.net
  ~
  ~ snmp2xml is an open source tool written by Vasil Yordanov and Nikolay Milovanov
  ~ in JAVA programing languadge.
  ~
  ~ This program is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ any later version.
  ~
  ~ This program is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program.  If not, see <http://www.gnu.org/licenses/>.
  -->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                xmlns:functx="http://www.functx.com"
                xmlns:snmpwalk="net.itransformers.snmptoolkit.Walk"
               >
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:param name="ipAddress"/>
    <xsl:param name="status"/>
    <xsl:param name="community-ro"/>
    <xsl:param name="community-rw"/>
    <xsl:param name="oid"/>

    <xsl:template match="/">
         <xsl:for-each select="//.[@oid = $oid]">

         </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
