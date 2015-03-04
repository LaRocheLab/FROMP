Running Instructions
FROMP is Java program and hence requires an installed and working Java version. You can get the latest java version from \url{http://java.com/en/}.

For Windows
Unzip the .zip file to extract the Fromp-v1.0 folder and double click on the "dnarna.jar" file. This will create a FROMP.bat file with instructions to increase the java heapsize which is required when dealing with very large input files. After the first run, you can use the .bat file for subsequent runs of the program.

For Linux
The default java environment in Linux is the OpenJDK. However, if you are not sure which java environment came with your version of linux, you should get the latest Sun Java JDK and the Java Runtime Environment (JRE) and then congfigure your Linux so that it uses the Sun java instead of the default. You can find tips on how to do that for Fedora, RHEL and CentOS at \url{http://www.freetechie.com/blog/installing-sun-java-on-fedora-12/} and for Ubuntu at \url{https://help.ubuntu.com/community/Java}


Unzip the .zip file to extract the Fromp-v1.0 folder. Change directory to this folder and type: 

java -Xms128m -Xmx256m -jar dnarna.jar start

This fixes the heapspace (or memory required to run FROMP). In case you get an "java.lang.OutOfMemoryError: Java heap space" error while running huge samples, you can try changing the -Xmx option to  -Xmx512m depending upon the RAM capacity of your machine.
