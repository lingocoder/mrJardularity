package org.javamodularity.moduleplugin.tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static java.lang.System.out;
class PerReleaseTest {

    private PerRelease classUnderTest = new PerRelease( );

    @Test
    void runtime( ) {

        String expected = String.format("In a %s '%d' runtime", "Java", Runtime.version().feature( ) );
        String accepted1 = String.format("In the %s%d Sourceset", "java", Runtime.version().feature( ) );
        String accepted2 = String.format("In the %s %s Sourceset", "java", "main" );
        String actual = classUnderTest.runtime( );
        out.printf("PerReleaseTest has:%n'%s'%n'%s'%n'%s'%n", expected, accepted1, accepted2);
        assertTrue( actual.contains( expected ) );
        assertTrue( actual.contains( accepted1 ) || actual.contains( accepted2 ) );
    }
}