/* Copyright (c) 2019 JAX-DB
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

package org.jaxdb;

import java.net.URL;
import java.util.HashMap;

import org.libj.lang.Strings;
import org.libj.net.URLs;
import org.libj.util.StringPaths;

class Reserve<T> {
  final T obj;
  private final HashMap<String,String> nameToName = new HashMap<>();

  Reserve(final T obj) {
    this.obj = obj;
  }

  String get(final URL schema, final String name) {
    String value = nameToName.get(name);
    if (value == null)
      nameToName.put(name, value = name == null ? URLs.getSimpleName(schema) + ".sql" : Strings.searchReplace(StringPaths.getName(schema.toString()), name));

    return value;
  }
}