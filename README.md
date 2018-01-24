# libdigidocpp library tests

Automated tests for the [libdigidocpp](https://github.com/metsma/libdigidocpp) mobile library.

## Prerequisites
- Use real Android or iOS phone.

### Linux

**Install**

1. Install JDK 8

  a. set *JAVA_HOME* variable to to refer correct location

  b. add *JAVA_HOME/bin* to PATH variable

2. Install Node.js

3. Install Appium package as global with command: `npm install -g appium`

4. Install Appium Doctor package as global with command: `npm install -g appium-doctor`

5. Execute `appium-doctor` to check installation and follow the guiding

6. Install additional driver for Android `npm install -g appium-uiautomator2-driver`

7. Install *Git* to access test code

**Diagnostics**

* Check connected devices with command `adb devices`

  * *List of devices attached*

    *YT911B22J4      device*

  *  For real phones activate *Developer mode* with *USB debugging*. Use *Charging mode* if phone is not visible with `adb devices` command. Phone may ask permission to connect this computer. Allow it. 

  * Correct status for communication is <u>device</u>. Other statuses (unauthorized, offline) not working.

### Windows

**Install**

* Installation is similar with Linux
* Ignore Python related errors when installing Appium
    (e.g. `gyp ERR! stack Error: Can't find Python executable "python"`). Appium will still install.

**Diagnostics**

* Actions are similar with Linux

### Mac

**Install**

1. Install  brew (*http://brew.sh/* ) and verify installation with command `brew doctor`

2. Install JDK 8

   a. set *JAVA_HOME* variable to to refer correct location

   b. add *JAVA_HOME/bin* to PATH variables

3. Install minimal Android tools for communication (https://dl.google.com/android/repository/platform-tools-latest-darwin.zip)

4. Install Node.js (https://nodejs.org/en/ )  as <u>normal user</u> . If you install Node.js with sudo then later you may have file access problems on Node.js level

5. Install Xcode from Mac store

   a. Register *iOS Developer* account from Xcode

6. Install Carthage dependency manager: `brew install carthage` 

7. Install Appium package as global with command: `npm install -g appium`

8. Install Appium Doctor package as global with command: `npm install -g appium-doctor`

9. Execute `appium-doctor` to check installation and follow the guiding

10. Install additional driver for Android `npm install -g appium-uiautomator2-driver`

11. Install additional tool for package install for iOS 10+ devices `npm install -g --unsafe-perm=true ios-deploy`

12. Install additional tool for authorize iOS devices `npm install -g authorize-ios` and **run** `sudo authorize-ios` **every time when you update Xcode version**!

13. Install xcpretty package for reasonable Xcode output for real devices `gem install xcpretty`

14. Install *Git* to access test code `brew install git`


**Diagnostics**

* TODO

## Running tests

* Connect single device through USB (Allow USB debugging from device settings).
* Optional: Verify that devices are connected with the command `adb devices`. The output should
  include a list of connected devices with "device" after the UDID.
* Navigate to project root directory
* Copy correct apk file (Android) into project root directory and change app file name into properties.conf file
* Copy files for validation into dataFiles directory
* For Windows replace `./gradlew` with `gradlew.bat` in commands.
* Recommended: add flag `--info` for more logging output.

**Commands**

_These commands will install the corresponding app and run tests on all connected devices._


Validate files on an Android device:

    ./gradlew validateWithAndroid --info

Validate files on an iOS device:

    ./gradlew validateWithIos --info

Generate TestNG XML file :

    ./gradlew generateSuite --info

Run the validation tests:

_This will compare a result JSON file from the resultFilesDirectory defined in Config.
To compare with a specific file in the resultFilesDirectory, add a parameter:
-PresultsFile="resultsFileName". The latest results file is used by default._

    ./gradlew validationTest --info

or

    ./gradlew validationTest --info -PresultsFile="resultsFileName"

Run everything together:

    ./gradlew validateWithAndroid generateSuite validationTest --info


**Test reports**

Test report path will be printed after executing tests,  `build/reports/tests/validationTest/index.html` by default.


### Changelog

