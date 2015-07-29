Running Instructions
FROMP is Java program and hence requires an installed and working Java version. You can get the latest java version from \url{http://java.com/en/}.

For Windows
Unzip the .zip file to extract the Fromp-v1.0 folder and double click on the "FROMP.jar" file. 

For Linux
The default java environment in Linux is the OpenJDK. However, if you are not sure which java environment came with your version of linux, you should get the latest Sun Java JDK and the Java Runtime Environment (JRE) and then congfigure your Linux so that it uses the Sun java instead of the default. You can find tips on how to do that for Fedora, RHEL and CentOS at \url{http://www.freetechie.com/blog/installing-sun-java-on-fedora-12/} and for Ubuntu at \url{https://help.ubuntu.com/community/Java}


Unzip the .zip file to extract the Fromp-v1.0 folder. Change directory to this folder and type: 

For GUI version:

java -jar FROMP.jar 

For command line:

java -jar FROMP.jar <insert arguments here>

Note: Using the "p" or "a" command with a large amount of samples may cause an "java.lang.OutOfMemoryError: Java heap space" error. You can try you can try
changing the -Xmx option (which goes in front of the -jar) to -Xmx512m depending upon the RAM capacity of your machine. If this fails to work it is better to
export your project and then open it in the GUI FROMP in order to export the desired pathway images. 

