<!--
  Copyright (c) 2021 JAX-DB

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

  You should have received a copy of The MIT License (MIT) along with this
  program. If not, see <http://opensource.org/licenses/MIT/>.
-->
<xsl:stylesheet
  xmlns="http://www.jaxdb.org/ddlx-0.6.xsd"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:ddlx="http://www.jaxdb.org/ddlx-0.6.xsd"
  exclude-result-prefixes="ddlx"
  version="2.0">

  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
  <xsl:strip-space elements="*"/>

  <xsl:key name="template" match="/ddlx:schema/ddlx:template[@name]" use="@name"/>

  <xsl:template match="node() | @*">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="/ddlx:schema/ddlx:template">
    <xsl:if test="local-name-from-QName(xs:QName(@xsi:type)) = 'enum'">
      <xsl:copy>
        <xsl:copy-of select="@name"/>
        <xsl:copy-of select="@xsi:type"/>
        <xsl:copy-of select="@values"/>
      </xsl:copy>
    </xsl:if>
  </xsl:template>

  <xsl:template match="/ddlx:schema/ddlx:table/ddlx:column">
    <xsl:choose>
      <xsl:when test="@template">
        <xsl:variable name="template" select="key('template', @template)"/>
        <xsl:copy>
          <xsl:choose>
            <xsl:when test="local-name-from-QName(xs:QName(@xsi:type)) = 'enum'">
              <xsl:copy-of select="$template/@*[local-name() != 'values']"/>
              <xsl:copy-of select="@*"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:copy-of select="$template/@*"/>
              <xsl:copy-of select="@*[local-name() != 'template']"/>
            </xsl:otherwise>
          </xsl:choose>
          <xsl:choose>
            <xsl:when test="ddlx:documentation">
              <xsl:choose>
                <xsl:when test="$template/ddlx:documentation">
                  <documentation>
                    <xsl:value-of select="concat($template/ddlx:documentation/text(), '&#xa;', ddlx:documentation/text())"/>
                  </documentation>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:copy-of select="ddlx:documentation"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:when>
            <xsl:when test="$template/ddlx:documentation">
              <xsl:copy-of select="$template/ddlx:documentation"/>
            </xsl:when>
          </xsl:choose>
          <xsl:copy-of select="*[local-name() != 'documentation']"/>
          <xsl:copy-of select="$template/ddlx:check"/>
        </xsl:copy>
      </xsl:when>
      <xsl:otherwise>
        <xsl:copy-of select="."/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>