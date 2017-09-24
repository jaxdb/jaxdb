package org.libx4j.rdb.dmlx;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.junit.Test;

public class ClassicModelsCreateTest extends DMLxTest {
  @Test
  public void testCreateSchema() throws IOException, TransformerException {
    createSchemas("classicmodels");
  }
}