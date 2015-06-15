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

import java.io.PrintWriter;
import java.io.StringWriter;



/**
 * Describe class <code>OptionException</code> here.
 *
 * @author <a href="mailto:mfuchs@unico-consulting.com">Michael Fuchs</a>
 * @version 1.0
 */
public class OptionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Exception cause;
    private String msg;

    /**
     * Creates a new <code>OptionException</code> instance.
     *
     * @param msg a <code>String</code> value
     */
    public OptionException(String msg) {

        super(msg);

        this.msg = msg;
        this.cause = null;
    }
    
    /**
     * Creates a new <code>OptionException</code> instance.
     *
     * @param oops an <code>Exception</code> value
     */
    public OptionException(Exception oops) {

        super(oops.getMessage());

        this.msg = "";
        this.cause = oops;
    }

    /**
     * Describe <code>getMessage</code> method here.
     *
     * @return an <code>Exception</code> value
     */
    public String getMessage() {

        if (cause == null) {
            return msg;
        } 

        StringWriter buffer = new StringWriter();
        cause.printStackTrace(new PrintWriter(buffer));
        return buffer.toString();
    }
}
/*
 * $Log$
 */
