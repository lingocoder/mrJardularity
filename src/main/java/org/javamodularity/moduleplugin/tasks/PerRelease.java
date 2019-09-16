package org.javamodularity.moduleplugin.tasks;

import static java.lang.System.out;
import static java.lang.Runtime.version;

public class PerRelease {

    protected String runtime( ){

        String testing = String.format( "In a Java '%d' runtime. In the java main Sourceset", version().feature() );

        out.println( testing );

        return testing;
    }
}
