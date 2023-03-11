package test.common;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.configuration.ResourceManager;

public class ResourceManagerTest {
	
	@Test
	public void testGet() {
		ResourceManager manager = ResourceManager.getResourceManager();
		assertNotNull(manager.get("floorSys.hostname"));
		assertNotNull(manager.get("schedSys.hostname"));
	}
	
	
	@Test
	public void testGetInt() {
		ResourceManager manager = ResourceManager.getResourceManager();
		assertNotNull(manager.getInt("floorSys.port"));
		assertNotNull(manager.getInt("schedSys.port"));

	}
	
}
