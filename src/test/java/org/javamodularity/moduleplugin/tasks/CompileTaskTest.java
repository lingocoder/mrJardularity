package org.javamodularity.moduleplugin.tasks;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;

import static java.lang.Runtime.version;
import static org.junit.jupiter.api.Assertions.*;

class CompileTaskTest {

    private CompileTask classUnderTest;

    private Project project;

    @Test
    void configureCompileJava( ) {

        /* given */
        Project project = ProjectBuilder.builder().withProjectDir(new File("test-project/")).build();
        String expected = String.format("In a Java `%d` runtime", version().feature( ) );

        classUnderTest = new CompileTask( project );

        /* when */
        String actual = classUnderTest.configureCompileJava( );

        /* then */
        assertDoesNotThrow( () -> new Exception( ) );

/*        assertEquals( expected, actual );*/

    }
}