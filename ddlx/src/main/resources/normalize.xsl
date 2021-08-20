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
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:ddlx="http://www.jaxdb.org/ddlx-0.5.xsd"
  exclude-result-prefixes="ddlx"
  version="1.0">

  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
  <xsl:strip-space elements="*"/>

  <xsl:key name="template" match="/ddlx:schema/ddlx:template[@name]" use="@name"/>

  <xsl:template match="node() | @*">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="/ddlx:schema/ddlx:template"/>

  <xsl:template match="/ddlx:schema/ddlx:table/ddlx:column[@template]">
    <xsl:variable name="template" select="key('template', @template)"/>
    <xsl:copy>
      <xsl:copy-of select="$template/@*"/>
      <xsl:copy-of select="@*[local-name() != 'template']"/>
      <xsl:copy-of select="*"/>
      <xsl:copy-of select="$template/ddlx:check"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="/ddlx:schema/ddlx:table/ddlx:column[not(@template)]">
    <xsl:copy-of select="."/>
  </xsl:template>

</xsl:stylesheet>