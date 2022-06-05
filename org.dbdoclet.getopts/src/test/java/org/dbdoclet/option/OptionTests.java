package org.dbdoclet.option;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Properties;

import org.junit.jupiter.api.Test;

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
            } 

            options.setProperties(properties);
            
        } catch (Exception oops) {        
            oops.printStackTrace();
        } 
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
            
            assertFalse(options.validate(), "Die Validierung ist nicht fehlgeschlagen!");

        } catch (Exception oops) {        
            fail(oops.getMessage());
        }
        
    }

	@Test
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
         
            assertTrue(options.validate(), options.getError());
            
        } catch (Exception oops) {
        
            fail(oops.getMessage());

        }
        
    }
    
    @Test
    public void twoDimensionalArgs() {

        String[][] args = {{"-a", "a" },  
        		{"-b", "b" }, 
        		{"-c", "c" }};

        new OptionList(args);
    }

}
