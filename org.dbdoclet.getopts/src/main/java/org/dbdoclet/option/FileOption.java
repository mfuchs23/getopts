/* 
 *
 * ### Copyright (C) 2005,2010 Michael Fuchs ###
 * ### All Rights Reserved.             ###
 *
 * Author: Michael Fuchs
 * E-Mail: michael.fuchs@dbdoclet.org
 * URL:    http://www.michael-a-fuchs.de
 */
package org.dbdoclet.option;

import java.io.File;

/**
 * Die Klasse <code>FileOption</code> verarbeitet ein Argument der
 * Kommandozeile, das eine Datei spezifiziert. 
 * 
 * @author michael
 * 
 */
public class FileOption extends Option<File> {

	private boolean isExisting = false;

	public FileOption() {

		super();
		hasArgument(true);
	}

	public FileOption(String longName) {

		super(longName);
		hasArgument(true);
	}

	public FileOption(String longName, String shortName) {

		super(longName, shortName);
		hasArgument(true);
	}

	@Override
	public void addValueFromString(String value) {
		addValue(new File(expandPath(value)));
	}

	public void isExisting(boolean value) {
		isExisting = value;
	}

	@Override
	public void setValueFromString(String value) {
		setValue(new File(expandPath(value)));
	}

	protected boolean checkArgument(String arg) {

		arg = expandPath(arg);
		
		File file = new File(arg);

		if (isExisting == true && file.exists() == false) {
			return false;
		}

		if (file.exists() == true && file.isFile() == false) {
			return false;
		}

		return true;
	}
	
	@Override
	public OptionType getType() {
		return OptionType.FILE;
	}
}
