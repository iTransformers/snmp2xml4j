<!--
  ~ devCert2.xslt
  ~
  ~ This work is free software; you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published
  ~ by the Free Software Foundation; either version 2 of the License,
  ~ or (at your option) any later version.
  ~
  ~ This work is distributed in the hope that it will be useful, but
  ~ WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program; if not, write to the Free Software
  ~ Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
  ~ USA
  ~
  ~ Copyright (c) 2010-2016 iTransformers Labs. All rights reserved.
  -->

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                xmlns:functx="http://www.functx.com"
                xmlns:snmpwalk="net.itransformers.snmptoolkit.Walk"
        >
    <xsl:output method="text" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:param name="DeviceOS"/>
    <xsl:variable name="DeviceOperatingSystem" select="$DeviceOS"/>

    <xsl:template match="/">
        HEADER: v7
        <xsl:for-each select="/root/objects/object">

        OBJECT:
            object: Change me this is the snmpMib Object name: <xsl:value-of select="@name"/>
            os: <xsl:value-of select="$DeviceOperatingSystem"/>
            index: .<xsl:value-of select="indicators/indicator[1]/oid"/>
            octets: <xsl:for-each select="indexes/index/syntax/@syntax">
        <xsl:variable name="syntax" select="."/>
        <xsl:choose>
            <xsl:when test="$syntax='INTEGER'">i</xsl:when>
            <xsl:when test="$syntax='OCTET STRING'">s</xsl:when>
            <xsl:otherwise>S</xsl:otherwise>
        </xsl:choose>
        </xsl:for-each>
            reverse: 1
            singleton: 0
            name: <xsl:text></xsl:text><xsl:value-of select="@name"/><xsl:text></xsl:text>
            description:  TIDY me up:  <xsl:value-of select="description"/>
            variables:
            assert:
            subtype:
            lastChangeOid:
            adminStatusExpression:
            operStatusExpression:<xsl:for-each select="indicators/indicator">
            INDICATOR:
                indicator: <xsl:value-of select="@name"/>
                description: <xsl:value-of select="description"/>
                expression: .<xsl:value-of select="oid"/>
                type: <xsl:variable name="syntaxType" select="snmpSyntax/@type"/>
                       <xsl:choose>
                           <xsl:when test="$syntaxType='Gauge32' or $syntaxType='Gauge64'">GAUGE</xsl:when>
                           <xsl:when test="$syntaxType='Counter32'">COUNTER32</xsl:when>
                           <xsl:when test="$syntaxType='Counter64'">COUNTER64</xsl:when>
                           <xsl:when test="$syntaxType='Counter64'">COUNTER64</xsl:when>
                           <xsl:when test="$syntaxType='Unsigned32'">GAUGE</xsl:when>
                           <xsl:when test="$syntaxType='Unsigned64'">GAUGE</xsl:when>

                           <xsl:otherwise>GAUGE</xsl:otherwise>
                       </xsl:choose>
                default: 1
                dataunits: Change ME: <xsl:choose><xsl:when test="@units!='null'"><xsl:value-of select="@units"/></xsl:when><xsl:otherwise>Number,Percent,Bytes,Bits...</xsl:otherwise></xsl:choose>
                percentable: 0
                max:  <!--TODO there are such constraints on many of the oids-->
                maxunits: Change ME if Necesary!
                units: Change ME: <xsl:choose><xsl:when test="@units!='null'"><xsl:value-of select="@units"/></xsl:when><xsl:otherwise>Number,Percent,Bytes,Bits...</xsl:otherwise></xsl:choose>
        </xsl:for-each>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
