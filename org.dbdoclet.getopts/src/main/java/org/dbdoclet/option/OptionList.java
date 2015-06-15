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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;

import org.dbdoclet.service.ResourceServices;

/**
 * Die Klasse <code>OptionList</code> verwaltet die bekannten Optionen und
 * wertet die Kommandozeile aus.
 * 
 * <div id="example_optionlist" title="Beispiel für OptionList">
 * 
 * <pre>
 * 
 * OptionList options = new OptionList(args);
 * StringOption opt = new StringOption(&quot;program-name&quot;, &quot;n&quot;);
 * opt.setMediumName(&quot;name&quot;);
 * opt.setPropertyName(&quot;program.name&quot;);
 * opt.setDefault(&quot;PROGRAM&quot;);
 * options.add(opt);
 * 
 * if (options.validate() == false) {
 * 	fail(options.getError());
 * }
 * 
 * options.setProperties(properties);
 * println(&quot;Optionen:&quot;);
 * println(options.list());
 * 
 * </pre>
 * 
 * </div>
 * 
 * @author <a href="mailto:mfuchs@unico-consulting.com">Michael Fuchs</a>
 * @version 1.0
 */
public class OptionList implements Iterable<Option<?>> {

	private static ResourceBundle res = Option.getResourceBundle();

	private ArrayList<String> remainingArgs;
	private ArrayList<String> args;
	private ArrayList<Option<?>> optList;
	private HashMap<String, Option<?>> optMap;
	private HashMap<String, Option<?>> deprecatedPropertiesMap;
	private String error = "";

	/**
	 * Erzeugt eine neue Instanz der Klasse <code>OptionList</code> ohne
	 * Argumente.
	 */
	public OptionList() {

		String[] args = new String[0];
		init(args, 0);
	}

	/**
	 * Erzeugt eine neue Instanz der Klasse <code>OptionList</code>.
	 * 
	 * @param cmdline
	 *            <code>String[][]</code>
	 */
	public OptionList(String[][] cmdline) {

		ArrayList<String> list = new ArrayList<>();

		for (int i = 0; i < cmdline.length; i++) {
			for (int j = 0; j < cmdline[i].length; j++) {
				list.add(cmdline[i][j]);
			}
		}

		String[] args = new String[list.size()];

		int index = 0;
		for (Iterator<String> i = list.iterator(); i.hasNext();) {
			args[index++] = i.next();
		}

		init(args, 0);
	}

	/**
	 * Erzeugt eine neue Instanz der Klasse <code>OptionList</code>.
	 * 
	 * @param cmdline
	 *            a <code>String[]</code> value
	 */
	public OptionList(String[] cmdline) {

		if (cmdline == null)
			throw new IllegalArgumentException("Variable args is null!");

		init(cmdline, 0);
	}

	/**
	 * Erzeugt eine neue Instanz der Klasse <code>OptionList</code>.
	 * 
	 * Der Parameter <code>offset</code> gibt an ab welchem Element der
	 * Kommandozeile die Argumente ausgewertet werden sollen. Damit ist möglich
	 * den Parametern Unterbefehle voranzustellen, z.B. wie in <code>cocs update
	 * --force</code>.
	 * 
	 * @param cmdline
	 *            a <code>String[]</code> value
	 */
	public OptionList(String[] cmdline, int offset) {

		if (cmdline == null) {
			throw new IllegalArgumentException(
					"The argument cmdline may not be null!");
		}

		if (offset < 0) {
			throw new IllegalArgumentException(
					"The argument offset must be greater than 0!");
		}

		if (offset > cmdline.length) {
			throw new IllegalArgumentException(
					"The argument offset is greater than the lehgth of the command line!");
		}

		init(cmdline, offset);
	}

	/**
	 * Die Methode <code>getError</code> liefert die letzte Fehlermeldung an den
	 * Aufrufer zurück.
	 * 
	 * @return <code>String</code>
	 */
	public String getError() {

		if (error == null) {
			error = "";
		}

		return String.join(" ",  args) + "\n\n" + error;
	}

