package org.dbdoclet.option;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.dbdoclet.service.FileServices;

public abstract class Option<T> implements Comparable<Option<?>> {

    private static ResourceBundle res = ResourceBundle.getBundle("org.dbdoclet.option.Resources");
    
    private String longName;
    private String shortName;
    private String mediumName;
    private String propertyName;

    private ArrayList<T> values = new ArrayList<T>();
    private boolean isPresent = false;
    private boolean isRequired = false;
    private boolean isUnique = true;
    private boolean hasArgument = false;
    private boolean isLowerCase = false;
    private boolean isUnset = true;

    public Option() {
        
        this.longName = "";
        this.shortName = "";
        this.mediumName = "";
        this.propertyName = "";

        this.values = new ArrayList<T>();
    }

    public Option(String longName) {

        if (longName == null) {
            throw new IllegalArgumentException("Variable longName is null!");
        }
        
        this.longName = longName;
        this.shortName = "";
        this.mediumName = "";
        this.propertyName = "";
        this.values = new ArrayList<T>();
    }

    public Option(String longName, 
                  String shortName) {

        if (longName == null) {
            throw new IllegalArgumentException("Variable longName is null!");
        }
        
        if (shortName == null) {
            throw new IllegalArgumentException("Variable shortName is null!");
        }
        
        this.longName = longName;
        this.shortName = shortName;
        this.mediumName = "";
        this.propertyName = "";
        this.values = new ArrayList<T>();
    }

    public static ResourceBundle getResourceBundle() {
        return res;
    }

    public abstract void addValueFromString(String value);
    
    public void addValue(T value) {

        if (value == null) {
            throw new IllegalArgumentException("Variable value is null!");
        }
        
        values.add(value);
        isUnset = false;

    }

    public int compareTo(Option<?> other) {
        
        if (other == null) {
            return 1;
        }
        
        String name = getFQName();
        return name.compareTo(other.getFQName());

    }

    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        
        if (obj instanceof Option) {
            
            Option<?> option = (Option<?>) obj;

            if (isValid() == false) {
                return false;
            }
        
            if (option.isValid() == false) {
                return false;
            }
        
            if (longName.equals(option.getLongName()) 
                && mediumName.equals(option.getMediumName())
                && shortName.equals(option.getShortName())
                && propertyName.equals(option.getPropertyName())) {

                return true;
            }
        }

