/* Copyright (c) 2021 JAX-DB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.jaxdb.jsql;

import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

interface exp {
  interface Expression<T extends type.Column<V>,D extends data.Column<V>,V> extends type.Entity {
    D AS(D column);
  }

  interface ApproxNumeric<T extends type.ApproxNumeric<V>,D extends data.ApproxNumeric<V>,V extends Number> extends Numeric<T,D,V>, type.ApproxNumeric<V> {}
  interface ARRAY<V> extends Objective<type.ARRAY<V>,data.ARRAY<V>,V[]>, type.ARRAY<V> {}
  interface BIGINT extends ExactNumeric<type.BIGINT,data.BIGINT,Long>, type.BIGINT {}
  interface BINARY extends Objective<type.BINARY,data.BINARY,byte[]>, type.BINARY {}
  interface BLOB extends LargeObject<type.BLOB,data.BLOB,InputStream>, type.BLOB {}
  interface BOOLEAN extends Primitive<type.BOOLEAN,data.BOOLEAN,Boolean>, type.BOOLEAN {}
  interface CHAR extends Textual<type.CHAR,data.CHAR,String>, type.CHAR {}
  interface CLOB extends LargeObject<type.CLOB,data.CLOB,Reader>, type.CLOB {}
  interface Column<T extends type.Column<V>,D extends data.Column<V>,V> extends Expression<T,D,V>, type.Column<V> {}
//  interface Column<V> extends Entity<col.Column<V>,type.Column<V>,V>, type.Column<V> {}
  interface DATE extends Temporal<type.DATE,data.DATE,LocalDate>, type.DATE {}
  interface DATETIME extends Temporal<type.DATETIME,data.DATETIME,LocalDateTime>, type.DATETIME {}
  interface DECIMAL extends ExactNumeric<type.DECIMAL,data.DECIMAL,BigDecimal>, type.DECIMAL {}
  interface DOUBLE extends ApproxNumeric<type.DOUBLE,data.DOUBLE,Double>, type.DOUBLE {}
//  interface Entity<T extends typ.Entity<V>,D extends data.Entity<V>,V> extends Expression<T,D,V> {}
  interface ENUM<V extends EntityEnum> extends Textual<type.ENUM<V>,data.ENUM<V>,V>, type.ENUM<V> {}
  interface ExactNumeric<T extends type.ExactNumeric<V>,D extends data.ExactNumeric<V>,V extends Number> extends Numeric<T,D,V>, type.ExactNumeric<V> {}
  interface FLOAT extends ApproxNumeric<type.FLOAT,data.FLOAT,Float>, type.FLOAT {}
  interface INT extends ExactNumeric<type.INT,data.INT,Integer>, type.INT {}
  interface LargeObject<T extends type.LargeObject<V>,D extends data.LargeObject<V>,V extends Closeable> extends Objective<T,D,V>, type.LargeObject<V> {}
  interface Numeric<T extends type.Numeric<V>,D extends data.Numeric<V>,V extends Number> extends Primitive<T,D,V>, type.Numeric<V> {}
  interface Objective<T extends type.Objective<V>,D extends data.Objective<V>,V> extends Column<T,D,V>, type.Objective<V> {}
  interface Primitive<T extends type.Primitive<V>,D extends data.Primitive<V>,V> extends Column<T,D,V>, type.Primitive<V> {}
  interface SMALLINT extends ExactNumeric<type.SMALLINT,data.SMALLINT,Short>, type.SMALLINT {}
//  interface Table extends Expression<kind.Table,type.Table,?>, kind.Table {}
  interface Temporal<T extends type.Temporal<V>,D extends data.Temporal<V>,V extends java.time.temporal.Temporal> extends Objective<T,D,V>, type.Temporal<V> {}
  interface Textual<T extends type.Textual<V>,D extends data.Textual<V>,V extends CharSequence & Comparable<?>> extends Objective<T,D,V>, type.Textual<V> {}
  interface TIME extends Temporal<type.TIME,data.TIME,LocalTime>, type.TIME {}
  interface TINYINT extends ExactNumeric<type.TINYINT,data.TINYINT,Byte>, type.TINYINT {}
}