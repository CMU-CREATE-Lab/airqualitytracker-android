Air Quality Tracker For Android
===============================

Overview
--------

The **Air Quality Tracker For Android** is an application for Android OS (minimum SDK version 11). This project pulls information from the [ESDR](https://github.com/CMU-CREATE-Lab/esdr) project through HTTP requests and displays it on the screen conveniently for the user.

Development Environment
-----------------------

To grab the repository, be sure to perform a recursive `git clone --recursive` as this project also utilizes the [Volley](https://developer.android.com/training/volley/index.html) repository for HTTP request handling.

This application was developed primarily using Android Studio v1.5.1 on Debian Linux. One known (minor) issue with this development environment was an issue with the interface builder rendering previews with Gradle (Please see the known issues tracked by android [here](http://tools.android.com/knownissues#TOC-Can-t-Render-Layouts-in-Android-Studio-1.2), as of June 2015). The Gradle version was changed from 1.2.3 to 1.1.3 in order to fix this. Also, the "compileSdkVersion" was changed from 22 to 21 (Check `app/build.gradle`).

Installation of Android Studio for this system required downloading the JDK from Oracle *directly*: [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html). I also needed to add the following environment variable via the terminal for executing Android Studio (`/opt` is where I extracted the JDK archive, and this was the most recent version as of May 2015):

    export JAVA_HOME=/opt/jdk1.8.0_45

If you use iBus on your system, you may also want to add another environment variable:

    export XMODIFIERS=/path/to/android-studio/bin/studio.sh

For setting up other, less-awesome development environments, please refer to the Android documentation. And good luck ;)
