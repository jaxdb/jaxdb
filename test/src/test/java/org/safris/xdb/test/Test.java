package org.safris.xdb.test;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.safris.commons.lang.Resources;
import org.safris.xdb.data.Transformer;

public class Test {
  @org.junit.Test
  public void test() throws IOException, TransformerException {
    Transformer.xdsToXsd(Resources.getResource("classicmodels.xds").getURL(), new File("target/generated-test-resources/xdb/classicmodels.xsd"));
  }
}