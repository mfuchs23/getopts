package org.dbdoclet.option;

public class StringOption extends Option<String> {

	public StringOption() {
		super();
		hasArgument(true);
	}

	public StringOption(String longName) {
		super(longName);
		hasArgument(true);
	}
	public StringOption(String longName, String shortName) {
		super(longName, shortName);
		hasArgument(true);
	}

	@Override
	public void addValueFromString(String value) {
		addValue(value);
	}

	@Override
	public void setValueFromString(String value) {
		setValue(value);
	}

	@Override
	public OptionType getType() {
		return OptionType.TEXT;
	}
}
