# CitySolution : Android Mobile Applications with Deep Learning-Based Image Classification for Smart City Corporation

This repository contains two Android applications designed to facilitate the reporting and resolution of various civic issues within cities across Bangladesh. 

## User Application
The User App empowers citizens to report problems they encounter in their cities. Key features include:
- **Image Capture**: Users can capture images of different issues.
- **Location Tracking**: The app automatically fetches the location of the problem using Google's Fused Location Services. Users can also manually adjust the location if needed.
- **Machine Learning Classification**: Problems are classified into four categories using an embedded ML model within the app. They are -damaged roads, trash, homeless people, and floods.
- **Notification System**: Users receive in-app notifications about the status of their reported issues.

## Authority Application
The Authority App is designed for government authorities to manage and address reported issues effectively. Its features include:
- **Problem Categorization**: Authorities can view categorized problems within their corresponding cities. If a problem lies outside the four categories of the model, the authority can manually assign its category.
- **Status Management**: Authorities can set the status of reported problems (e.g., "In Progress," "Completed," etc.), which users can view.
- **Fraud Detection**: Authorities can manually handle fake complaints to ensure the integrity of the reporting system.
- **Higher Authority Segment**: A dedicated segment allows the higher authority to oversee and manage problems reported from all cities. Higher officials can also delete city corporation employee accounts.
- **Graphican Views**: Graphical views are  included for easier monitoring.

## Components Used
This project utilizes various components and technologies, including:

- **Android Studio**: Integrated Development Environment (IDE) for Android app development.
- **Java**: Programming language used for developing Android apps.
- **Firebase**: Backend services for mobile and web applications, used for authentication, real-time database, etc.
- **Google Fused Location Services**: API for retrieving accurate location information on Android devices.
- **Teachable Machine**: A machine learning platform by Google, used for training and deploying machine learning models.

## Repository Structure
- **CitySolution_User**: Contains the source code and resources for the User App.
   CitySolution_User/app/src/main/java/com/example/usersafecity : Contains the Java Files
   CitySolution_User/app/src/main/res/layout : Contains the xml file
- **CitySolution_Authority**: Contains the source code and resources for the Authority App.
   CitySolution_Authority/app/src/main/java/com/example/safecity : Contains the Java Files
   CitySolution_Authority/app/src/main/res/layout : Contains the xml files


## Installation
1. Clone the repository.
2. Open the respective Android projects in Android Studio.
3. Build and run the apps on your device or emulator.
   
**Or**

 You can directly find the executable file(app) in the apk folder. Download and Install it directly to your android mobile phone.

 

 ## Demo Use
  You can find the screen-recordings of the two apps inside the AppVideo folder. 
  User Manual.pdf also contains the instructions for using the applications.

  

## Deploying Code with Modified Firebase Account
For deploying the code, Android Studio dolphin version is needed. Here is the step-by-step procedure for deploying the code with a modified firebase account.
-	**Clone the Repository:**
Clone the project repository to your local machine using Git.
-	**Open the Project in Android Studio:**
Open Android Studio.
Click on "Open an existing Android Studio project" and navigate to the cloned project directory.
-	**Create a Firebase Project:**
Go to the Firebase Console.
Click on "Add project" and follow the instructions to create a new Firebase project.
-	**Add Android App to Firebase:**
In your Firebase project overview, click on the Android icon to add an Android app.
Register your app with the package name of your Android project (you can find this in the AndroidManifest.xml file).
Download the google-services.json file provided by Firebase.
-	**Configure Firebase in Your Project:**
Move the downloaded google-services.json file to the app directory of your Android Studio project.
Open the build.gradle file (Project level) and ensure you have the following classpath in the dependencies section:
classpath 'com.google.gms:google-services:4.3.10' // or the latest version
-	**Update Firebase Rules:**
In the Firebase Console, navigate to the Realtime Database and Storage sections.
Update the rules to ensure proper access permissions during development. 
-	**Run the Project:**
Sync your project with Gradle files.
Run the project on an emulator or a physical device.

 

## License
This project is licensed under the [Apache License 2.0](LICENSE).

## Contact
For questions or inquiries, please contact [Farhatun Shama](mailto:farhatunshama@gmail.com), and [Lamisa Bintee Mizan Deya](mailto:lamisa.deya2001@gmail.com).



