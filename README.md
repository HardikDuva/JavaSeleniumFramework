# Test-Driven-Framework

### Prerequisites
1. Install and set environment variable for java.
    * Windows - https://www.oracle.com/java/technologies/downloads/
    * Linux - ```  sudo apt-get install openjdk-8-jre  ```
    * MacOS - Java should already be present on Mac OS X by default.
2. Install and set environment varibale for Maven.
    * Windows - https://maven.apache.org/install.html
    * Linux/ MacOS -  [Homebrew](http://brew.sh/) (Easier)
    ```
     install maven
    ```
3. Install Docker desktop

### Run your First Test
1. Clone the Data-Drivern-Framework repository. 
```
git clone https://github.com/HardikDuva/TestDrivenFramework.git and make sure all the dependency in pom.xml file are upto date
```
2. Set-up Test Data file and configuration fike
Make sure all the necessary data are there

3. Write test case for Login Page(You can write test case for any page)

4. Set-up Failed screenshot file folder path in BaseTest.java class

5. Set-up AfterSuite method if you want to send execution report to email 
  Add Email address and other required parameter
   
6. Run docker image according to requirnment (Command -> docker compose up)

7. Run xml file from suite directory according to browser requirnment

8. After run successfully you can find execution report in testOutput directory and failed TCS screenshot in "TestResult" directory.
