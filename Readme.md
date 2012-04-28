OpenJDK "classpath exception" checker
=====================================

OpenJDK is shipped under the GPL license with "classpath exception". There isn't an official list of classes covered under this provision. The only way to find is to examine each source file header to check if it states: "Oracle designates this particular file as subject to the "Classpath" exception as provided by Oracle in the LICENSE file that accompanied this code.".

This simple utility written in Scala searches a given OpenJDK directory and lists files without the classpath exception. By default, it searches for all `java..*` and `javax..*` classes, but you can customize this by modifying `src/main/resources/application.conf`.

How to run
----------
    sbt "run path/to/openjdk-root"
