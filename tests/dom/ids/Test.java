/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xerces" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, International
 * Business Machines, Inc., http://www.apache.org.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package dom.ids;

import java.io.PrintWriter;

import org.w3c.dom.*;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import dom.util.Assertion;
import dom.ParserWrapper;

/**
 * A simple program to test Document.getElementById() and the management
 * of ID attributes. Originally based on dom.Counter.
 * CAVEAT: Although any document can be given in argument, the test assumes
 * it is given personal.xml and relies on that to function properly.
 *
 * @author Andy Clark, IBM
 * @author Arnaud  Le Hors, IBM
 *
 * @version $Id$
 */
public class Test {

    //
    // Constants
    //

    // feature ids

    protected static final String NAMESPACES_FEATURE_ID =
        "http://xml.org/sax/features/namespaces";

    protected static final String VALIDATION_FEATURE_ID =
        "http://xml.org/sax/features/validation";

    protected static final String SCHEMA_VALIDATION_FEATURE_ID =
        "http://apache.org/xml/features/validation/schema";

    protected static final String SCHEMA_FULL_CHECKING_FEATURE_ID =
        "http://apache.org/xml/features/validation/schema-full-checking";

    protected static final String DEFERRED_DOM_FEATURE_ID =
        "http://apache.org/xml/features/dom/defer-node-expansion";

    // default settings

    protected static final String DEFAULT_PARSER_NAME = "dom.wrappers.Xerces";

    protected static final boolean DEFAULT_NAMESPACES = true;

    protected static final boolean DEFAULT_VALIDATION = false;

    protected static final boolean DEFAULT_SCHEMA_VALIDATION = false;

    protected static final boolean DEFAULT_SCHEMA_FULL_CHECKING = false;

    // Xerces specific feature
    protected static final boolean DEFAULT_DEFERRED_DOM = true;

    //
    // Public methods
    //

    /** Performs the actual test. */
    public void test(Document doc) {

        System.out.println("DOM IDs Test...");
        Element el = doc.getElementById("one.worker");
        Assertion.assert(el != null);
        Element el2 = doc.getElementById("one.worker there");
        Assertion.assert(el2 == null);

        if (el != null) {
            Assertion.equals(el.getAttribute("id"), "one.worker");
            el.setAttribute("id", "my.worker");
            el2 = doc.getElementById("my.worker");
            Assertion.assert(el2 == el);
            
            el2 = doc.getElementById("one.worker");
            Assertion.assert(el2 == null);
            el.removeAttribute("id");
            el2 = doc.getElementById("my.worker");
            Assertion.assert(el2 == null);
        }

        // find default id attribute and check its value
        NodeList elementList = doc.getElementsByTagName("person");
        Element testEmployee = (Element)elementList.item(1);
        Attr id = testEmployee.getAttributeNode("id2");
        Assertion.assert(id.getNodeValue().equals("id02"), "value == 'id02'");


        Element elem = doc.getElementById("id02");
        Assertion.assert(elem.getNodeName().equals("person"), "return by id 'id02'");
        
        // 
        // remove default attribute and check on retrieval what its value
        Attr removedAttr = testEmployee.removeAttributeNode(id);
        String value = testEmployee.getAttribute("id2");
        Assertion.assert(value.equals("default.id"), "value='default.id'");


        elem = doc.getElementById("default.id");
        Assertion.assert(elem !=null, "elem by id 'default.id'");


        elem = doc.getElementById("id02");
        Assertion.assert(elem ==null, "elem by id '02'");

        System.out.println("done.");

    } // test(Document)

    //
    // MAIN
    //