        return false;
    }

    public String getFQName() {

        String buffer = "(";

        if (propertyName != null && propertyName.length() > 0) {

            if (buffer.length() > 0 ) {
                buffer += ".";
            }
            
            buffer += propertyName;
        }

        if (longName != null && longName.length() > 0) {

            if (buffer.length() > 0 ) {
                buffer += "/";
            }
            
            buffer += longName;
        }
        
        if (mediumName != null && mediumName.length() > 0) {

            if (buffer.length() > 0 ) {
                buffer += "/";
            }
            
            buffer += mediumName;
        }
        
        if (shortName != null && shortName.length() > 0) {

            if (buffer.length() > 0 ) {
                buffer += "/";
            }
            
            buffer += shortName;
        }

        buffer += ")";
        
        return buffer;
    }

    public String getLongName() {
        return longName;
    }

    public String getMediumName() {
        return mediumName;
    }

    public String[] getNames() {
        
        ArrayList<String> list = new ArrayList<String>();
        
        if (longName != null 
            && longName.length() > 0
            && list.contains(longName) == false) {
            list.add(longName);
        }
        
        if (mediumName != null 
            && mediumName.length() > 0
            && list.contains(mediumName) == false) {
            list.add(mediumName);
        }
        
        if (shortName != null 
            && shortName.length() > 0
            && list.contains(shortName) == false) {
            list.add(shortName);
        }
        
        if (propertyName != null 
            && propertyName.length() > 0
            && list.contains(propertyName) == false) {
            list.add(propertyName);
        }

        String[] names = new String[list.size()]; 
        for (int i = 0; i<list.size(); i++) {
            names[i] = list.get(i);
        }        

        return names;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getUniqueName() {

        if (longName != null && longName.length() > 0) {
            return "--" + longName;
        }
        
        if (mediumName != null && mediumName.length() > 0) {
            return "-" + mediumName;
        }
        
        if (shortName != null && shortName.length() > 0) {
            return "-" + shortName;
        }
        
        if (propertyName != null && propertyName.length() > 0) {
            return propertyName;
        }
        
        return "???";
    }

    public T getValue() {

        T value = null;
        
        if (values.size() > 0) {
             value = values.get(0);
        }
        
        return value;
    }
 
    public ArrayList<T> getValues() {
        return values;
    }

    public int hashCode() {

        int result = 17;

        result = 37 * result + longName.hashCode();
        result = 37 * result + mediumName.hashCode();
        result = 37 * result + shortName.hashCode();
        result = 37 * result + propertyName.hashCode();

        return result;
    }

    public boolean isLowerCase() {
        return isLowerCase;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public Option<T> isRequired(boolean value) {
        isRequired = value;
        return this;
    }
    
    public boolean isUnique() {
        return isUnique;
    }

    public Option<T> isUnique(boolean value) {
        isUnique = value;
        return this;
    }

    public boolean isUnset() {
        return isUnset;
    }
    
    public void isUnset(boolean isUnset) {
        this.isUnset = isUnset;
    }

    public boolean isValid() {

        if (longName == null) {
            return false;
        }
        
        if (mediumName == null) {
            return false;
        }
        
        if (shortName == null) {
            return false;
        }
        
        if (propertyName == null) {
            return false;
        }

        if (longName.length() == 0 
            && mediumName.length() == 0
            && shortName.length() == 0
            && propertyName.length() == 0) {
            return false;
        }
        
        return true;
    }

    public Option<T> setDefault(T value) {

        if (value == null) {
            throw new IllegalArgumentException(" The argument value may not be null!");
        }
 
        values.clear();
        values.add(value);
        return this;
    }

    public Option<T> setLongName(String longName) {
        
        if (longName == null) {
            throw new IllegalArgumentException(" The argument longName may not be null!");
        }
 
        this.longName = longName;
        return this;
    }

    public void setLowerCase(boolean isLowerCase) {
        this.isLowerCase = isLowerCase;
    }

    public Option<T> setMediumName(String mediumName) {

        if (mediumName == null) {
            throw new IllegalArgumentException(" The argument mediumName may not be null!");
        }
 
        this.mediumName = mediumName;
        return this;
    }

    public Option<T> setPropertyName(String propertyName) {

        if (propertyName == null) {
            throw new IllegalArgumentException(" The argument propertyName may not be null!");
        }
 
        this.propertyName = propertyName;
        return this;

    }

    public Option<T> setShortName(String shortName) {
 
        if (shortName == null) {
            throw new IllegalArgumentException(" The argument shortName may not be null!");
        }
 
        this.shortName = shortName;
        return this;
    }

    public abstract void setValueFromString(String value);

    public void setValue(T value) {

        if (value == null) {
            throw new IllegalArgumentException("Variable value is null!");
        }
        
        values.clear();
        values.add(value);

        isUnset = false;
    }

    public String toString() {

        StringBuilder buffer = new StringBuilder();

        if (propertyName != null && propertyName.length() > 0) {
            buffer.append(propertyName + " ");
        }
        
        if (longName != null && longName.length() > 0) {
            buffer.append(longName + " ");
        }

        if (mediumName != null && mediumName.length() > 0) {
            buffer.append(mediumName + " ");
        }

        if (shortName != null && shortName.length() > 0) {
            buffer.append(shortName + " ");
        }

        buffer.append("[");
             
        if (hasArgument) {
            buffer.append('A');
        } else {
            buffer.append('a');
        } 
        
        if (isPresent) {
            buffer.append('P');
        } else {
            buffer.append('p');
        } 
        
        if (isRequired) {
            buffer.append('R');
        } else {
            buffer.append('r');
        } 
        
        buffer.append(']');
        for (int j = buffer.length(); j < 32; j++) {
            buffer.append('.');
        }
        
        buffer.append(": ");

        List<String> csList = values.stream().map(T::toString).collect(Collectors.toList());
        buffer.append(String.join(",  ", csList));
        
        return buffer.toString();
    }

    protected boolean checkArgument(String arg) {
        return true;
    }

    protected String expandPath(String arg) {
		if (arg.startsWith("~") == true) {
			arg = FileServices.appendPath(System.getProperty("user.home"), arg
					.substring(1));
		}
		return arg;
	}

    protected boolean hasArgument() {
        return hasArgument;
    }

    
    // Implementation of java.lang.Comparable

    protected void hasArgument(boolean value) {
        hasArgument = value;
    }

	protected void isPresent(boolean value) {
        isPresent = value;
    }

	public abstract OptionType getType();
}

