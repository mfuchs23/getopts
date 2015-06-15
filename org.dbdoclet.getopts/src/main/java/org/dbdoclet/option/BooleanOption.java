package org.dbdoclet.option;

/**
 * <div lang="en">
 * The class <code>BooleanOption</code> implements a flag option with no
 * argument.  
 *
 * If the flag is present on the command line, the value of the option will be
 * true, otherwise it will be false.
 *
 * </div>
 *
 * <div lang="de"> Die Klasse <code>BooleanOption</code> realisiert die
 * Standardoption ohne Argument.
 *
 * Wird die Option auf der Kommandozeile gesetzt, so erhält die Option den Wert
 * "wahr". Andernfalls den Wert "falsch". Der Standardwert kann bei Aufruf der
 * Methode <code>getFlag()</code> überschrieben werden.
 *
 * </div>
 *
 * <div id="example_BooleanOption" 
 *      title="BooleanOption">
 * <pre>
 OptionList options = new OptionList(args);

 Option option = new BooleanOption("help","h");
 options.add(option);

 if ( options.getFlag("help",false) ) {
     printUsage();
     return;
 } 

 * </pre>
 * </div>
 *
 * @author <a href="mailto:mfuchs@unico-consulting.com">Michael Fuchs</a>
 * @version 1.0
 */
public class BooleanOption extends Option<Boolean> {

    /**
     * Creates a new <code>BooleanOption</code> instance.
     */
    public BooleanOption() {
        super();
        setDefault(false);
    }

    public BooleanOption(String longName) {
        super(longName);
        setDefault(false);
    }

    /**
     * <div lang="en">
     * Creates a new <code>BooleanOption</code> instance.
     * </div>
     *
     * <div lang="de">
     * Erzeugt eine neue Instanz der Klasse <code>BooleanOption</code>.
     * </div>
     *
     * @param longName a <code>String</code> value
     * @param shortName a <code>String</code> value
     */
    public BooleanOption(String longName, String shortName) {
        super(longName, shortName);
        setDefault(false);
    }
    
    @Override
	public void addValueFromString(String value) {
		addValue(Boolean.valueOf(value));
	}

    @Override
	public void setValueFromString(String value) {

    	boolean flag = false;
    	
        if (value.equals("1") 
            || value.equalsIgnoreCase("on")
            || value.equalsIgnoreCase("yes")
            || value.equalsIgnoreCase("true")
            || value.equalsIgnoreCase("ja")
            || value.equalsIgnoreCase("wahr")) {

            flag = true;
        }
        
        setValue(flag);
    }

	@Override
	public OptionType getType() {
		return OptionType.BOOLEAN;
	}
}
