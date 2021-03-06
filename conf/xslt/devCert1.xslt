<?xml version="1.0" encoding="UTF-8"?>

<!--
  ~ devCert1.xslt
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
                xmlns:functx="http://www.functx.com"
        >
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <xsl:variable name="root" select="//."/>
        <root>
            <xsl:variable name="objectNames"
                          select="distinct-values(//.[@snmpSyntax='Counter' or @snmpSyntax='Gauge' or @snmpSyntax='Counter32'or @snmpSyntax='Counter64' or @snmpSyntax='Gauge32' or @snmpSyntax='Gauge64' or @snmpSyntax='Unsigned32' or @snmpSyntax='Unsigned64']/../name())"/>
            <!--Loop over potential objects -->
            <objects>
                <xsl:for-each select="$objectNames">
                    <xsl:variable name="objectName" select="(.)"/>
                    <object><xsl:attribute name="name" select="$objectName"/>
                        <description><xsl:value-of select="distinct-values($root//.[name() = $objectName]/description)"/></description>
                        <!--Loop on indexes -->
                        <indexes>
                            <xsl:for-each select="distinct-values($root//.[name() = $objectName]/index/@name)">
                                <xsl:variable name="indexName" select="(.)"/>
                                <index><xsl:attribute name="name" select="$indexName"/>
                                    <xsl:for-each select="distinct-values($root//.[name() = $objectName]/index[@name=$indexName]/./@oid)">
                                        <xsl:variable name="test" select="."/>
                                        <oid><xsl:attribute name="value" select="$test"/></oid>
                                    </xsl:for-each>
                                    <xsl:for-each select="distinct-values($root//.[name() = $objectName]/index[@name=$indexName]/./@primitiveSyntax)">
                                        <xsl:variable name="test" select="."/>
                                        <syntax><xsl:attribute name="syntax" select="$test"/></syntax>
                                    </xsl:for-each>
                                    <xsl:for-each select="distinct-values($root//.[name() = $objectName]/index[@name=$indexName]/./@access)">
                                        <xsl:variable name="test" select="."/>
                                        <access><xsl:attribute name="access" select="$test"/></access>
                                    </xsl:for-each>
                                    <values>
                                        <xsl:for-each select="$root//.[name() = $objectName]/index[@name=$indexName]">
                                            <xsl:variable name="test" select="."/>
                                            <value><xsl:attribute name="access" select="$test"/></value>
                                        </xsl:for-each>
                                    </values>
                                </index>
                            </xsl:for-each>
                        </indexes>

                        <!--Loop over performance Indicators-->
                        <indicators>
                            <xsl:variable name="performanceIndicators" select="distinct-values($root//.[name()=$objectName]//.[name()!='index' and name()!='instance' and name()!='' and (@snmpSyntax='Counter' or @snmpSyntax='Gauge' or @snmpSyntax='Counter32'or @snmpSyntax='Counter64' or @snmpSyntax='Gauge32' or @snmpSyntax='Gauge64' or @snmpSyntax='Unsigned32' or @snmpSyntax='Unsigned64') ]/name())"/>
                            <xsl:for-each select="$performanceIndicators">
                                <xsl:variable name="name" select="(.)"/>
                                <indicator><xsl:attribute name="name"  select="$name"/>

                                    <xsl:variable name="oids">
                                        <oids>
                                            <xsl:for-each select="$root//.[name() = $objectName]//.[name()=$name]">
                                                <xsl:variable name="instance1" select="../instance"/>
                                                <xsl:variable name="test" select="@oid"/>
                                                <xsl:choose>
                                                    <xsl:when test="$instance1!=''">
                                                        <xsl:variable name="oid" select="functx:substring-before-last-match(functx:substring-before-last-match($test,$instance1),'.')"/>
                                                        <oid><xsl:value-of select="$oid"/></oid>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <oid><xsl:value-of select="$test"/></oid>
                                                    </xsl:otherwise>
                                                </xsl:choose>

                                            </xsl:for-each>
                                        </oids>
                                    </xsl:variable>
                                    <!--<xsl:copy-of select="$oids"/>-->
                                    <oid><xsl:value-of select="$oids//oid[1]/text()"/></oid>
                                    <!--</xsl:variable>-->
                                    <!--<oid><xsl:attribute name="id" select="distinct-values($oids1/oid)"/></oid>-->
                                    <xsl:for-each select="distinct-values($root//.[name() = $objectName]//.[name()=$name]/@access)">
                                        <xsl:variable name="test" select="."/>
                                        <access><xsl:attribute name="type" select="$test"/></access>
                                    </xsl:for-each>
                                    <xsl:for-each select="distinct-values($root//.[name() = $objectName]//.[name()=$name]/@units)">
                                        <xsl:variable name="test" select="."/>
                                        <units><xsl:attribute name="type" select="$test"/></units>
                                    </xsl:for-each>
                                    <xsl:for-each select="distinct-values($root//.[name() = $objectName]//.[name()=$name]/@snmpSyntax)">
                                        <xsl:variable name="test" select="."/>
                                        <snmpSyntax><xsl:attribute name="type" select="$test"/></snmpSyntax>
                                    </xsl:for-each>
                                    <xsl:for-each select="distinct-values($root//.[name() = $objectName]//.[name()=$name]/description)">
                                        <xsl:variable name="test" select="."/>
                                        <description><xsl:value-of select="$test"/> </description>
                                    </xsl:for-each>
                                </indicator>
                            </xsl:for-each>
                        </indicators>
                        <otherOids>
                        <xsl:variable name="otherOids" select="distinct-values($root//.[name()=$objectName]//.[name()!='index' and name()!='instance' and name()!='' and ( @snmpSyntax!='Counter' and @snmpSyntax!='Gauge'and @snmpSyntax!='Counter32'and @snmpSyntax!='Counter64' and @snmpSyntax!='Gauge32' and @snmpSyntax!='Gauge64' and  @snmpSyntax!='Unsigned32' and @snmpSyntax!='Unsigned64'and @snmpSyntax!='SEQUENCE') ]/name())"/>
                        <xsl:for-each select="$otherOids">
                            <xsl:variable name="name" select="(.)"/>
                            <otherOid><xsl:attribute name="name"  select="$name"/>
                                <xsl:variable name="oids">
                                    <oids>
                                        <xsl:for-each select="$root//.[name() = $objectName]//.[name()=$name]">
                                            <xsl:variable name="instance1" select="../instance"/>
                                            <xsl:variable name="test" select="@oid"/>
                                            <xsl:choose>
                                                <xsl:when test="$instance1!=''">
                                                    <xsl:variable name="oid" select="functx:substring-before-last-match(functx:substring-before-last-match($test,$instance1),'.')"/>
                                                    <oid><xsl:value-of select="$oid"/></oid>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:variable name="instance1" select="../instance/@instanceIndex"/>

                                                    <xsl:choose>
                                                        <xsl:when test="$instance1!=''">
                                                            <xsl:variable name="oid" select="functx:substring-before-last-match(functx:substring-before-last-match($test,$instance1),'.')"/>
                                                            <oid><xsl:value-of select="$oid"/></oid>
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                            <oid><xsl:value-of select="$test"/></oid>

                                                        </xsl:otherwise>
                                                    </xsl:choose>

                                                </xsl:otherwise>
                                            </xsl:choose>

                                        </xsl:for-each>
                                    </oids>
                                </xsl:variable>
                                <!--<xsl:copy-of select="$oids"/>-->
                                <oid><xsl:value-of select="$oids//oid[1]/text()"/></oid>
                                <!--</xsl:variable>-->
                                <!--<oid><xsl:attribute name="id" select="distinct-values($oids1/oid)"/></oid>-->
                                <xsl:for-each select="distinct-values($root//.[name() = $objectName]//.[name()=$name]/@access)">
                                    <xsl:variable name="test" select="."/>
                                    <access><xsl:attribute name="type" select="$test"/></access>
                                </xsl:for-each>
                                <xsl:for-each select="distinct-values($root//.[name() = $objectName]//.[name()=$name]/@units)">
                                    <xsl:variable name="test" select="."/>
                                    <units><xsl:attribute name="type" select="$test"/></units>
                                </xsl:for-each>

                                <xsl:for-each select="distinct-values($root//.[name() = $objectName]//.[name()=$name]/@snmpSyntax)">
                                    <xsl:variable name="test" select="."/>
                                    <snmpSyntax><xsl:attribute name="type" select="$test"/></snmpSyntax>
                                </xsl:for-each>
                                <xsl:for-each select="distinct-values($root//.[name() = $objectName]//.[name()=$name]/description)">
                                    <xsl:variable name="test" select="."/>
                                    <description><xsl:value-of select="$test"/></description>
                                </xsl:for-each>
                            </otherOid>
                        </xsl:for-each>
                    </otherOids>

                    </object>
                </xsl:for-each>
            </objects>
        </root>
    </xsl:template>
    <xsl:function name="functx:substring-before-last-match" as="xs:string?" xmlns:functx="http://www.functx.com">
        <xsl:param name="arg" as="xs:string?"/>
        <xsl:param name="regex" as="xs:string"/>
        <xsl:sequence select="
   replace($arg,concat('^(.*)',$regex,'.*'),'$1')
 "/>
    </xsl:function>
    <xsl:function name="functx:substring-after-last-match" as="xs:string"
                  xmlns:functx="http://www.functx.com">
        <xsl:param name="arg" as="xs:string?"/>
        <xsl:param name="regex" as="xs:string"/>

        <xsl:sequence select="
   replace($arg,concat('^.*',$regex),'')
 "/>
    </xsl:function>
</xsl:stylesheet>
