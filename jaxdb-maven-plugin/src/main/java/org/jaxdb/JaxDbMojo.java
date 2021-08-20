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
import org.openjax.maven.mojo.FilterParameter;
import org.openjax.maven.mojo.FilterType;
import org.openjax.maven.mojo.GeneratorMojo;

abstract class JaxDbMojo<P extends Produce<?>> extends GeneratorMojo {
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
      return this.schemas;
    }

    P[] getProduce() {
      return this.produce;
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
    final String[] produces = produce.split(",");
    final P[] produce = (P[])Array.newInstance((Class<?>)Classes.getSuperclassGenericTypes(getClass())[0], produces.length);
    out:
    for (int i = 0; i < produces.length; ++i) {
      for (final P value : values()) {
        if (value.name.equalsIgnoreCase(produces[i])) {
          produce[i] = value;
          continue out;
        }
      }

      throw new MojoExecutionException("The value '" + Arrays.toString(produce) + "' in parameter <produce>" + this.produce + "</produce> does not match supported values: " + Arrays.toString(values()));
    }

    try {
      final LinkedHashSet<URL> schemas = new LinkedHashSet<>(this.schemas.size());
      for (final String schema : this.schemas)
        schemas.add(new URL(schema));

      execute(new Configuration(configuration, schemas, produce));
    }
    catch (final Exception e) {
      throw new MojoExecutionException(e.getClass().getSimpleName() + ": " + e.getMessage(), e);
    }
  }

  abstract P[] values();
  abstract void execute(Configuration configuration) throws Exception;
}