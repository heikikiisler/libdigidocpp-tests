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

## Running tests

##### Android

* Connect single device through USB (Allow USB debugging from device settings).
* Optional: Verify that devices are connected with the command `adb devices`. The output should
  include a list of connected devices with "device" after the UDID.
* Test executor machine and phone must be in same network (Wi-Fi) where devices can see each other. 

##### iOS

* Build validation app with XCode for specific iOS device and install it with XCode *play* button. If you have app signing problems you should change app *bundleID* in XCode project. Trust validation app in phone menu (Settings -> General -> Device Management) after installation. 
* Open Webdriver agent (WDA) project (_/usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent/WebDriverAgent.xcodeproj_) with XCode. Build this app for specific iOS device.  If you have app signing problems you should change app bundleID in XCode project. Install app with XCode *play* button.
* Share Mac Internet to iOS phone. (System Preferences -> Sharing -> Internet Sharing -> iPhone USB).

##### For both mobile OS

* Navigate to project root directory
* Copy `properties.conf.sample` into new file in root directory named `properties.conf` if it does not exist yet.
  * For Android you should copy correct `.apk` file into project root directory and change app file name in *android* section the properties.conf file.
  * For iOS you should add correct parameter values for *udid* (device ID) and *bundleID* (validation app *bundleID*)  properties. 
* Copy files for validation into *dataFiles* directory.
* Copy expected result files (if they exists) into  *dataFiles* directory.
* For Windows replace `./gradlew` with `gradlew` in commands.
* Recommended: add flag `--info` for more logging output.

**Commands**

_Main commands: these will install the corresponding app and run tests on the connected device:_

Android device:

    ./gradlew androidTest --info

iOS device (only on a Mac):

    ./gradlew iosTest --info

_More specific commands:_

Validate files on an Android device:

    ./gradlew validateWithAndroid --info

Validate files on an iOS device:

    ./gradlew validateWithIos --info

Generate TestNG XML file :

    ./gradlew generateSuite --info

Run the validation tests:

This will compare a result JSON file from the resultFilesDirectory defined in properties.conf.
To compare with a specific file in the resultFilesDirectory, add a parameter:
-PresultsFile="resultsFileName". The latest generated results file is used by default. 

    ./gradlew validationTest --info

or

    ./gradlew validationTest --info -PresultsFile="resultsFileName"

**Test reports**

Test report path will be printed after executing tests,  `build/reports/tests/validationTest/index.html` by default. 

* To avoid reports overwriting you can add `-PcustomResultsDir` parameter for `androidTest` , `iosTest` or `validationTest` task. This creates HTML reports to timestamped subdirectory in `build/reports/tests` folder.

To ignore certain files in tests, add lines to `dataFiles/ignored_files.txt` separated by comma: `file name,ignore message`
