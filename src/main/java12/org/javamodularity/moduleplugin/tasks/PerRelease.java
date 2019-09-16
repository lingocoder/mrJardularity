package org.javamodularity.moduleplugin.tasks;

import static java.lang.System.out;

public class PerRelease {

    protected String runtime( ){

        String testing = String.format( "In a Java '%d' runtime. In the java%d Sourceset", 12, 12 );

        out.println( testing );
        
        return testing;
    }
}
