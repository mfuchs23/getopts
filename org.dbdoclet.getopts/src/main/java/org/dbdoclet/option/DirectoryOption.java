package org.dbdoclet.option;

import java.io.File;


/**
 * Die Klasse <code>DirectoryOption</code> verarbeitet ein Argument auf der
 * Kommandozeile, das ein Verzeichnis angibt. 
 * 
 * @author michael
 * 
 */
public class DirectoryOption extends Option<File> {

	private boolean isExisting = false;
	private boolean createPath = false;

	public DirectoryOption() {
		super();
		hasArgument(true);
	}

	public DirectoryOption(String longName) {
		super(longName);
		hasArgument(true);
	}

	public DirectoryOption(String longName, String shortName) {
		super(longName, shortName);
		hasArgument(true);
	}

	public void isExisting(boolean value) {
		isExisting = value;
	}

	public void setCreatePath(boolean createPath) {
		this.createPath = createPath;
	}

	protected boolean checkArgument(String arg) {

		arg = expandPath(arg);

		File file = new File(arg);

		if (file.exists() == false && createPath == true) {
			file.mkdirs();
		}

		if (isExisting == true && file.exists() == false) {
			return false;
		}

		if (file.exists() == true && file.isDirectory() == false) {
			return false;
		}

		return true;
	}

	@Override
	public void addValueFromString(String value) {
		addValue(new File(expandPath(value)));
	}

	@Override
	public void setValueFromString(String value) {
		setValue(new File(expandPath(value)));
	}

	@Override
	public OptionType getType() {
		return OptionType.FILE;
	}
}
