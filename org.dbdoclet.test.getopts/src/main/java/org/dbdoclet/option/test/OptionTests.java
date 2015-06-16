package org.dbdoclet.option.test;

import static org.junit.Assert.fail;

import java.util.Properties;

import org.dbdoclet.option.BooleanOption;
import org.dbdoclet.option.FileOption;
import org.dbdoclet.option.OptionList;
import org.dbdoclet.option.StringOption;
import org.junit.Test;

/**
 *  Unit Test for class OptionTests.java
 *
 *
 * Created: Fri Oct 31 18:50:16 2003
 *
 * @author <a href="mailto:mfuchs@unico-consulting.com">Michael Fuchs</a>
 * @version 1.0
 */
public class OptionTests {

	@Test
	public void testOptions1() {

        String[] args = { "-file", "/etc/hosts" };

        Properties properties = new Properties();
        properties.setProperty("input.file","/etc/passwd");
        properties.setProperty("destination.directory","/var/tmp");

        OptionList options = new OptionList(args);

        try {
            
            FileOption fopt = new FileOption();
            fopt.setMediumName("file");
            fopt.setPropertyName("input.file");
            fopt.isExisting(true);
            options.add(fopt);
            
            StringOption sopt = new StringOption("destination-directory","d");
            sopt.setMediumName("destdir");
            sopt.setPropertyName("destination.directory");
            sopt.setDefault("/tmp");
            options.add(sopt);
            
            if (options.validate() == false) {
                fail(options.getError());
            } // end of else

            options.setProperties(properties);

            println("Optionen:");
            println(options.list());
            
        } catch (Exception oops) {
        
            oops.printStackTrace();

        } // end of try-catch
        
    }

	@Test
    public void testOptions2() {

        String[] args = { "--help", "-h" , "-?"};

        OptionList options = new OptionList(args);

        try {
            
            BooleanOption opt = new BooleanOption("help","?");
            opt.setMediumName("h");
            opt.setPropertyName("help");
            options.add(opt);
            
            if (options.validate()) {
                fail("Die Validierung muss fehlschlagen, da die Option --help mehrmals verwendet wurde.");
            } else {
                println("Die Validierung der Optionen war nicht erfolgreich:");
                println(options.getError());
            } // end of else

            println("Optionen:");
            println(options.list());
            
        } catch (Exception oops) {
        
            fail(oops.getMessage());

        } // end of try-catch
        
    }

    public void testOptions3() {

        String[] args = { "-tag", "a", 
                          "-tag", "b", 
                          "-tag", "c"};

        OptionList options = new OptionList(args);

        try {
            
            StringOption opt = new StringOption();
            opt.isUnique(false);
            opt.isRequired(true);
            opt.setMediumName("tag");
            opt.setPropertyName("javadoc.tag");
            options.add(opt);
            
            if (options.validate()) {
                println("Die Validierung der Optionen war erfolgreich.");
            } else {
                println("Die Validierung der Optionen war nicht erfolgreich:");
                println(options.getError());
            } // end of else

            println("Optionen:");
            println(options.list());
            
        } catch (Exception oops) {
        
            fail(oops.getMessage());

        } // end of try-catch
        
    }

    private static void println(String msg) {
        System.out.println(msg);
    }
    
} // OptionTest
