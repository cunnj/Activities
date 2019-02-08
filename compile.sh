#!/bin/bash
chmod +x gradlew
./gradlew assembleRelease
# https://github.com/appium/sign
wget -c -qq "https://github.com/appium/sign/raw/master/dist/sign.jar"
java -jar sign.jar app/build/outputs/apk/release/app-release-unsigned.apk
mv app/build/outputs/apk/release/app-release-unsigned.s.apk app/build/outputs/apk/release/app-release.apk
echo "Compiled successfully to app/build/outputs/apk/release/app-release.apk"
