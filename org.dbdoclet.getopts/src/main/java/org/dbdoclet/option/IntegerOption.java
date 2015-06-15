/* 
 * $Id$
 *
 * ### Copyright (C) 2005 Michael Fuchs ###
 * ### All Rights Reserved.             ###
 *
 * Author: Michael Fuchs
 * E-Mail: michael.fuchs@unico-group.com
 * URL:    http://www.michael-a-fuchs.de
 *
 * RCS Information
 * Author..........: $Author$
 * Date............: $Date$
 * Revision........: $Revision$
 * State...........: $State$
 */
package org.dbdoclet.option;


public class IntegerOption extends Option<Integer> {

     public IntegerOption() {
        super();
        hasArgument(true);
    }

    public IntegerOption(String longName, String shortName) {
        super(longName, shortName);
        hasArgument(true);
    }

	@Override
     public void setValueFromString(String value) {
        setValue(new Integer(value));
    }

	@Override
	public void addValueFromString(String value) {
        addValue(new Integer(value));
	}

	@Override
	public OptionType getType() {
		return OptionType.INTEGER;
	}

}