	/**
	 * Die Methode <code>findOption</code> sucht nach der angegebenen Option in
	 * der aktuellen Liste von Optionen.
	 * 
	 * Falls die Option nicht gefunden werden kann, wird <code>null</code>
	 * zurückgegeben.
	 * 
	 * @param option
	 *            Die <code>Option</code> nach der gesucht wird.
	 * @return Die gefundene <code>Option</code> oder <code>null</code>.
	 */
	public Option<?> findOption(Option<?> option) {

		Object obj;

		int index = optList.indexOf(option);

		if (index != -1) {

			obj = optList.get(index);
			if (obj != null && obj instanceof Option) {
				return (Option<?>) obj;
			}
		}

		return null;
	}

	/**
	 * The method <code>findOption</code> tries to find the given option by
	 * name. If the option can not be found, null is returned.
	 * 
	 * @param name
	 *            a <code>String</code> value
	 * @return The option or null if no option can be found.
	 */
	public Option<?> findOption(String name) {

		if (name == null) {
			throw new IllegalArgumentException(
					" The argument name may not be null!");
		}

		return optMap.get(name);
	}

	public Option<?> findExistingOption(Option<?> option) {

		if (option == null) {
			throw new IllegalArgumentException(
					" The argument option may not be null!");
		}

		Option<?> existing;

		existing = optMap.get(option.getLongName());
		if (existing != null) {
			return existing;
		}

		existing = optMap.get(option.getMediumName());
		if (existing != null) {
			return existing;
		}

		existing = optMap.get(option.getShortName());
		if (existing != null) {
			return existing;
		}

		existing = optMap.get(option.getPropertyName());
		if (existing != null) {
			return existing;
		}

		throw new OptionException("Can't find existing option!");
	}

	/**
	 * The method <code>getOption</code> tries to find the given option by name.
	 * If the option can not be found, an OptionException is thrown.
	 * 
	 * @param name
	 *            a <code>String</code> value
	 * @return The option.
	 */
	public Option<?> getOption(String name) throws OptionException {

		if (name == null) {
			throw new IllegalArgumentException(
					"The argument name may not be null!");
		}

		Option<?> option = optMap.get(name);

		if (option == null) {

			String msg = MessageFormat.format(
					ResourceServices.getString(res, "C_ERROR_OPTION_UNKNOWN"),
					name);
			throw new OptionException(msg);
		}

		return option;
	}

	public BooleanOption getBooleanOption(String name) {

		if (name == null) {
			throw new IllegalArgumentException(
					"The argument name may not be null!");
		}

		Option<?> option = getOption(name);

		if (option instanceof BooleanOption) {
			return (BooleanOption) option;
		} else {

			String msg = MessageFormat.format(ResourceServices.getString(res,
					"C_ERROR_OPTION_INVALID_TYPE"), name, "boolean", option
					.getClass().getName());
			throw new OptionException(msg);
		} // end of else
	}

	public void add(Option<?> option) throws OptionException {

		if (option == null) {
			throw new IllegalArgumentException(
					" The argument option may not be null!");
		}

		if (option.isValid() == false) {
			throw new OptionException(ResourceServices.getString(res,
					"C_ERROR_INVALID_OPTION") + option.getUniqueName());
		}

		if (exists(option)) {

			Option<?> existing = findExistingOption(option);

			throw new OptionException(MessageFormat.format(ResourceServices
					.getString(res, "C_ERROR_OPTION_ALREADY_IN_USE"), option
					.getFQName(), existing.getFQName()));

		}

		optList.add(option);

		String[] names = option.getNames();

		for (int i = 0; i < names.length; i++) {
			optMap.put(names[i], option);
		}

	}

	/**
	 * Die Methode <code>getRemainingArgs</code> liefert die nach der
	 * Validierung überzähligen Argumente. Also diejenigen Argumente, die nicht
	 * definiert waren.
	 * 
	 * @return <code>String[]</code>
	 */
	public String[] getRemainingArgs() {

		if (remainingArgs == null) {
			return new String[0];
		}

		String[] array = new String[remainingArgs.size()];

		for (int i = 0; i < remainingArgs.size(); i++) {
			array[i] = remainingArgs.get(i);
		}

		return array;
	}

