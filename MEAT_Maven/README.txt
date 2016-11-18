********************************************************************
**********  Welcome to CSCE740 GROUP7's MEAT project ***************
********************************************************************


0. Pre-Required Installation
   ________________________________________
   * JDK 1.8 (Jave Development Kit 8)
   * Apache Maven 3.x
   ________________________________________

1. Configuration (Directory)
  -----------------------------------------
   * javadoc            : Java document for MEAT project source
   * lib                : External library folder for MEAT system
   * resource           : Contains local database file and external db file (json format)
   * src                : MEAT source files
   ------------------------------------------ 
   * pom.xml            : maven build script file
   * sampleScript.json  : sample file for script running mode
   

2. How to Compile and Pack into executable MEAT.jar
    
   Prompt> mvn install clean 
   
   * If build successfully, you can find the MEAT.jar in current directory.
     
   
3. How to run the MEAT system
   ------------------------------------------
   3.1 Interactive   mode   : java -jar MEAT.jar
   ------------------------------------------
   3.2 ScriptCommand mode   : java -jar MEAT.jar filename.json
   -------------------------------------------   
   3.3 Load externalDB mode : java -jar MEAT.jar DBSYNC
   -------------------------------------------
   3.4 DB initilized mode   : java -jar MEAT.jar INITDB