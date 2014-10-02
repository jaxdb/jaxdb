package org.safris.xdb.xde;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.safris.commons.lang.Resources;
import org.safris.xdb.xde.EntityBridgeUtil.Assignment;
import org.safris.xdb.xdl.xdl_database;
import org.safris.xml.generator.compiler.runtime.Bindings;
import org.xml.sax.InputSource;

import com.livecare.entity.livecare_patient;
import com.livecare.entity.livecare_provider;
import com.livecare.entity.livecare_provider_patient;

public class EntitiesTest {
  @Test
  public void testEntities() throws Exception {
    final InputStream in = Resources.getResource("/entity.xdl").getURL().openStream();
    final xdl_database database = (xdl_database)Bindings.parse(new InputSource(in));
    in.close();
    final String sql = "SELECT patient.*, pr.given_name, pr.website, pr.*, pp.*, pp.id, pp.emergency_contact_name FROM provider pr, provider_patient pp, patient WHERE pr.gender = 'M' AND pr.id = pp.provider_id AND patient.id = pp.patient_id";
    final Map<String,Class<?>> aliasToBinding = EntityBridgeUtil.parseBindings(database, sql);
    assertEquals(livecare_provider.class, aliasToBinding.get("pr"));
    assertEquals(livecare_provider_patient.class, aliasToBinding.get("pp"));
    assertEquals(livecare_patient.class, aliasToBinding.get("patient"));
    
    final EntityBridge entities = EntityBridge.getInstance(database);
    final List<Assignment> assignments = entities.parseAssignments(sql);
    assertEquals(5, assignments.size());
    Assignment assignment = assignments.get(0);
    assertEquals(livecare_patient.class, assignment.binding);
    assertEquals(17, assignment.entries.size());
    assignment = assignments.get(1);
    assertEquals(livecare_provider.class, assignment.binding);
    assertEquals(2, assignment.entries.size());
    assignment = assignments.get(2);
    assertEquals(livecare_provider.class, assignment.binding);
    assertEquals(42, assignment.entries.size());
    assignment = assignments.get(3);
    assertEquals(livecare_provider_patient.class, assignment.binding);
    assertEquals(31, assignment.entries.size());
    assignment = assignments.get(4);
    assertEquals(livecare_provider_patient.class, assignment.binding);
    assertEquals(2, assignment.entries.size());
    // FIXME: Weak tests here! :| Should check the order of columns too
  }
}