	/**
	 * Describe <code>getString</code> method here.
	 * 
	 * @param name
	 *            a <code>String</code> value
	 * @return a <code>String</code> value
	 */
	public String getString(String name) {

		if (name == null)
			throw new IllegalArgumentException("Variable name is null!");

		Option<?> option = optMap.get(name);
		if (option == null) {
			return "";
		}

		if (option.getValue() == null) {
			return null;
		}

		return option.getValue().toString();
	}

	/**
	 * Describe <code>getString</code> method here.
	 * 
	 * @param name
	 *            a <code>String</code> value
	 * @param deflt
	 *            a <code>String</code> value
	 * @return a <code>String</code> value
	 */
	public String getString(String name, String deflt) {

		if (name == null)
			throw new IllegalArgumentException("Variable name is null!");

		Option<?> option = optMap.get(name);

		if (option == null || option.isUnset())
			return deflt;

		if (option.getValue() == null) {
			return deflt;
		} else {
			return option.getValue().toString();
		}

	}

	public void addDeprecatedPropertyAlias(Option<?> option, String alias)
			throws OptionException {

		if (option == null) {
			throw new IllegalArgumentException(
					"The argument option may not be null!");
		}

		if (alias == null || alias.length() == 0 || alias.equals("!")) {
			throw new IllegalArgumentException(
					"The argument alias is invalid: " + alias);
		}

		if (deprecatedPropertiesMap == null) {
			throw new IllegalStateException(
					" The field deprecatedPropertiesMap may not be null!");
		}

		String name = alias;
		if (name.startsWith("!")) {
			name = name.substring(1);
		} // end of if (name.startsWith("!"))

		Option<?> opt = optMap.get(name);

		if (opt != null && opt.equals(option) == false) {
			throw new OptionException(MessageFormat.format(ResourceServices
					.getString(res, "C_ERROR_OPTION_ALREADY_IN_USE"), name));

		}

		deprecatedPropertiesMap.put(alias, option);
	}

	public boolean setProperties(String fname) throws FileNotFoundException,
			IOException {

		if (fname == null) {
			throw new IllegalArgumentException(
					" The argument fname may not be null!");
		}

		Properties props = new Properties();
		props.load(new FileInputStream(fname));

		return setProperties(props);
	}

	public boolean setProperties(Properties props) throws OptionException {

		if (props == null) {
			throw new IllegalArgumentException(
					" The argument props may not be null!");
		}

		Object obj;
		Option<?> option;
		String name;
		String value;

		String buffer = "";

		for (Iterator<Object> i = props.keySet().iterator(); i.hasNext();) {

			obj = i.next();

			if (obj instanceof String) {

				name = (String) obj;
				value = props.getProperty(name);

				if (value == null) {
					continue;
				}

				option = optMap.get(name);

				if (option == null) {

					option = deprecatedPropertiesMap.get(name);

					if (option != null) {
						buffer += MessageFormat.format(ResourceServices
								.getString(res, "C_WARN_PROPERTY_DEPRECATED"),
								name, option.getPropertyName())
								+ "\n";
					} else {

						option = deprecatedPropertiesMap.get("!" + name);

						if (option != null && (option instanceof BooleanOption)) {

							value = value.trim();

							boolean flag = false;

							if (value.equals("1")
									|| value.equalsIgnoreCase("yes")
									|| value.equalsIgnoreCase("true")
									|| value.equalsIgnoreCase("ja")
									|| value.equalsIgnoreCase("wahr")) {

								flag = true;
							}

							((BooleanOption) option).setValue(flag);
							continue;
						}
					}
				}

				if (option == null) {

					buffer += MessageFormat.format(ResourceServices
							.getString(res, "C_ERROR_PROPERTY_UNKNOWN"), name)
							+ "\n";
					continue;
				}

				if (option != null && option.isUnset()) {
					option.setValueFromString(value);
				}
			}
		}

		if (buffer != null && buffer.length() > 0) {
			error = buffer;
			return false;
		} else {
			return true;
		}
	}

