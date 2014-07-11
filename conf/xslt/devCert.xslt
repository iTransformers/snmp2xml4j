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

    <xsl:template match="/">
        <xsl:variable name="root" select="//."/>
        <root>
            <xsl:variable name="objectNames"
                          select="distinct-values(//.[@snmpSyntax='Counter32'or @snmpSyntax='Counter64' or @snmpSyntax='Gauge32' or @snmpSyntax='Gauge64']/../name())"/>
            <!--Loop over potential objects -->
            <objects>
            <xsl:for-each select="$objectNames">
                <xsl:variable name="objectName" select="string(.)"/>
                <object><xsl:attribute name="name" select="$objectName"/>
                <!--Loop on indexes -->
                <xsl:for-each select="distinct-values($root//.[name() = $objectName]/index/@name)">
                    <xsl:variable name="indexName" select="string(.)"/>
                    <index><xsl:attribute name="name" select="$indexName"/>
                        <xsl:for-each select="distinct-values($root//.[name() = $objectName][1]/index[@name=$indexName]/./@oid)">
                            <xsl:variable name="test" select="."/>
                            <oid><xsl:attribute name="value" select="$test"/></oid>
                        </xsl:for-each>
                    <xsl:for-each select="distinct-values($root//.[name() = $objectName][1]/index[@name=$indexName]/./@primitiveSyntax)">
                        <xsl:variable name="test" select="."/>
                        <syntax><xsl:attribute name="syntax" select="$test"/></syntax>
                    </xsl:for-each>
                    <xsl:for-each select="distinct-values($root//.[name() = $objectName][1]/index[@name=$indexName]/./@access)">
                        <xsl:variable name="test" select="."/>
                        <access><xsl:attribute name="access" select="$test"/></access>
                    </xsl:for-each>
                    <values>
                    <xsl:for-each select="$root//.[name() = $objectName][1]/index[@name=$indexName]">
                            <xsl:variable name="test" select="."/>
                            <value><xsl:attribute name="access" select="$test"/></value>
                    </xsl:for-each>
                    </values>
                    </index>
                    <!--Loop over performance Indicators-->
                    <indicators>
                    <xsl:variable name="performanceIndicators" select="distinct-values($root//.[name()=$objectName]//.[name()!='index' and name()!='instance' and name()!='' and (@snmpSyntax='Counter32'or @snmpSyntax='Counter64' or @snmpSyntax='Gauge32' or @snmpSyntax='Gauge64') ]/name())"/>
                    <xsl:for-each select="$performanceIndicators">
                        <xsl:variable name="name" select="."/>
                        <indicator><xsl:attribute name="name" select="$name"/>
                            <xsl:for-each select="$root//.[name() = $objectName]//.[name()=$name]/@oid">
                                <xsl:variable name="test" select="."/>
                                <oid><xsl:attribute name="id" select="$test"/></oid>
                            </xsl:for-each>
                            <xsl:for-each select="distinct-values($root//.[name() = $objectName]//.[name()=$name]/@access)">
                                <xsl:variable name="test" select="."/>
                                <access><xsl:attribute name="type" select="$test"/></access>
                            </xsl:for-each>
                            <xsl:for-each select="distinct-values($root//.[name() = $objectName]//.[name()=$name]/@snmpSyntax)">
                                <xsl:variable name="test" select="."/>
                                <snmpSyntax><xsl:attribute name="type" select="$test"/></snmpSyntax>
                            </xsl:for-each>
                        </indicator>
                    </xsl:for-each>
                    </indicators>

                </xsl:for-each>
                </object>
            </xsl:for-each>
            </objects>
        </root>
        <!--<xsl:for-each select="$root//ifEntry/$uniqueIndicatorName">-->
        <!--&lt;!&ndash;<xsl:copy-of select="."/>&ndash;&gt;-->
        <!--<object>-->
        <!--<xsl:attribute name="name"><xsl:value-of select="../name()"/></xsl:attribute>-->
        <!--<xsl:attribute name="description"><xsl:value-of select="../@description"/></xsl:attribute>-->
        <!--<indexes>-->
        <!--<xsl:for-each select="../index">-->
        <!--<index>-->
        <!--<xsl:attribute name="oid"><xsl:value-of select="@oid"/></xsl:attribute>-->
        <!--<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>-->
        <!--<xsl:attribute name="primitiveSyntax"><xsl:value-of select="@primitiveSyntax"/></xsl:attribute>-->
        <!--<xsl:attribute name="access"><xsl:value-of select="@access"/></xsl:attribute>-->
        <!--</index>-->
        <!--</xsl:for-each>-->
        <!--</indexes>-->
        <!--<indicator>-->
        <!--<xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>-->
        <!--<xsl:attribute name="oid"><xsl:value-of select="functx:substring-before-last-match(functx:substring-before-last-match(@oid,../instance),'.')"/></xsl:attribute>-->
        <!--<xsl:attribute name="snmpSyntax"><xsl:value-of select="@snmpSyntax"/></xsl:attribute>-->
        <!--<xsl:attribute name="primitiveSyntax"><xsl:value-of select="@primitiveSyntax"/></xsl:attribute>-->
        <!--<xsl:attribute name="desciption"><xsl:value-of select="@desciption"/></xsl:attribute>-->
        <!--<xsl:attribute name="access"><xsl:value-of select="@access"/></xsl:attribute>-->
        <!--</indicator>-->
        <!--</object>-->
        <!--</xsl:for-each>-->
        <!--</xsl:for-each>-->
        <!--</root>-->
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
