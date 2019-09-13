## mrJardularity — MRJARS meet Modularity

#### How to turn a plugin that is not a module into one that is both a module and a Multi-Release JAR File

I have authored two plugins so far. The [*JarExec plugin*](http://bit.ly/JarExecPi) and ***mrJar***. They've both already had the ***mrJar*** treatment. The *gradle-modules-plugin* is precisely [*the type of project for which **mrJar** was created*](http://bit.ly/mrJrPlug). That is because: (*A*) It is a plugin. (*B*) It is not a module.

#### Context

The *gradle-modules-plugin* is a popular tool that build authors use to create modules. Interestingly, however, ***the *gradle-modules-plugin* itself is not a module***. As ironic as that might be, the discussion of whether or not a Gradle plugin *should* be a module is not the focus of this demonstration.

So, I want to be clear that the context of this demonstration is specifically this: *With the help of* ***mrJar***, *it is **technically possible for any plugin** to be a module*.

#### What does *mrJardularity* even mean?

This project uses the code in @paulbakker's and @sandermak's [*gradle-modules-plugin*](http://bit.ly/GrdlModPi) repository to demonstrate the usage and functionality of the ***mrJar*** plugin. It demonstrates the following:

1. ***mrJar's*** support for transforming a project that is *not* a module, into a project that *is* a module
2. ***mrJar's*** support for creating Modular Multi-Release JAR Files
3. ***mrJar's*** ability to augment other plugins with functionality they don't themselves implement; and not conflict with those plugins even though they share similar functionality that is also implemented in ***mrJar***

So *mrJardularity* is a melding of ***mrJar*** with those guys' *Java 9 Modularity*.

### What's in this repo?

This repository contains a copy of code that implements a Gradle plugin. The original code was originally authored by @paulbakker and @sandermak (*links above*). The code in this repo has been refactored, relatively minimally, to demonstrate functionality of a different plugin authored by me, lingocoder.

These are the steps I took to turn the *gradle-modules-plugin* into a plugin that is a module: 

1. Clone the original *gradle-modules-plugin* repo (*link above*) or download it as a zip <br />

2. Run *`gradle check`* to run the tests and verify the original project builds successfully

   a. Observe that, before being given the ***mrJar*** treatment, the original *gradle-modules-plugin* that was assembled by Gradle's built-in archiver, is only an automatic module:
   
       jar -f build/libs/moduleplugin-1.5.1-SNAPSHOT.jar
       No module descriptor found. Derived automatic module.
       
       moduleplugin@1.5.1-SNAPSHOT automatic
       requires java.base mandated
       contains org.javamodularity.moduleplugin
       contains org.javamodularity.moduleplugin.extensions
       contains org.javamodularity.moduleplugin.internal
       contains org.javamodularity.moduleplugin.shadow.javaparser
       contains org.javamodularity.moduleplugin.shadow.javaparser.ast
       ...
  
3. Apply the ***mrJar*** plugin in the *`plugins{ }`* block of the project's *`build.gradle`*: <br />
   a. *`id 'com.lingocoder.mrjar' version '0.0.10'`* <br />

4. Configure the properties of ***mrJar's*** *`mrjar`* extension:
   ```
   mrjar{
       releases = [JavaVersion.VERSION_1_9,JavaVersion.VERSION_12]
       packages =['org.javamodularity.moduleplugin.extensions', 'org.javamodularity.moduleplugin.internal', 'org.javamodularity.moduleplugin.tasks']
   }
   ```   

5. *`cd`* into the root folder of the project and initialize the project for ***mrJar*** processing, with its *`mrinit`* task:

   a. *`gradlew mrinit`*

6. Copy classes from the main source set to their corresponding release-specific packages you just created in Step 3+4 <br />

   • I, more or less randomly, chose the minimal set of *gradle-modules-plugin* classes sufficient to demonstrate ***mrJar***'s MRJAR File creation functionality. Whether or not @paulbakker and @sandermak would spin off those particular classes to target particular releases is, once again, off-topic for a demo.<br />
   
   • Similarly, the functionality of the particular classes I chose arbitrarily for this demo, is not important in the context of this particular demo. <br />
   
     &nbsp;&nbsp;&nbsp;a) make appropriate release-specific changes to the respective source files <br />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- since this demo is about applying ***mrJar*** — *and not about coding best practices* — I made the simplest release-specific changes to the code that sprang to mind. In a real-world project, you would make real-world changes in this step.

7. Create *`module-info.java`* files in the base directory of the per-release source sets created above

    a. add *`requires`*, *`exports`*, *`uses`*, etc. as appropriate for your module
    
8. Assemble the MRJAR File artifact

    a. For the particular use case of the original *gradle-modules-plugin* project, that project is scripted to publish its artifact to a local Maven repository. So I ran Gradle's *`publish`* task:
    
       gradlew publish

    b. There are bound to be JPMS read access-related errors in this step, which you will need to solve before the modular MRJAR artifact can be built successfully. Solving those kinds of problems is out of scope for this kind of demo. Specific solutions would depend on the specific details of a specific project. The problems I encountered at this step for the original *gradle-modules-plugin* were, luckily, solved by Gradle's dependency management capabilities. See this repo's refactored build script for the details.
    
    c. After each iteration of solving module-graph resolution-related issues, loop back to Step 8a until *`gradle publish`* reports:
    
        BUILD SUCCESSFUL in N seconds
    
9. Confirm that ***mrJar*** did indeed turn what was before an automatic module plugin into a plugin that is now actually a fully-fledged module itself; and one that supports multiple different releases to boot<sup>*1*</sup>:

        jar -f consuming/maven-repo/org/javamodularity/j9dularity/1.5.1-mrjar/j9dularity-1.5.1-mrjar.jar --describe-module
        releases: 9 12
    
        java9.modularity jar:file:///.../consuming/maven-repo/org/javamodularity/j9dularity/1.5.1-mrjar/j9dularity-1.5.1-mrjar.jar/!module-info.class
        exports org.javamodularity.moduleplugin
        exports org.javamodularity.moduleplugin.extensions
        exports org.javamodularity.moduleplugin.internal
        exports org.javamodularity.moduleplugin.tasks
        requires com.github.javaparser.core
        requires com.github.javaparser.symbolsolver.core
        requires gradle.api
        requires java.base mandated        

10. Run the tests again to confirm the plugin is still good to go after some pretty major refactoring:

        gradlew check
        
        ...
        
        BUILD SUCCESSFUL in 32m 53s
        10 actionable tasks: 10 executed
     

Follow those steps and you too can turn ***any*** Java-based library project (*plugin or not*) into an MRJAR File that is also a bona fide JPMS module.
<br />
<br />
<br />


__

<sup><sup>1</sup></sup><sup> — *Notice I changed the name of the plugin in this demo's refactored build script from the original „modulesPlugin“ to „j9dularity“. The intention of that change is to make it super obvious that the code and artifacts produced by the refactored build scripts in this demo repo are definitely not to be confused with those of the original.*</sup>