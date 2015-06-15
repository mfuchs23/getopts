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

import java.io.File;
import java.util.StringTokenizer;

public class PathOption extends Option<String> {

    private String invalidPathElement = "";

    public PathOption() {
        super();
        hasArgument(true);
    }

    public PathOption(String longName) {
        super(longName);
        hasArgument(true);
    }

    public PathOption(String longName, String shortName) {
        super(longName, shortName);
        hasArgument(true);
    }

    @Override
	public void addValueFromString(String value) {
		addValue(value);
	}
    
    public String getInvalidPathElement() {
        return invalidPathElement;
    }

	@Override
	public void setValueFromString(String value) {
		setValue(value);
	}

	protected boolean checkArgument(String arg) {

        if (arg == null) {
            throw new IllegalArgumentException("The argument arg may not be null!");
        }
 
        StringTokenizer stz = new StringTokenizer(arg, System.getProperty("path.separator"));
        String token;

        File path;
        String pathElement;

        while (stz.hasMoreTokens()) {

            token = stz.nextToken();
            
            pathElement = token;

            if (pathElement.startsWith("~")) {

                if (pathElement.equals("~")) {

                    pathElement = System.getProperty("user.home");

                } else {
                    
                    pathElement = System.getProperty("user.home") 
                        + pathElement.substring(1);
                }
            }
            
            path = new File(pathElement);
            
            if (path.exists() == false) {
                invalidPathElement = pathElement;
                return false;
            }
        }
    
        return true;
    }

	@Override
	public OptionType getType() {
		return OptionType.TEXT;
	}
}
/*
 * $Log$
 */
