<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                xmlns:functx="http://www.functx.com"
                xmlns:snmpwalk="net.itransformers.snmptoolkit.Walk"
        >
    <xsl:output method="text" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <xsl:variable name="root" select="//."/>
        <xsl:variable name="objects" select="distinct-values(//object/@name)"/>
        <xsl:for-each select="$objects">
            <xsl:text>Object name: </xsl:text><xsl:value-of select="."/><xsl:text>
            </xsl:text>
            <xsl:variable name="objectName" select="."/>
            <xsl:for-each select="distinct-values($root//object[@name=$objectName]/indexes/index/@name)">
                <xsl:variable name="indexName" select="."/>
                <xsl:text>Index name: </xsl:text><xsl:value-of select="$indexName"/><xsl:text>
            </xsl:text>
                <xsl:text>Index: </xsl:text>
                <xsl:for-each select="$root//object[@name=$objectName][1]/indexes/index[@name=$indexName]">
                    <xsl:choose>
                        <xsl:when test="@primitiveSyntax='INTEGER'">
                            <xsl:text>i</xsl:text>
                        </xsl:when>
                        <xsl:when test="@primitiveSyntax='OCTET STRING'">
                            <xsl:text>s</xsl:text>
                        </xsl:when>
                        <xsl:when test="@primitiveSyntax='STRING IMPLIED'">
                            <xsl:text>S</xsl:text>
                        </xsl:when>
                    </xsl:choose>
                </xsl:for-each>
            <xsl:text>
            </xsl:text>
                <xsl:text>Indexed by: </xsl:text>
                <xsl:value-of
                        select="distinct-values($root//object[@name=$objectName][1]/indexes/index[@name=$indexName and @access!='not-accesible']/@oid)"/>
            </xsl:for-each>
            <xsl:text>
                </xsl:text>
            <xsl:for-each select="distinct-values($root//object[@name=$objectName]/indicator/@name)">
                <xsl:variable name="indicatorName" select="."/>
                <xsl:text>Indicator name: </xsl:text><xsl:value-of select="$indicatorName"/><xsl:text>
                </xsl:text>
                <xsl:for-each select="$root//object[@name=$objectName]/indicator[@name=$indicatorName]">
                    <xsl:text>Indicator oid: </xsl:text><xsl:value-of select="@oid"/><xsl:text>
                </xsl:text>
                    <xsl:text>Indicator syntax: </xsl:text><xsl:value-of select="@snmpSyntax"/><xsl:text>
                </xsl:text>
                    <xsl:text>Indicator description: </xsl:text><xsl:value-of select="@description"/><xsl:text>
                </xsl:text>
                </xsl:for-each>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>