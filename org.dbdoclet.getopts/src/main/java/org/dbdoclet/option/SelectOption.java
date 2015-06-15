/* 
 * ### Copyright (C) 2001-2003 Michael Fuchs ###
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, 
 * Boston, MA 02111-1307, USA.
 *      
 * Author: Michael Fuchs
 * E-Mail: mfuchs@unico-consulting.com
 *
 * RCS Information:
 * ---------------
 * Id.........: $Id: SelectOption.java,v 1.1.1.1 2004/12/21 14:06:09 mfuchs Exp $
 * Author.....: $Author: mfuchs $
 * Date.......: $Date: 2004/12/21 14:06:09 $
 * Revision...: $Revision: 1.1.1.1 $
 * State......: $State: Exp $
 */
package org.dbdoclet.option;

/**
 * Die Klasse <code>SelectOption</code> definiert eine Option, die einen Wert
 * aus einer Menge vorbestimmter Werte annehmen kann.
 *
 * Das folgende Beispiel zeigt die Definition der Option "cmd", die die Werte
 * list, extract und create annehmen kann.
 *
 * <div id="example_SelectOption" title="Beispiel für SelectOption"> 
 * <pre>  SelectOption optCmd = new SelectOption("cmd", "c");
 *  optCmd.isRequired(true);
 *  String[] optCmdList = { "list", "extract", "create" };
 *  optCmd.setList(optCmdList);
 *  options.add(optCmd);</pre>
 * </div>
 *
 * @author <a href="mailto:mfuchs@unico-consulting.com">Michael Fuchs</a>
 * @version 1.0
 */
public class SelectOption extends Option<String> {

    private String[] list;
    
    public SelectOption() {
        super();
        hasArgument(true);
    }

    public SelectOption(String longName, String shortName) {
        super(longName, shortName);
        hasArgument(true);
    }

    public void setList(String[] list) {
        this.list = list;
    }

    public String getListAsString() {
        String buffer = "";
        
        for (int i = 0; i<list.length; i++) {

            buffer += list[i];
            
            if (i < list.length-1)
                buffer += ", ";
        }
        
        return buffer;
    }


    protected boolean checkArgument(String arg) {

        for (int i = 0; i<list.length; i++) {

            if (arg.equals(list[i]))
                return true;

        } // end of for ()

        return false;
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