	public boolean validate() {
		return validate(false);
	}

	/**
	 * Die Methode <code>validate</code> validiert die Argumente.
	 * 
	 * Das Validieren prüft die Argumente auf ihre syntaktische Korrektheit.
	 * Zusätzlich werden die Optionen selbst bearbeitet. Gefundene Optionen
	 * werden als vorhanden markiert und mit den Werten aus den Argumenten
	 * initialisert. Überzählige Argumente werden in einem Container namens
	 * <code>remainingArgs</code> gespeichert. Je nach Wert des Parameters
	 * <code>ignoreUnknownOptions</code> führen überzählige Argumente zu einer
	 * Fehlermeldung oder sie können mit Hilfe der Methode
	 * <code>getRemainingArgs</code> ermittelt werden. Zusätzliche Argumente
	 * sind sinnvoll, wenn den Optionen z.B. eine URL- oder Pfadangabe folgt,
	 * welche nicht von einer Option eingeleitet wird.
	 * 
	 * <div id="example_optionlist_validate"
	 * title="Kommandozeile mit überzähligen Argumenten">
	 * 
	 * <pre>
	 * cocs checkout --verbose file:///repository
	 * </pre>
	 * <p>
	 * In diesem Beispiel wird die URL nicht von einer Option begleitet. Sie
	 * gilt als "überzähliges Argument".
	 * </p>
	 * </div>
	 * 
	 * @param ignoreUnknownOptions
	 *            <code>boolean</code>
	 * @return <code>boolean</code>
	 * @exception OptionException
	 */
	public boolean validate(boolean ignoreUnknownOptions)
			throws OptionException {

		Iterator<Option<?>> iterator = optList.iterator();
		Option<?> option;
		String arg;
		String str;

		String buffer = "";
		int index = -1;
		int counter = 0;

		ArrayList<String> argList = new ArrayList<String>(args);

		while (iterator.hasNext()) {

			option = iterator.next();
			option.isUnset(true);

			index = searchArgs(argList, option);

			if (index == -1 && option.isRequired()) {
				buffer += MessageFormat
						.format(ResourceServices.getString(res,
								"C_ERROR_OPTION_REQUIRED"), option
								.getUniqueName())
						+ "\n";
				continue;
			}

			counter = 0;

			while (index != -1) {

				if (option.hasArgument()) {

					if (index == argList.size() - 1) {

						buffer += MessageFormat.format(ResourceServices
								.getString(res, "C_ERROR_OPTION_NEEDS_ARG"),
								option.getUniqueName())
								+ "\n";
						argList.remove(index);
						index = searchArgs(argList, option);
						continue;
					}

					arg = argList.get(index + 1);

					if (arg.startsWith("-")) {
						buffer += MessageFormat.format(ResourceServices
								.getString(res, "C_ERROR_OPTION_NEEDS_ARG"),
								option.getUniqueName())
								+ "\n";
						argList.remove(index);

						index = searchArgs(argList, option);
						continue;
					}

					if (option instanceof SelectOption) {

						SelectOption selopt = (SelectOption) option;

						if (selopt.checkArgument(arg) == false) {

							buffer += MessageFormat.format(ResourceServices
									.getString(res,
											"C_ERROR_OPTION_INVALID_VALUE"),
									option.getUniqueName(), arg, selopt
											.getListAsString())
									+ "\n";
							argList.remove(index + 1);
							argList.remove(index);

							index = searchArgs(argList, option);
							continue;
						}

					}

					if (option instanceof PathOption) {

						PathOption pathopt = (PathOption) option;

						if (pathopt.checkArgument(arg) == false) {
							buffer += MessageFormat.format(ResourceServices
									.getString(res,
											"C_ERROR_OPTION_INVALID_PATH"),
									option.getUniqueName(), arg, pathopt
											.getInvalidPathElement())
									+ "\n";

							argList.remove(index + 1);
							argList.remove(index);

							index = searchArgs(argList, option);
							continue;
						}
					}

					if (option instanceof FileOption) {

						FileOption fileopt = (FileOption) option;

						if (fileopt.checkArgument(arg) == false) {
							buffer += MessageFormat.format(ResourceServices
									.getString(res,
											"C_ERROR_OPTION_INVALID_FILE"),
									option.getUniqueName(), arg)
									+ "\n";

							argList.remove(index + 1);
							argList.remove(index);

							index = searchArgs(argList, option);
							continue;
						}

					}

					if (option instanceof DirectoryOption) {

						DirectoryOption diropt = (DirectoryOption) option;

						if (diropt.checkArgument(arg) == false) {
							buffer += MessageFormat
									.format(ResourceServices.getString(res,
											"C_ERROR_OPTION_INVALID_DIRECTORY"),
											option.getUniqueName(), arg)
									+ "\n";

							argList.remove(index + 1);
							argList.remove(index);

							index = searchArgs(argList, option);
							continue;
						}

					}

					if (option.isLowerCase()) {
						arg = arg.toLowerCase();
					}

					if (option.isUnset() || option.isUnique()) {
						option.setValueFromString(arg);
					} else {
						option.addValueFromString(arg);
					}

					option.isPresent(true);

					argList.remove(index + 1);
					argList.remove(index);

					/* This option can have multiple arguments */
					if (option.isUnique() == false) {

						int i;

						for (i = 0; i < argList.size(); i++) {

							arg = argList.get(i);

							if (arg.startsWith("-") == false) {
								option.addValueFromString(arg);
							} else {
								break;
							}

						}

						for (int j = 0; j < i; j++) {
							argList.remove(0);
						}
					}

				} else {

					String value = "true";

					if (index + 1 < argList.size()) {

						String next = argList.get(index + 1);
						if (next.startsWith("-") == false) {
							value = next;
							argList.remove(index + 1);
						}
					}

					option.setValueFromString(value);
					option.isPresent(true);
					argList.remove(index);
				}

				// look for more appearances of this option.
				index = searchArgs(argList, option);

				if (option.isUnique() && index != -1 && counter == 0) {

					buffer += MessageFormat.format(ResourceServices
							.getString(res,
									"C_ERROR_OPTION_WAS_FOUND_MORE_THAN_ONCE"),
							option.getUniqueName())
							+ "\n";
				}

				counter++;

			}
		}

		if (argList.size() > 0 && ignoreUnknownOptions == false) {

			str = "";

			for (int i = 0; i < argList.size(); i++) {
				str += argList.get(i) + " ";
			}

			buffer += MessageFormat.format(
					ResourceServices.getString(res, "C_ERROR_OPTION_UNKNOWN"),
					str) + "\n";
		}

		remainingArgs = new ArrayList<String>(argList);

		if (buffer != null && buffer.length() > 0) {
			error = buffer;
			return false;
		} else {
			return true;
		}
	}

