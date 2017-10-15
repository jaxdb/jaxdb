package org.libx4j.rdb.dmlx;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.lib4j.jci.CompilationException;

public class ClassicModelsCreateTest extends DMLxTest {
  @Test
  public void testCreateSchema() throws CompilationException, IOException, JAXBException, TransformerException {
    createSchemas("classicmodels");
  }
}