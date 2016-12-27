<!--
  Copyright (c) 2016 Seva Safris
  
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
  xmlns:xds="http://xdb.safris.org/xds.xsd"
  xmlns:function="http://xdb.safris.org/xdd.xsl"
  xmlns:ext="http://exslt.org/common" 
  xmlns:math="http://www.w3.org/2005/xpath-functions/math"
  exclude-result-prefixes="function xs xsi ext xds math"
  version="2.0">
  
  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
  
  <xsl:variable name="namespace">
    <xsl:text>xdd.</xsl:text>
    <xsl:value-of select="//xds:schema/@name"/>
  </xsl:variable>  
  
  <xsl:variable name="xmlns">
    <xsl:element name="xdd:x" namespace="{$namespace}"/>
  </xsl:variable>
  
  <xsl:function name="function:camel-case">
    <xsl:param name="string"/>
    <xsl:value-of select="string-join(for $s in tokenize($string, '\W+') return concat(upper-case(substring($s, 1, 1)), substring($s, 2)), '')"/>
  </xsl:function>
  
  <xsl:function name="function:instance-case">
    <xsl:param name="string"/>
    <xsl:value-of select="concat(lower-case(substring($string, 1, 1)), substring(function:camel-case($string), 2))"/>
  </xsl:function>
  
  <xsl:function name="function:precision-scale">
    <xsl:param name="precision"/>
    <xsl:choose>
      <xsl:when test="math:pow(10, $precision) &gt; 9223372036854775807">
        <xsl:value-of select="9223372036854775807"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="format-number(math:pow(10, $precision) - 1, '#')"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function>
  
  <xsl:template match="/xds:schema">
    <xs:schema
      elementFormDefault="qualified"
      xmlns:xds="http://xdb.safris.org/xds.xsd"
      xmlns:xs="http://www.w3.org/2001/XMLSchema">
      
      <xsl:copy-of select="ext:node-set($xmlns)/*/namespace::*[.=$namespace]"/>
      
      <xsl:attribute name="targetNamespace">
        <xsl:value-of select="$namespace"/>
      </xsl:attribute>
      
      <xsl:for-each select="xds:table">
        <xs:complexType>
          <xsl:attribute name="name">
            <xsl:value-of select="function:camel-case(@name)"/>
          </xsl:attribute>
          <xsl:choose>
            <xsl:when test="@extends">
              <xs:complexContent>
                <xs:extension>
                  <xsl:attribute name="base">
                    <xsl:text>xdd:</xsl:text>
                    <xsl:value-of select="function:camel-case(@extends)"/>
                  </xsl:attribute>
                  <xsl:apply-templates select="xds:column"/>
                </xs:extension>
              </xs:complexContent>
            </xsl:when>
            <xsl:otherwise>
              <xsl:apply-templates select="xds:column"/>
            </xsl:otherwise>
          </xsl:choose>
        </xs:complexType>
      </xsl:for-each>
      
      <xs:element name="data">
        <xs:complexType>
          <xs:sequence>
            <xs:choice minOccurs="0" maxOccurs="unbounded">
              <xsl:for-each select="xds:table">
                <xsl:if test="not(@abstract='true')">
                  <xs:element>
                    <xsl:attribute name="name">
                      <xsl:value-of select="function:camel-case(@name)"/>
                    </xsl:attribute>
                    <xsl:attribute name="type">
                      <xsl:text>xdd:</xsl:text>
                      <xsl:value-of select="function:camel-case(@name)"/>
                    </xsl:attribute>
                  </xs:element>
                </xsl:if>
              </xsl:for-each>
            </xs:choice>
          </xs:sequence>
        </xs:complexType>
        <xsl:for-each select="xds:table">
          <xsl:if test="xds:constraints/xds:primaryKey">
            <xs:key>
              <xsl:attribute name="name">
                <xsl:value-of select="concat('key', function:camel-case(@name))"/>
              </xsl:attribute>
              <xs:selector>
                <xsl:attribute name="xpath">
                  <xsl:text>xdd:</xsl:text>
                  <xsl:value-of select="function:camel-case(@name)"/>
                </xsl:attribute>
              </xs:selector>
              <xsl:for-each select="xds:constraints/xds:primaryKey/xds:column">
                <xs:field>
                  <xsl:attribute name="xpath">
                    <xsl:text>@</xsl:text>
                    <xsl:value-of select="function:instance-case(@name)"/>
                  </xsl:attribute>
                </xs:field>
              </xsl:for-each>
            </xs:key>
          </xsl:if>
          <xsl:for-each select="xds:column">
            <xsl:if test="xds:foreignKey">
              <xs:keyref>
                <xsl:attribute name="refer">
                  <xsl:value-of select="concat('xdd:key', function:camel-case(xds:foreignKey/@references))"/>
                </xsl:attribute>
                <xsl:attribute name="name">
                  <xsl:value-of select="concat('keyRef', function:camel-case(../@name), function:camel-case(xds:foreignKey/@references), function:camel-case(xds:foreignKey/@column))"/>
                </xsl:attribute>
                <xs:selector>
                  <xsl:attribute name="xpath">
                    <xsl:text>xdd:</xsl:text>
                    <xsl:value-of select="function:camel-case(../@name)"/>
                  </xsl:attribute>
                </xs:selector>
                <xs:field>
                  <xsl:attribute name="xpath">
                    <xsl:text>@</xsl:text>
                    <xsl:value-of select="function:instance-case(xds:foreignKey/@column)"/>
                  </xsl:attribute>
                </xs:field>
              </xs:keyref>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </xs:element>
    </xs:schema>
  </xsl:template>
  
  <xsl:template name="column" match="xds:table/xds:column">
    <xs:attribute>
      <xsl:attribute name="name">
        <xsl:value-of select="function:instance-case(@name)"/>
      </xsl:attribute>
      <xsl:attribute name="use">
        <xsl:choose>
          <xsl:when test="@null='false' and not(@default)">required</xsl:when>
          <xsl:otherwise>optional</xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
      <xsl:if test="@default">
        <xsl:attribute name="default">
          <xsl:value-of select="@default"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@xsi:type='char' or @xsi:type='clob'">
        <xs:simpleType>
          <xs:restriction base="xs:token">
            <xs:maxLength>
              <xsl:attribute name="value">
                <xsl:value-of select="@length"/>
              </xsl:attribute>
            </xs:maxLength>
            <xsl:if test="not(@varying='true') and not(@xsi:type='clob')">
              <xs:minLength>
                <xsl:attribute name="value">
                  <xsl:value-of select="@length"/>
                </xsl:attribute>
              </xs:minLength>
            </xsl:if>
          </xs:restriction>
        </xs:simpleType>
      </xsl:if>
      <xsl:if test="@xsi:type='enum'">
        <xs:simpleType>
          <xs:restriction base="xs:token">
            <xsl:for-each select="tokenize(@values, ' ')">
              <xs:enumeration>
                <xsl:attribute name="value">
                  <xsl:value-of select="."/>
                </xsl:attribute>
              </xs:enumeration>
            </xsl:for-each>
          </xs:restriction>
        </xs:simpleType>
      </xsl:if>
      <xsl:if test="@xsi:type='integer'">
        <xs:simpleType>
          <xs:restriction base="xs:long">
            <xs:maxInclusive>
              <xsl:attribute name="value">
                <xsl:value-of select="function:precision-scale(@precision)"/>
              </xsl:attribute>
            </xs:maxInclusive>
            <xs:minInclusive>
              <xsl:attribute name="value">
                <xsl:choose>
                  <xsl:when test="not(@unsigned='true')">
                    <xsl:text>-</xsl:text>
                    <xsl:value-of select="function:precision-scale(@precision)"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="0"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:attribute>
            </xs:minInclusive>
          </xs:restriction>
        </xs:simpleType>
      </xsl:if>
      <xsl:if test="@xsi:type='decimal'">
        <xs:simpleType>
          <xs:restriction base="xs:decimal">
            <xs:fractionDigits>
              <xsl:attribute name="value">
                <xsl:value-of select="@decimal"/>
              </xsl:attribute>
            </xs:fractionDigits>
            <xs:maxInclusive>
              <xsl:attribute name="value">
                <xsl:value-of select="function:precision-scale(@precision)"/>
              </xsl:attribute>
            </xs:maxInclusive>
            <xs:minInclusive>
              <xsl:attribute name="value">
                <xsl:choose>
                  <xsl:when test="not(@unsigned='true')">
                    <xsl:text>-</xsl:text>
                    <xsl:value-of select="function:precision-scale(@precision)"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="0"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:attribute>
            </xs:minInclusive>
          </xs:restriction>
        </xs:simpleType>
      </xsl:if>
    </xs:attribute>
  </xsl:template>
  
</xsl:stylesheet>