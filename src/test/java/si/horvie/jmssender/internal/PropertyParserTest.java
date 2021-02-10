package si.horvie.jmssender.internal;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PropertyParserTest {

	@Test
	void testParseEmpty() {
		String props = "";
		List<Property> properties = new PropertyParser().parse(props);
		Assertions.assertTrue(properties.isEmpty());
	}

	@Test
	void testParseSeparator() {
		String props = "";
		List<Property> properties = new PropertyParser().parse(props);
		Assertions.assertTrue(properties.isEmpty());
	}

	@Test
	void testParseOneSimple() {
		String props = "p1:v1";
		List<Property> properties = new PropertyParser().parse(props);
		Assertions.assertEquals(1, properties.size());
		Assertions.assertEquals("p1", properties.get(0).name);
		Assertions.assertEquals("v1", properties.get(0).value);
	}

	@Test
	void testParseOneMultiSeparator() {
		String props = "p1:v1::1";
		List<Property> properties = new PropertyParser().parse(props);
		Assertions.assertEquals(1, properties.size());
		Assertions.assertEquals("p1", properties.get(0).name);
		Assertions.assertEquals("v1::1", properties.get(0).value);
	}

	@Test
	void testParseMultiline() {
		String props = "p1:v1" + System.lineSeparator()
				+ "p2:v2" + System.lineSeparator()
				+ "p3:v3" + System.lineSeparator();

		List<Property> properties = new PropertyParser().parse(props);
		Assertions.assertEquals(3, properties.size());
		Assertions.assertEquals("p1", properties.get(0).name);
		Assertions.assertEquals("v1", properties.get(0).value);
		Assertions.assertEquals("p2", properties.get(1).name);
		Assertions.assertEquals("v2", properties.get(1).value);
		Assertions.assertEquals("p3", properties.get(2).name);
		Assertions.assertEquals("v3", properties.get(2).value);
	}
}
