# Autism Friendly Locations
Android App Version 2

|Name        |Student No|
|------------|----------|
|Keith Maher |20074612  |

* The app was developed for Mobile App Development module in Waterford Institute of Technology.
* The app was developed with the purpose to help people with autism find locations that cater for their needs.
* The app allows te user to see a global list - Friendly List - of all comments on all locations. 
* The user can then comment on these locations and give feedback on their experience. 
* The app had full CRUD functionality allowing the user to Create, Read, Update and delete their comments. 
* The Search function allows the user either have the app show locations near by or within a radius of a town name. 
* The app can be downloaded here - www.keithjmaher.ie

# API's
* The app users API - www.fourquare.com - Gets a json list of places (Like Google Places). 
* The app also uses the Google Maps API. 

# UX
* Inviting opening screen with some information to help user login
* User stays logged in to avoid having to always type in details - Logged out from within thee app.
* The navigation system is a nav drawer.
* This nav drawer is customised for each use using there logged in details. 

# DX
* Uses mainly fragments.
* Code is tidy and well presented. 

# GitHub
* I used Github throughout the assignment.
* Multiple commits & branches.

# Persistence
* The persistance within this app is Firebase realtime storage.
* Both the Locations and the Comments are stored within Firebase.
* The API makes the call to the endpoint everytime the "Search" fragment is loaded.

# Instillation
* To download and build in Android Studio - Download zip file or git clone repositorie.
* Install app on your phone - Visit www.keithjmaher.ie -> follow instructons, and enjoy.

# Video Demo
https://youtu.be/FaU39UPjQHw

# References
* https://github.com/tommybuonomo/dotsindicator#custom-attributes
* https://foursquare.com/developers/apps
* https://stackoverflow.com/questions/6533942/adding-gif-image-in-an-imageview-in-android
* https://s3.amazonaws.com/gs-forums-gc/monthly_2018_03/5aa367f3285b6_jigsawpuzzle.gif.98b7a5eff7410db227b6b8f51a96cf92.gif
* https://forums.geocaching.com/GC/index.php?/topic/345351-upload-animated-gifs-bug/
* https://www.youtube.com/watch?v=OQy1u2vVHTc&index=4&list=PLF0BIlN2vd8uXXgsh26ndCIJowMavwG2U
* https://github.com/hdodenhof/CircleImageView
* https://stackoverflow.com/questions/26368178/getlayoutinflater-in-fragment
* https://www.thecrazyprogrammer.com/2017/01/how-to-get-current-location-in-android.html
* https://stackoverflow.com/questions/5026349/how-to-open-a-website-when-a-button-is-clicked-in-android-application
* https://www.mkyong.com/java/how-to-calculate-date-time-difference-in-java/
* https://www.androidtutorialpoint.com/basics/android-seekbar-tutorial/
* Damian Mamak - For helping strip V1 app back to basics and implement fragments going forward for V2
