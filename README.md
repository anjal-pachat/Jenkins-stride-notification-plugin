# Stride_Notification

Stride plugin for Jenkins
A Jenkins plugin that sends notifications to Stride chat rooms for build events.
## Features
Can send notifications for the following build statuses
-	Build Number
-	Build Status(e.g. build success , build failure ,build unstable)
-	Build URL
-	Build Duration
-	Job Name
## Configuration
The plugin allows only a single level of configuration is listed below 
### Job level configuration
To set up the plugin for an individual job, go to the job's configuration page and add the **Stride Notifications** post-build action. The settings listed there are:
-	**Access Token:** It is the unique token for specific stride chat room where the notification message intend to send .Just copy it and paste it here .
-	**Url:** It’s the Rest Url of Specific stride chat room , just copy the whole URL and paste it here 
How to generate Access token and Url
Go to stride chat room and click on the Apps on the right side ,then click add button it will display different applications from that choose connect your app it prompts a window of custom app , specify a token under  API tokens label and press the create button . Then you can see that token is created ..

-	Copy that access token and paste it on the access token field in the Jenkins job configuration page
-	Copy the whole URL and paste it on the URL field in the Jenkins job configuration page


