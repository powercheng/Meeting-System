********************************************************************
**********  Welcome to CSCE740 GROUP7's MEAT project ***************
********************************************************************

0. Pre-Required Installation
   ________________________________________
   * JDK 1.8 (Jave Development Kit 8)
   * Apache Maven 3.x
   ________________________________________

1. Required Java libraries 
  -----------------------------------------
  * json-simple-1.1.jar     : JSON date type helper
  * sqlite-jdbc-3.15.0.jar  : Sqlite db manipulater 
  * junit-4.12.jar          : Testing framework 
 
2. Configuration (Directory)
  -----------------------------------------
   * Coverage Report    : Test coverage repoort driectory
   * javadoc            : Java document for MEAT project source
   * lib                : External library folder for MEAT system
   * resource           : Contains local database file and external db file (json format)
   * src                : MEAT source files
   * test               : JUNIT test java files
   ------------------------------------------ 
   * pom.xml            : maven build script file
   * sampleScript.json  : sample file for script running mode
   * sampleScriptError.json  : sample error file for script running test mode
   

3. How to compile, test, and pack into executable MEAT.jar
    
   Prompt> mvn install clean 
   
   * Test suite will run automatically and the results be shown on the screen
   * If build successfully, you can find the MEAT.jar in current directory.
     
   
4. How to run the MEAT system
   ------------------------------------------
   4.1 Interactive   mode   : java -jar MEAT.jar
   ------------------------------------------
   4.2 ScriptCommand mode   : java -jar MEAT.jar filename.json
   -------------------------------------------   
   4.3 Load externalDB mode : java -jar MEAT.jar DBSYNC
   -------------------------------------------
   4.4 DB initilized mode   : java -jar MEAT.jar DBINIT
