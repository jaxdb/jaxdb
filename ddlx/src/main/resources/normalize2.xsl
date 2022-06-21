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
  xmlns="http://www.jaxdb.org/ddlx-0.5.xsd"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:ddlx="http://www.jaxdb.org/ddlx-0.5.xsd"
  xmlns:function="http://www.jaxdb.org/ddlx.xsl"
  exclude-result-prefixes="function ddlx xs"
  version="2.0">

  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
  <xsl:strip-space elements="*"/>
  
  <xsl:variable name="noTables" select="count(/ddlx:schema/ddlx:table)"/>
  
  <!-- This function is used to sort the tables in topological dependency order -->
  <xsl:function name="function:computeWeight" as="xs:integer*">
    <xsl:param name="node"/>
    <xsl:param name="i"/>
    <!-- generate a sequence containing the weights of each node I reference -->
    <xsl:variable name="weights1" as="xs:integer*">
      <xsl:sequence select="0"/>
      <xsl:for-each select="$node/ddlx:column/ddlx:foreignKey/@references">
        <xsl:value-of select="function:computeWeight(/ddlx:schema/ddlx:table[@name=current() and $node/@name!=current()], $i + 1)"/>
      </xsl:for-each>
    </xsl:variable>
    <xsl:variable name="weights2" as="xs:integer*">
      <xsl:sequence select="0"/>
      <xsl:for-each select="$node/ddlx:constraints/ddlx:foreignKey/@references">
        <xsl:value-of select="function:computeWeight(/ddlx:schema/ddlx:table[@name=current() and $node/@name!=current()], $i + 1)"/>
      </xsl:for-each>
    </xsl:variable>
    <!-- make my weight higher than any of the nodes I reference -->
    <xsl:choose>
      <xsl:when test="$noTables > $i">
        <xsl:value-of select="max($weights1) + max($weights2) + 1"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:message terminate="yes">Loop detected in FOREIGN KEY relationship</xsl:message>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function>
  
  <xsl:function name="function:pad" as="xs:string*">
    <xsl:param name="n"/>
    <xsl:choose>
      <xsl:when test="10 > $n">
        <xsl:value-of select="concat('0000', string($n))"/>
      </xsl:when>
      <xsl:when test="100 > $n">
        <xsl:value-of select="concat('000', string($n))"/>
      </xsl:when>
      <xsl:when test="1000 > $n">
        <xsl:value-of select="concat('00', string($n))"/>
      </xsl:when>
      <xsl:when test="10000 > $n">
        <xsl:value-of select="concat('0', string($n))"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="string($n)"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function>
  
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

  <xsl:template match="/ddlx:schema">
    <schema xmlns:sqlx="http://www.jaxdb.org/sqlx-0.5.xsd" xmlns:jsql="http://www.jaxdb.org/jsql-0.5.xsd">
      <xsl:apply-templates select="ddlx:template"/>
      <xsl:for-each select="ddlx:table">
        <xsl:sort select="function:pad(function:computeWeight(., 0))" order="ascending"/>
        <table>
          <xsl:copy-of select="@*"/>
          <xsl:for-each select="ddlx:column">
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
          </xsl:for-each>
          <xsl:copy-of select="*[not(local-name(.)='column')]"/>
        </table>
      </xsl:for-each>
    </schema>
  </xsl:template>

</xsl:stylesheet>