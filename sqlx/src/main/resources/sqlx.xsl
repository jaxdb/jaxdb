<!--
  Copyright (c) 2016 JAX-DB

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
  xmlns:dt="http://www.jaxdb.org/datatypes-0.5.xsd"
  xmlns:ddlx="http://www.jaxdb.org/ddlx-0.5.xsd"
  xmlns:sqlx="http://www.jaxdb.org/sqlx-0.5.xsd"
  xmlns:function="http://www.jaxdb.org/sqlx.xsl"
  xmlns:ext="http://exslt.org/common"
  xmlns:math="http://www.w3.org/2005/xpath-functions/math"
  xmlns:saxon="http://saxon.sf.net/"
  xmlns:annox="http://annox.dev.java.net"
  xmlns:jaxb="http://java.sun.com/xml/ns/jaxb"
  exclude-result-prefixes="ext function math saxon ddlx xs xsi"
  version="2.0">

  <xsl:variable name="database" select="function:file-name(base-uri())"/>

  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
  <xsl:strip-space elements="*"/>

  <xsl:variable name="namespace">
    <xsl:if test="$database=''">
      <xsl:message select="base-uri()"></xsl:message>
      <xsl:message select="function:file-name(base-uri())"></xsl:message>
      <xsl:message terminate="yes">Missing variable: database</xsl:message>
    </xsl:if>
    <xsl:value-of select="concat('urn:jaxdb:sqlx:', $database)"/>
  </xsl:variable>

  <xsl:variable name="xmlns">
    <xsl:element name="ns:x" namespace="{$namespace}"/>
  </xsl:variable>

  <xsl:variable name="noTables" select="count(/ddlx:schema/ddlx:table)"/>

  <!-- This function is used to sort the tables in topological dependency order -->
  <xsl:function name="function:computeWeight" as="xs:integer*">
    <xsl:param name="node"/>
    <xsl:param name="i"/>
    <!-- generate a sequence containing the weights of each node I reference -->
    <xsl:variable name="weights" as="xs:integer*">
      <xsl:sequence select="0"/>
      <xsl:for-each select="$node/ddlx:column/ddlx:foreignKey/@references">
        <xsl:value-of select="function:computeWeight(/ddlx:schema/ddlx:table[@name=current() and $node/@name!=current()], $i + 1)"/>
      </xsl:for-each>
    </xsl:variable>
    <!-- make my weight higher than any of the nodes I reference -->
    <xsl:choose>
      <xsl:when test="$noTables > $i">
        <xsl:value-of select="max($weights) + 1"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:message terminate="yes">Loop detected in FOREIGN KEY relationship</xsl:message>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function>

  <xsl:function name="function:getPrimaryKey">
    <xsl:param name="node" as="element()"/>
    <xsl:sequence select="$node/ddlx:constraints/ddlx:primaryKey"/>
  </xsl:function>

  <xsl:function name="function:camel-case">
    <xsl:param name="string"/>
    <xsl:value-of select="string-join(for $s in tokenize($string, '\W+') return concat(upper-case(substring($s, 1, 1)), substring($s, 2)), '')"/>
  </xsl:function>

  <xsl:function name="function:instance-case">
    <xsl:param name="string"/>
    <xsl:value-of select="concat(lower-case(substring($string, 1, 1)), substring(function:camel-case($string), 2))"/>
  </xsl:function>

  <xsl:function name="function:substring-after-last-match" as="xs:string">
    <xsl:param name="arg" as="xs:string?"/>
    <xsl:param name="regex" as="xs:string"/>
    <xsl:sequence select="replace($arg, concat('^.*',$regex), '')"/>
  </xsl:function>

  <xsl:function name="function:file-name">
    <xsl:param name="string"/>
    <xsl:value-of select="substring-before(function:substring-after-last-match($string, '/'), '.')"/>
  </xsl:function>

  <xsl:function name="function:precision-scale">
    <xsl:param name="precision"/>
    <xsl:param name="max"/>
    <xsl:choose>
      <xsl:when test="not($precision)">
        <xsl:value-of select="$max"/>
      </xsl:when>
      <xsl:when test="$precision = 0">
        <xsl:value-of select="0"/>
      </xsl:when>
      <xsl:when test="$max = -1 or math:pow(10, $precision) &lt; $max">
        <xsl:value-of select="substring('99999999999999999999', 1, $precision)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$max"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function>

  <xsl:template match="/ddlx:schema">
    <xsl:if test="ddlx:template">
      <xsl:message terminate="yes">Input schema is not normalized</xsl:message>
    </xsl:if>
    <xs:schema
      jaxb:version="2.1"
      jaxb:extensionBindingPrefixes="annox"
      elementFormDefault="qualified"
      xmlns:ddlx="http://www.jaxdb.org/ddlx-0.5.xsd"
      xmlns:xs="http://www.w3.org/2001/XMLSchema">

      <xsl:copy-of select="ext:node-set($xmlns)/*/namespace::*[.=$namespace]"/>

      <xsl:attribute name="targetNamespace" select="$namespace"/>

      <xs:import namespace="http://www.jaxdb.org/sqlx-0.5.xsd" schemaLocation="http://www.jaxdb.org/sqlx-0.5.xsd"/>
      <xs:import namespace="http://www.jaxdb.org/datatypes-0.5.xsd" schemaLocation="http://www.jaxdb.org/datatypes-0.5.xsd"/>

      <xsl:for-each select="ddlx:table">
        <xs:complexType>
          <xsl:attribute name="name" select="@name"/>
          <xs:annotation>
            <xs:appinfo>
              <annox:annotate>
                <xsl:value-of select="concat('@org.jaxdb.ddlx.annotation.Table(name = &quot;', @name, '&quot;)')"/>
              </annox:annotate>
            </xs:appinfo>
          </xs:annotation>
          <xs:complexContent>
            <xs:extension base="sqlx:row">
              <xsl:variable name="tableName" select="@name"/>
              <xsl:for-each select="ddlx:column">
                <xsl:variable name="type">
                  <xsl:value-of select="function:substring-after-last-match(@xsi:type, ':')"/>
                </xsl:variable>
                <xs:attribute>
                  <xsl:choose>
                    <xsl:when test="@generateOnInsert">
                      <xsl:attribute name="id" select="concat($tableName, '-', @name, '-', @generateOnInsert)"/>
                    </xsl:when>
                    <xsl:when test="@sqlx:generateOnInsert">
                      <xsl:attribute name="id" select="concat($tableName, '-', @name, '-', @sqlx:generateOnInsert)"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:attribute name="id" select="concat($tableName, '-', @name)"/>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:attribute name="name" select="function:instance-case(@name)"/>
                  <xsl:if test="@null='false' and not(@default) and not(@generateOnInsert) and not(@sqlx:generateOnInsert)">
                    <xsl:attribute name="use">required</xsl:attribute>
                  </xsl:if>
                  <xsl:if test="@default">
                    <xsl:attribute name="default" select="@default"/>
                  </xsl:if>
                  <xsl:if test="$type='boolean'">
                    <xsl:attribute name="type">dt:boolean</xsl:attribute>
                  </xsl:if>
                  <xsl:if test="$type='date'">
                    <xsl:attribute name="type">dt:date</xsl:attribute>
                  </xsl:if>
                  <xsl:if test="$type='datetime'">
                    <xsl:attribute name="type">dt:datetime</xsl:attribute>
                  </xsl:if>
                  <xsl:if test="$type='time'">
                    <xsl:attribute name="type">dt:time</xsl:attribute>
                  </xsl:if>
                  <xs:annotation>
                    <xs:appinfo>
                      <annox:annotate>
                        <xsl:value-of select="concat('@org.jaxdb.ddlx.annotation.Column(name = &quot;', @name, '&quot;')"/>
                        <xsl:if test="@generateOnInsert">
                          <xsl:value-of select="concat(', generateOnInsert = &quot;', @generateOnInsert, '&quot;')"/>
                        </xsl:if>
                        <xsl:if test="@sqlx:generateOnInsert">
                          <xsl:value-of select="concat(', generateOnInsert = &quot;', @sqlx:generateOnInsert, '&quot;')"/>
                        </xsl:if>
                        <xsl:text>)</xsl:text>
                      </annox:annotate>
                    </xs:appinfo>
                  </xs:annotation>
                  <xsl:if test="$type='binary' or $type='blob'">
                    <xs:simpleType>
                      <xs:restriction>
                        <xsl:attribute name="base" select="concat('dt:', $type)"/>
                        <xsl:if test="@length">
                          <xs:maxLength>
                            <xsl:attribute name="value" select="@length"/>
                          </xs:maxLength>
                        </xsl:if>
                        <xsl:if test="not(@varying='true') and not($type='blob')">
                          <xs:minLength>
                            <xsl:attribute name="value" select="@length"/>
                          </xs:minLength>
                        </xsl:if>
                      </xs:restriction>
                    </xs:simpleType>
                  </xsl:if>
                  <xsl:if test="$type='char' or $type='clob'">
                    <xs:simpleType>
                      <xs:restriction>
                        <xsl:attribute name="base" select="concat('dt:', $type)"/>
                        <xsl:if test="@length">
                          <xs:maxLength>
                            <xsl:attribute name="value" select="@length"/>
                          </xs:maxLength>
                        </xsl:if>
                        <xsl:if test="not(@varying='true') and not($type='clob')">
                          <xs:minLength>
                            <xsl:attribute name="value" select="@length"/>
                          </xs:minLength>
                        </xsl:if>
                      </xs:restriction>
                    </xs:simpleType>
                  </xsl:if>
                  <xsl:if test="$type='float'">
                    <xs:simpleType>
                      <xs:restriction base="dt:float">
                        <xsl:if test="@max">
                          <xs:maxInclusive>
                            <xsl:attribute name="value" select="@max"/>
                          </xs:maxInclusive>
                        </xsl:if>
                        <xsl:if test="@min">
                          <xs:minInclusive>
                            <xsl:attribute name="value" select="@min"/>
                          </xs:minInclusive>
                        </xsl:if>
                      </xs:restriction>
                    </xs:simpleType>
                  </xsl:if>
                  <xsl:if test="$type='double'">
                    <xs:simpleType>
                      <xs:restriction base="dt:double">
                        <xsl:if test="@max">
                          <xs:maxInclusive>
                            <xsl:attribute name="value" select="@max"/>
                          </xs:maxInclusive>
                        </xsl:if>
                        <xsl:if test="@min">
                          <xs:minInclusive>
                            <xsl:attribute name="value" select="@min"/>
                          </xs:minInclusive>
                        </xsl:if>
                      </xs:restriction>
                    </xs:simpleType>
                  </xsl:if>
                  <xsl:if test="$type='decimal'">
                    <xs:simpleType>
                      <xs:restriction base="dt:decimal">
                        <xs:fractionDigits>
                          <xsl:attribute name="value" select="@scale"/>
                        </xs:fractionDigits>
                        <xs:maxInclusive>
                          <xsl:attribute name="value">
                            <xsl:choose>
                              <xsl:when test="@max">
                                <xsl:value-of select="@max"/>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:value-of select="function:precision-scale(@precision - @scale, -1)"/>
                              </xsl:otherwise>
                            </xsl:choose>
                          </xsl:attribute>
                        </xs:maxInclusive>
                        <xs:minInclusive>
                          <xsl:attribute name="value">
                            <xsl:choose>
                              <xsl:when test="@min">
                                <xsl:value-of select="@min"/>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:value-of select="concat('-', function:precision-scale(@precision - @scale, -1))"/>
                              </xsl:otherwise>
                            </xsl:choose>
                          </xsl:attribute>
                        </xs:minInclusive>
                      </xs:restriction>
                    </xs:simpleType>
                  </xsl:if>
                  <xsl:if test="$type='enum'">
                    <xs:simpleType>
                      <xs:restriction base="dt:enum">
                        <xsl:for-each select="tokenize(replace(@values, '\\ ', '\\`'), ' ')">
                          <xs:enumeration>
                            <xsl:attribute name="value" select="replace(., '\\`', ' ')"/>
                          </xs:enumeration>
                        </xsl:for-each>
                      </xs:restriction>
                    </xs:simpleType>
                  </xsl:if>
                  <xsl:if test="$type='tinyint' or $type='smallint' or $type='int' or $type='bigint'">
                    <xs:simpleType>
                      <xs:restriction>
                        <xsl:attribute name="base" select="concat('dt:', $type)"/>
                        <xs:maxInclusive>
                          <xsl:attribute name="value">
                            <xsl:choose>
                              <xsl:when test="@max">
                                <xsl:value-of select="@max"/>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:value-of select="function:precision-scale(@precision, if ($type='tinyint') then 127 else if ($type='smallint') then 32767 else if ($type='int') then 2147483647 else 9223372036854775807)"/>
                              </xsl:otherwise>
                            </xsl:choose>
                          </xsl:attribute>
                        </xs:maxInclusive>
                        <xs:minInclusive>
                          <xsl:attribute name="value">
                            <xsl:choose>
                              <xsl:when test="@min">
                                <xsl:value-of select="@min"/>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:value-of select="concat('-', function:precision-scale(@precision, if ($type='tinyint') then 128 else if ($type='smallint') then 32768 else if ($type='int') then 2147483648 else 9223372036854775808))"/>
                              </xsl:otherwise>
                            </xsl:choose>
                          </xsl:attribute>
                        </xs:minInclusive>
                      </xs:restriction>
                    </xs:simpleType>
                  </xsl:if>
                </xs:attribute>
              </xsl:for-each>
            </xs:extension>
          </xs:complexContent>
        </xs:complexType>
      </xsl:for-each>

      <xs:complexType>
        <xsl:attribute name="name" select="$database"/>
        <xs:annotation>
          <xs:appinfo>
            <annox:annotate>
              <xsl:value-of select="concat('@org.jaxdb.ddlx.annotation.Schema(name = &quot;', $database, '&quot;)')"/>
            </annox:annotate>
          </xs:appinfo>
        </xs:annotation>
        <xs:complexContent>
          <xs:extension base="sqlx:database">
            <xs:sequence>
              <xs:element minOccurs="0" maxOccurs="unbounded">
                <xsl:attribute name="ref" select="concat('ns:', $database)"/>
              </xs:element>
              <xs:choice minOccurs="0" maxOccurs="unbounded">
                <xsl:for-each select="ddlx:table">
                  <xs:element>
                    <xsl:attribute name="id" select="concat(@name, '-', function:computeWeight(., 0))"/>
                    <xsl:attribute name="name" select="function:instance-case(@name)"/>
                    <xsl:attribute name="type" select="concat('ns:', @name)"/>
                  </xs:element>
                </xsl:for-each>
              </xs:choice>
            </xs:sequence>
          </xs:extension>
        </xs:complexContent>
      </xs:complexType>

      <xs:element>
        <xsl:attribute name="id" select="$database"/>
        <xsl:attribute name="name" select="$database"/>
        <xsl:attribute name="type" select="concat('ns:', $database)"/>
        <xsl:for-each select="ddlx:table">
          <xsl:variable name="primaryKey" select="function:getPrimaryKey(current())"/>
          <xsl:variable name="tableName" select="@name"/>
          <xsl:if test="$primaryKey">
            <xs:key>
              <xsl:attribute name="name" select="concat(function:instance-case($tableName), 'Key')"/>
              <xs:selector>
                <xsl:attribute name="xpath" select="concat('.//ns:', function:instance-case($tableName))"/>
              </xs:selector>
              <xsl:for-each select="$primaryKey/ddlx:column">
                <xs:field>
                  <xsl:attribute name="xpath" select="concat('@', function:instance-case(@name))"/>
                </xs:field>
              </xsl:for-each>
            </xs:key>
          </xsl:if>
          <xsl:for-each select="ddlx:constraints/ddlx:unique | ddlx:column/ddlx:index[@unique='true']">
            <xsl:variable name="index" select="position()"/>
            <xs:unique>
              <xsl:attribute name="name" select="concat('unique', function:instance-case($tableName), $index)"/>
              <xs:selector>
                <xsl:attribute name="xpath" select="concat('.//ns:', function:instance-case($tableName))"/>
              </xs:selector>
              <xsl:if test="ddlx:column">
                <xsl:for-each select="ddlx:column">
                  <xs:field>
                    <xsl:attribute name="xpath" select="concat('@', function:instance-case(@name))"/>
                  </xs:field>
                </xsl:for-each>
              </xsl:if>
              <xsl:if test="local-name()='index'">
                <xs:field>
                  <xsl:attribute name="xpath" select="concat('@', function:instance-case(../@name))"/>
                </xs:field>
              </xsl:if>
            </xs:unique>
          </xsl:for-each>
          <xsl:for-each select="ddlx:column | ddlx:constraints">
            <xsl:if test="ddlx:foreignKey">
              <xs:keyref>
                <xsl:attribute name="refer" select="concat('ns:', function:instance-case(ddlx:foreignKey/@references), 'Key')"/>
                <xsl:attribute name="name" select="concat(function:instance-case(../@name), function:camel-case(ddlx:foreignKey/@references), function:camel-case(ddlx:foreignKey/@column), function:camel-case(@name), 'KeyRef')"/>
                <xs:selector>
                  <xsl:attribute name="xpath" select="concat('.//ns:', function:instance-case(../@name))"/>
                </xs:selector>
                <xsl:choose>
                  <xsl:when test="ddlx:foreignKey/ddlx:column">
                    <xsl:for-each select="ddlx:foreignKey/ddlx:column">
                      <xs:field>
                        <xsl:attribute name="xpath" select="concat('@', function:instance-case(@name))"/>
                      </xs:field>
                    </xsl:for-each>
                  </xsl:when>
                  <xsl:otherwise>
                    <xs:field>
                      <xsl:attribute name="xpath" select="concat('@', function:instance-case(@name))"/>
                    </xs:field>
                  </xsl:otherwise>
                </xsl:choose>
              </xs:keyref>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </xs:element>
    </xs:schema>
  </xsl:template>

</xsl:stylesheet>