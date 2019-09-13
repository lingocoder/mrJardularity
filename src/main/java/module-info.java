module java9.modularity {

    requires gradle.api;
    requires com.github.javaparser.symbolsolver.core;
    requires com.github.javaparser.core;
    exports org.javamodularity.moduleplugin;
    exports org.javamodularity.moduleplugin.extensions;
    exports org.javamodularity.moduleplugin.internal;
    exports org.javamodularity.moduleplugin.tasks;
    
}