package test.common;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.configuration.ResourceManager;

public class ResourceManagerTest {
	
	@Test
	public void testGet() {
		ResourceManager manager = ResourceManager.getResourceManager();
		assertNotNull(manager.get("test.value1"));
		assertNotNull(manager.get("test.value2"));
	}
	
	
	@Test
	public void testGetInt() {
		ResourceManager manager = ResourceManager.getResourceManager();
		assertNotNull(manager.getInt("test.int1"));
		assertNotNull(manager.getInt("test.int2"));

	}
	
}