    /** Main program entry point. */
    public static void main(String argv[]) {

        // is there anything to do?
        /*if (argv.length == 0) {
            printUsage();
            System.exit(1);
        } */

        
        // variables
        Test test = new Test();
        ParserWrapper parser = null;
        boolean namespaces = DEFAULT_NAMESPACES;
        boolean validation = DEFAULT_VALIDATION;
        boolean schemaValidation = DEFAULT_SCHEMA_VALIDATION;
        boolean schemaFullChecking = DEFAULT_SCHEMA_FULL_CHECKING;
        boolean deferredDom = DEFAULT_DEFERRED_DOM;
        
        String inputfile="tests/dom/ids/input.xml";
        
        // process arguments
        for (int i = 0; i < argv.length; i++) {
            String arg = argv[i];
            if (arg.startsWith("-")) {
                String option = arg.substring(1);
                if (option.equals("p")) {
                    // get parser name
                    if (++i == argv.length) {
                        System.err.println("error: Missing argument to -p"
                                           + " option.");
                    }
                    String parserName = argv[i];

                    // create parser
                    try {
                        parser = (ParserWrapper)
                            Class.forName(parserName).newInstance();
                    }
                    catch (Exception e) {
                        parser = null;
                        System.err.println("error: Unable to instantiate "
                                           + "parser (" + parserName + ")");
                    }
                    continue;
                }
                if (option.equalsIgnoreCase("n")) {
                    namespaces = option.equals("n");
                    continue;
                }
                if (option.equalsIgnoreCase("v")) {
                    validation = option.equals("v");
                    continue;
                }
                if (option.equalsIgnoreCase("s")) {
                    schemaValidation = option.equals("s");
                    continue;
                }
                if (option.equalsIgnoreCase("f")) {
                    schemaFullChecking = option.equals("f");
                    continue;
                }
                if (option.equalsIgnoreCase("d")) {
                    deferredDom = option.equals("d");
                    continue;
                }
                if (option.equals("h")) {
                    printUsage();
                    continue;
                }
            }
        }

            // use default parser?
            if (parser == null) {

                // create parser
                try {
                    parser = (ParserWrapper)
                        Class.forName(DEFAULT_PARSER_NAME).newInstance();
                }
                catch (Exception e) {
                    System.err.println("error: Unable to instantiate parser ("
                                       + DEFAULT_PARSER_NAME + ")");
                    System.exit(1);
                }
            }

            // set parser features
            try {
                parser.setFeature(NAMESPACES_FEATURE_ID, namespaces);
            }
            catch (SAXException e) {
                System.err.println("warning: Parser does not support feature ("
                                   + NAMESPACES_FEATURE_ID + ")");
            }
            try {
                parser.setFeature(VALIDATION_FEATURE_ID, validation);
            }
            catch (SAXException e) {
                System.err.println("warning: Parser does not support feature ("
                                   + VALIDATION_FEATURE_ID + ")");
            }
            try {
                parser.setFeature(SCHEMA_VALIDATION_FEATURE_ID,
                                  schemaValidation);
            }
            catch (SAXException e) {
                System.err.println("warning: Parser does not support feature ("
                                   + SCHEMA_VALIDATION_FEATURE_ID + ")");
            }
            try {
                parser.setFeature(SCHEMA_FULL_CHECKING_FEATURE_ID,
                                  schemaFullChecking);
            }
            catch (SAXException e) {
                System.err.println("warning: Parser does not support feature ("
                                   + SCHEMA_FULL_CHECKING_FEATURE_ID + ")");
            }

            if (parser instanceof dom.wrappers.Xerces) {
                try {
                    parser.setFeature(DEFERRED_DOM_FEATURE_ID,
                                      deferredDom);
                }
                catch (SAXException e) {
                    System.err.println("warning: Parser does not support " +
                                       "feature (" +
                                       DEFERRED_DOM_FEATURE_ID + ")");
                }
            }

            // parse file
            try {
                Document document = null;
                document = parser.parse(inputfile);
                test.test(document);
            }
            catch (SAXParseException e) {
                // ignore
            }
            catch (Exception e) {
                System.err.println("error: Parse error occurred - " +
                                   e.getMessage());
                Exception se = e;
                if (e instanceof SAXException) {
                    se = ((SAXException)e).getException();
                }
                if (se != null)
                  se.printStackTrace(System.err);
                else
                  e.printStackTrace(System.err);
            }
        

    } // main(String[])

    //
    // Private static methods
    //

    /** Prints the usage. */
    private static void printUsage() {

        System.err.println("usage: java dom.ids.Test (options) " +
                           "...data/personal.xml");
        System.err.println();

        System.err.println("options:");
        System.err.println("  -p name    Select parser by name.");
        System.err.println("  -d  | -D   Turn on/off (Xerces) deferred DOM.");
        System.err.println("  -n  | -N   Turn on/off namespace processing.");
        System.err.println("  -v  | -V   Turn on/off validation.");
        System.err.println("  -s  | -S   Turn on/off Schema validation " +
                           "support.");
        System.err.println("             NOTE: Not supported by all parsers.");
        System.err.println("  -f  | -F   Turn on/off Schema full checking.");
        System.err.println("             NOTE: Requires use of -s and not " +
                           "supported by all parsers.");
        System.err.println("  -h         This help screen.");
        System.err.println();

        System.err.println("defaults:");
        System.err.println("  Parser:     " + DEFAULT_PARSER_NAME);
        System.err.println("  Xerces Deferred DOM: " +
                           (DEFAULT_DEFERRED_DOM ? "on" : "off"));
        System.err.println("  Namespaces: " +
                           (DEFAULT_NAMESPACES ? "on" : "off"));
        System.err.println("  Validation: " +
                           (DEFAULT_VALIDATION ? "on" : "off"));
        System.err.println("  Schema:     " +
                           (DEFAULT_SCHEMA_VALIDATION ? "on" : "off"));
        System.err.println("  Schema full checking:     " +
                           (DEFAULT_SCHEMA_FULL_CHECKING ? "on" : "off"));

    } // printUsage()

} // class Test
