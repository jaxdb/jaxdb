/* Copyright (c) 2018 JAX-DB
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;

import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.libj.net.URLs;

@Mojo(name = "sqlxsd", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.TEST)
@Execute(goal = "sqlxsd")
public class SqlXsdMojo extends JaxDbMojo<SqlXsdProduce> {
  class Configuration extends JaxDbMojo<SqlXsdProduce>.Configuration {
    private final LinkedHashSet<URL> xsds;

    Configuration(final Configuration configuration) {
      this(configuration, configuration.xsds);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    Configuration(final JaxDbMojo.Configuration configuration, final LinkedHashSet<URL> xsds) {
      super(configuration);
      this.xsds = xsds;
    }

    LinkedHashSet<URL> getXsds() {
      return xsds;
    }
  }

  final Configuration configure(final JaxDbMojo<?>.Configuration configuration) throws MalformedURLException {
    final LinkedHashSet<URL> xsds = new LinkedHashSet<>();
    final LinkedHashSet<URL> schemas = configuration.getSchemas();
    if (schemas.size() > 0)
      for (final URL schema : schemas) // [S]
        xsds.add(new File(configuration.getDestDir(), EXTENSION_PATTERN.matcher(URLs.getName(schema)).replaceAll(".xsd")).toURI().toURL());

    return new Configuration(configuration, xsds);
  }

  @Override
  final void execute(final JaxDbMojo<SqlXsdProduce>.Configuration configuration) throws Exception {
    final Configuration config = configure(configuration);
    for (final SqlXsdProduce produce : configuration.getProduce()) // [A]
      produce.execute(config, null);
  }

  @Override
  SqlXsdProduce[] values() {
    return SqlXsdProduce.values;
  }
}