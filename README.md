# LIFX Scene Switch
## Summary
Custom device handler for LIFX scenes.

## This device handler supports
On / Off of LIFX Scene

## Installation via GitHub Integration
1. Open SmartThings IDE in your web browser and log into your account.
2. Click on the "My Device Handlers" section in the navigation bar.
3. Click on "Settings".
4. At the bottom of the popup window, click "Add New Repository".
5. Enter "yenba" as the namespace.
6. Enter "LIFX-Scene-Switch" as the repository.
7. Ensure that the branch is set to "master", it should be this way by default.
8. Hit "Save".
9. Select "Update from Repo" and select "LIFX-Scene-Switch".
10. Select (check) "lifx-scene-switch.groovy".
11. Check "Publish" and hit "Execute".
12. See the "Preferences" & "How to get your API Token" sections below on how to configure.

## How to get your API Token
Navigate to https://cloud.lifx.com, sign in and then go to the settings section and select generate new token.

## Preferences
1. API Token - [Required] You have to get this from LIFX. It is a long character string so text it to yourself and copy and paste it in.
2. Bulb Name - [Required] Must match the exact bulb you want to control. Use the name  you give it in the LIFX app.
3. Default Transition Time - [Required] You can enter the speed of the transition into the scene in seconds here.
4. Log Level - Enter: TRACE, DEBUG, INFO, WARN, ERROR
