<!--
  Copyright (c) 2022 JAX-DB

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
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:ddlx="http://www.jaxdb.org/ddlx-0.5.xsd"
  xmlns:function="http://www.jaxdb.org/merge.xsl"
  exclude-result-prefixes="ddlx function xs"
  version="2.0">

  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
  <xsl:strip-space elements="*"/>

  <xsl:function name="function:merge">
    <xsl:param name="table"/>
    <xsl:param name="columns"/>
    <xsl:param name="primaryKeys"/>
    <xsl:param name="foreignKeys"/>
    <xsl:param name="uniques"/>
    <xsl:param name="checks"/>
    <xsl:param name="triggers"/>
    <xsl:param name="indexes"/>
    <xsl:param name="others"/>
    <xsl:variable name="columnsAll" select="$columns | $table/*[local-name() = 'column']"/>
    <xsl:variable name="primaryKeysAll" select="$primaryKeys | $table/ddlx:constraints/*[local-name() = 'primaryKey']"/>
    <xsl:variable name="foreignKeysAll" select="$foreignKeys | $table/ddlx:constraints/*[local-name() = 'foreignKey']"/>
    <xsl:variable name="uniquesAll" select="$uniques | $table/ddlx:constraints/*[local-name() = 'unique']"/>
    <xsl:variable name="checksAll" select="$checks | $table/ddlx:constraints/*[local-name() = 'check']"/>
    <xsl:variable name="triggersAll" select="$triggers | $table/ddlx:triggers/*"/>
    <xsl:variable name="indexesAll" select="$indexes | $table/ddlx:indexes/*"/>
    <xsl:variable name="othersAll" select="$others | $table/*[local-name() != 'column' and local-name() != 'constraints' and local-name() != 'triggers' and local-name() != 'indexes']"/>
    <xsl:for-each select="$table/@extends">
      <xsl:variable name="super" select="/ddlx:schema/ddlx:table[@name = current() and $table/@name != current()]"/>
      <xsl:copy-of select="function:merge($super, $columnsAll, $primaryKeysAll, $foreignKeysAll, $uniquesAll, $checksAll, $triggersAll, $indexesAll, $othersAll)"/>
    </xsl:for-each>
    <xsl:if test="not($table/@extends)">
      <xsl:if test="$columnsAll">
        <xsl:for-each select="$columnsAll">
          <xsl:copy-of select="."/>
        </xsl:for-each>
      </xsl:if>
      <xsl:if test="$primaryKeysAll | $foreignKeysAll | $uniquesAll | $checksAll">
        <ddlx:constraints>
          <xsl:for-each select="$primaryKeysAll">
            <xsl:copy-of select="."/>
          </xsl:for-each>
          <xsl:for-each select="$foreignKeysAll">
            <xsl:copy-of select="."/>
          </xsl:for-each>
          <xsl:for-each select="$uniquesAll">
            <xsl:copy-of select="."/>
          </xsl:for-each>
          <xsl:for-each select="$checksAll">
            <xsl:copy-of select="."/>
          </xsl:for-each>
        </ddlx:constraints>
      </xsl:if>
      <xsl:if test="$triggersAll">
        <ddlx:triggers>
          <xsl:for-each select="$triggersAll">
            <xsl:copy-of select="."/>
          </xsl:for-each>
        </ddlx:triggers>
      </xsl:if>
      <xsl:if test="$indexesAll">
        <ddlx:indexes>
          <xsl:for-each select="$indexesAll">
            <xsl:copy-of select="."/>
          </xsl:for-each>
        </ddlx:indexes>
      </xsl:if>
      <xsl:if test="$othersAll">
        <xsl:for-each select="$othersAll">
          <xsl:copy-of select="."/>
        </xsl:for-each>
      </xsl:if>
    </xsl:if>
  </xsl:function>

  <xsl:template match="/ddlx:schema">
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:copy-of select="*[local-name() = 'template']"/>
      <xsl:for-each select="ddlx:table">
        <xsl:if test="not(@abstract)">
          <xsl:copy>
            <xsl:copy-of select="@* except @extends"/>
            <xsl:copy-of select="function:merge(., ./*[local-name() = 'column'], ./ddlx:constraints/*[local-name() = 'primaryKey'], ./ddlx:constraints/*[local-name() = 'foreignKey'], ./ddlx:constraints/*[local-name() = 'unique'], ./ddlx:constraints/*[local-name() = 'check'], ./ddlx:triggers/*, ./ddlx:indexes/*, ./*[local-name() != 'column' and local-name() != 'constraints' and local-name() != 'triggers' and local-name() != 'indexes'])"/>
          </xsl:copy>
        </xsl:if>
      </xsl:for-each>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>