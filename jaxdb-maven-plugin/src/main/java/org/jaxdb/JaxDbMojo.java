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

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.libj.lang.Classes;
import org.libj.lang.Strings;
import org.openjax.maven.mojo.FilterParameter;
import org.openjax.maven.mojo.FilterType;
import org.openjax.maven.mojo.GeneratorMojo;

abstract class JaxDbMojo<P extends Produce<?,?,?>> extends GeneratorMojo {
  static final Pattern EXTENSION_PATTERN = Pattern.compile("\\.\\S+$");
  class Configuration extends GeneratorMojo.Configuration {
    private final LinkedHashSet<URL> schemas;
    private final P[] produce;

    Configuration(final Configuration configuration) {
      this(configuration, configuration.schemas, configuration.produce);
    }

    Configuration(final GeneratorMojo.Configuration configuration, final LinkedHashSet<URL> schemas, final P[] produce) {
      super(configuration);
      this.schemas = schemas;
      this.produce = produce;
    }

    LinkedHashSet<URL> getSchemas() {
      return schemas;
    }

    P[] getProduce() {
      return produce;
    }
  }

  @Parameter(property="produce", required=true)
  private String produce;

  @FilterParameter(FilterType.URL)
  @Parameter(property="schemas", required=true)
  private List<String> schemas;

  @Override
  @SuppressWarnings("unchecked")
  public final void execute(final GeneratorMojo.Configuration configuration) throws MojoExecutionException, MojoFailureException {
    if (schemas.size() == 0) {
      getLog().info("Nothing to do -- no schemas provided");
      return;
    }

    final String[] produces = Strings.split(produce, ',');
    final P[] produce = (P[])Array.newInstance((Class<?>)Classes.getSuperclassGenericTypes(getClass())[0], produces.length);
    OUT:
    for (int i = 0, i$ = produces.length; i < i$; ++i) { // [A]
      for (final P value : values()) { // [A]
        if (value.name.equalsIgnoreCase(produces[i])) {
          produce[i] = value;
          continue OUT;
        }
      }

      throw new MojoExecutionException("The value '" + Arrays.toString(produce) + "' in parameter <produce>" + this.produce + "</produce> does not match supported values: " + Arrays.toString(values()));
    }

    try {
      final int len = this.schemas.size();
      final LinkedHashSet<URL> schemas = new LinkedHashSet<>(len);
      for (int i = 0; i < len; ++i) // [RA]
        schemas.add(new URL(this.schemas.get(i)));

      execute(new Configuration(configuration, schemas, produce));
    }
    catch (final Exception e) {
      throw new MojoExecutionException(e.getClass().getSimpleName() + ": " + e.getMessage(), e);
    }
  }

  abstract P[] values();
  abstract void execute(Configuration configuration) throws Exception;
}