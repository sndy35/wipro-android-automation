
# wipro-android-automation

# Run a Test

1. Make sure Appium server is started(If appium server is not started locally, provider ip and port using -DappiumServer and -DappiumPort
2. Run `adb devices` to get the udid of connected android device
3. Run the below command from root folder


java -cp target/wipro-android-automation-0.0.1-SNAPSHOT-tests.jar:target/libs/*:target/wipro-android-automation-0.0.1-SNAPSHOT.jar -Dplatform=android  -DplatformVersion=androidVersion  -Dudid=udid  -DdeviceName=udid  -DappPackage=com.amazon.mShop.android.shopping -DappActivity=com.amazon.mShop.splashscreen.StartupActivity -DnewCommandTimeout=5000 org.testng.TestNG  testng_files/amazon.xml  


# Architecture of wipro-android-automation

This Framework uses Page Object Model with data driven approach. Data is passed as xml via paramters in testNG and it is parsed usign JAXB

