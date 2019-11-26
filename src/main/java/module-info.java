module java9.modularity {

    requires com.github.javaparser.symbolsolver.core;
    exports org.javamodularity.moduleplugin;
    exports org.javamodularity.moduleplugin.extensions;
    exports org.javamodularity.moduleplugin.internal;
    exports org.javamodularity.moduleplugin.tasks;

}