	public void write(PrintWriter writer) {

		Option<?> option;
		String name;

		Object[] options = optList.toArray();
		Arrays.sort(options);

		for (int i = 0; i < options.length; i++) {

			option = (Option<?>) options[i];
			name = option.getPropertyName();

			if (option.getValue() != null) {
				if (name != null && name.length() > 0) {
					writer.println(option.getPropertyName() + "="
							+ escape(option.getValue().toString()));
				}
			}
		}
	}

	public Iterator<Option<?>> iterator() {
		return optList.iterator();
	}

	/**
	 * Describe <code>list</code> method here.
	 * 
	 * @return a <code>String</code> value
	 */
	public String list() {

		StringBuffer buffer = new StringBuffer();

		Object[] options = optList.toArray();
		Arrays.sort(options);

		for (int i = 0; i < options.length; i++) {
			buffer.append(((Option<?>) options[i]).toString() + "\n");
		}

		return buffer.toString();

	}

	/**
	 * Describe <code>init</code> method here.
	 * 
	 * @param cmdline
	 *            a <code>String[]</code> value
	 */
	private void init(String[] cmdline, int offset) {

		if (cmdline == null) {
			throw new IllegalArgumentException(
					"The argument cmdline may not be null!");
		}

		if (offset < 0) {
			throw new IllegalArgumentException(
					"The argument offset must be greater than 0!");
		}

		if (offset > cmdline.length) {
			throw new IllegalArgumentException(
					"The argument offset is greater than the length of the command line!");
		}

		args = new ArrayList<String>();

		String arg;
		int pos;

		for (int i = offset; i < cmdline.length; i++) {

			arg = cmdline[i];

			pos = arg.indexOf('=');

			if (arg.startsWith("--") && pos > 0) {

				args.add(arg.substring(0, pos));

				if (pos + 1 < arg.length())
					args.add(arg.substring(pos + 1));
				else
					args.add("");

			} else {
				args.add(cmdline[i]);
			}
		}

		optList = new ArrayList<Option<?>>();
		optMap = new HashMap<String, Option<?>>();
		deprecatedPropertiesMap = new HashMap<String, Option<?>>();
	}

