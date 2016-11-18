********************************************************************
**********  Welcome to CSCE740 GROUP7's MEAT project ***************
********************************************************************

1. Configuration

  1.1 directory

     * apache-maven-3.3.9 : Automation tool for MEAT project's compiling and packaging. (for Windows)
     * javadoc            : Java document for MEAT project source
     * lib                : External library folder for MEAT system
     * resource           : Contains local database file and external db file (json format)
     * src                : MEAT source files

   2.2 files
     
     * pom.xml            : maven build script file
     * sampleScript.json  : sample file for script running mode


2. How to compile and packaging into executable MEAT.jar 

   2.1. if maven tool is installed in this computer, type in as below      

      >>> mvn install clean 

   2.2 if not installed , type in as below   

      >>> apache-maven-3.3.9\bin\mvn install clean    
   
   3.3 if build successfully, you can find the MEAT.jar in current directory.
     
   
3. How to run the MEAT system
 
   3.1 Interactive   mode   : java -jar MEAT.jar

   3.2 ScriptCommand mode   : java -jar MEAT.jar filename.json
      
   3.3 Load externalDB mode : java -jar MEAT.jar DBSYNC
   
   3.4 DB initilized mode   : java -jar MEAT.jar INITDB