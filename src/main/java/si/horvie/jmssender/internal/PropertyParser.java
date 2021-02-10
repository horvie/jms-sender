package si.horvie.jmssender.internal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PropertyParser {
	private static final String PROP_SEPARATOR = ":";

	public List<Property> parse(String props) {
		String[] propsArr = props.split(System.lineSeparator());
		return Arrays.stream(propsArr).flatMap(this::prepareProp).collect(Collectors.toList());
	}

	private Stream<Property> prepareProp(String prop) {
		if (!prop.isBlank()) {
			int idx = prop.indexOf(PROP_SEPARATOR);
			if (idx < 0) {
				return Stream.of(new Property(prop, ""));
			} else if (idx == 0) {
				return Stream.empty();
			} else {
				return Stream.of(new Property(prop.substring(0, idx), prop.substring(idx + 1)));
			}
		}

		return Stream.empty();
	}
}
