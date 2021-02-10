package si.horvie.jmssender.internal;

class Property {
	String name;
	String value;

	public Property(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Property [name=" + name + ", value=" + value + "]";
	}

}