	/**
	 * Describe <code>searchArgs</code> method here.
	 * 
	 * @param longName
	 *            a <code>String</code> value
	 * @param shortName
	 *            a <code>String</code> value
	 * @return an <code>int</code> value
	 */
	private int searchArgs(ArrayList<String> args, Option<?> option) {

		if (args == null) {
			throw new IllegalArgumentException(
					" The argument args may not be null!");
		}

		if (option == null) {
			throw new IllegalArgumentException(
					" The argument option may not be null!");
		}

		String arg;

		for (int i = 0; i < args.size(); i++) {

			arg = args.get(i);

			if (arg.equals("--" + option.getLongName())
					|| arg.equals("-" + option.getMediumName())
					|| arg.equals("-" + option.getShortName())) {

				return i;
			}
		}

		return -1;
	}

	/**
	 * Describe <code>exists</code> method here.
	 * 
	 * @param longName
	 *            a <code>String</code> value
	 * @param mediumName
	 *            a <code>String</code> value
	 * @param shortName
	 *            a <code>String</code> value
	 * @param propertyName
	 *            a <code>String</code> value
	 * @return a <code>boolean</code> value
	 */
	private boolean exists(Option<?> lookFor) {

		if (optList.contains(lookFor)) {
			return true;
		}

		if (isOptionNameAvailable(lookFor.getLongName()) == false) {
			return true;
		}

		if (isOptionNameAvailable(lookFor.getMediumName()) == false) {
			return true;
		}

		if (isOptionNameAvailable(lookFor.getShortName()) == false) {
			return true;
		}

		if (isOptionNameAvailable(lookFor.getPropertyName()) == false) {
			return true;
		}

		return false;
	}

	private boolean isOptionNameAvailable(String name) {

		if (name != null && name.length() > 0 && optMap.get(name) != null) {
			return false;
		}

		return true;
	}

	private String escape(String str) {

		if (str == null) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < str.length(); i++) {

			char c = str.charAt(i);
			int n = (int) c;

			if (c == '\r') {
				continue;
			}

			if (n >= 0 && n < 128 && c != '\n' && c != '\\') {

				buffer.append(c);

			} else {

				buffer.append("\\u");
				String hex = Integer.toHexString(n);

				for (int j = 0; j < 4 - hex.length(); j++) {
					buffer.append("0");
				}

				buffer.append(hex);
			}
		}

		return buffer.toString();
	}

	public boolean getFlag(String name, boolean def) {

		Option<?> option = optMap.get(name);

		if (option != null && option instanceof BooleanOption) {
			return ((BooleanOption) option).getValue();
		}

		return def;
	}

	public Option<String> getTextOption(String name) {

		Option<?> option = getOption(name);
		
		if (option instanceof SelectOption) {
			return (SelectOption) option;
		}
		
		if (option instanceof StringOption) {
			return (StringOption) option;
		}
		
		return null;
	}

}
/*
 * $Log$
 */
