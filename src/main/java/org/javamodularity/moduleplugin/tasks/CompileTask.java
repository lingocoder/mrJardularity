package org.javamodularity.moduleplugin.tasks;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logging;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.compile.AbstractCompile;
import org.gradle.api.tasks.compile.JavaCompile;
import org.javamodularity.moduleplugin.extensions.CompileModuleOptions;
import org.javamodularity.moduleplugin.internal.CompileModuleInfoHelper;
import org.gradle.api.logging.Logger;

import java.util.Optional;

import static java.lang.Runtime.version;
import static java.lang.System.out;

public class CompileTask extends AbstractCompileTask {
    private static final Logger LOG = Logging.getLogger( CompileTask.class );
    
    public CompileTask(Project project) {
        super(project);
    }

    /**
     * @see CompileModuleInfoTask#configureCompileModuleInfoJava()
     */
    public String configureCompileJava() {
        final String[] testing = {"Sorry. Wrong number :("};
        helper().findTask(JavaPlugin.COMPILE_JAVA_TASK_NAME, JavaCompile.class)
                .ifPresent( (var compile ) -> { testing[0] = this.configureCompileJava( compile ); });
        return testing[0];
    }

    private String configureCompileJava(JavaCompile compileJava) {
        String testing = String.format("In a Java `%d` runtime", version().feature( ) );
        debug( testing );
        var moduleOptions = compileJava.getExtensions().create("moduleOptions", CompileModuleOptions.class, project);
        project.afterEvaluate(p -> {
            MergeClassesHelper.POST_JAVA_COMPILE_TASK_NAMES.stream()
                    .map( ( var name ) -> helper().findTask(name, AbstractCompile.class))
                    .flatMap(Optional::stream)
                    .filter( ( var task ) -> !task.getSource().isEmpty())
                    .findAny()
                    .ifPresent( ( var task ) -> moduleOptions.setCompileModuleInfoSeparately(true));

            if (moduleOptions.getCompileModuleInfoSeparately()) {
                compileJava.exclude("module-info.java");
            } else {
                configureModularityForCompileJava(compileJava, moduleOptions);
            }
        });
        return testing;
    }

    /**
     * @see CompileModuleInfoTask#configureModularityForCompileModuleInfoJava
     */
    void configureModularityForCompileJava(JavaCompile compileJava, CompileModuleOptions moduleOptions) {
        CompileModuleInfoHelper.dependOnOtherCompileModuleInfoJavaTasks(compileJava);

        CompileJavaTaskMutator mutator = createCompileJavaTaskMutator(compileJava, moduleOptions);
        // don't convert to lambda: https://github.com/java9-modularity/gradle-modules-plugin/issues/54
        compileJava.doFirst(new Action<Task>() {
            @Override
            public void execute(Task task) {
                mutator.modularizeJavaCompileTask(compileJava);
            }
        });
    }

    private static void debug( String msg ){
        LOG.quiet( msg );
    }

}
