# AppFirewall FX Packaging

Greatly inspired by [Santulator](https://santulator.github.io/)

Only Linux is supported.

## Preparing your Build Environment

### JDK 11

You will need to install JDK 11 to build the installable bundle of AppFirewall FX.  E.g. the version provided by [AdoptOpenJDK](https://adoptopenjdk.net/).

### Java Packager

Download the Java Packager.  Choose the linux link for your platform from those listed [on this page](https://mail.openjdk.java.net/pipermail/openjfx-dev/2018-September/022500.html).  Note that this is an early access version of the Java Packager, made available for JDK 11 while [JEP 343](https://jdk.java.net/jpackage/) is being developed.

## Creating the Installable Bundle

### Creating the Bundle

Assuming that you have installed the [Java Packager](#java-packager) into `/opt/jpackager-11/`, you can run the following command to create the installable bundle:

    ./gradlew clean createBundle -PjavaPackagerPath=/opt/jpackager-11

### Stripping debug symbols
For a smaller package size the stripping of Debug symbols is currently necessary.

See issue: [JDK-8214796](https://bugs.openjdk.java.net/browse/JDK-8214796).

    strip -p --strip-unneeded runtime/lib/server/libjvm.so


## Using the Java Packager with JDK 11

For more general information about the approach taken to building the installable bundles for Mac, Linux and Windows and how you could apply this in your own project, see the article [Using the Java Packager with JDK 11].

[Using the Java Packager with JDK 11]:https://medium.com/@adam_carroll/java-packager-with-jdk11-31b3d620f4a8
