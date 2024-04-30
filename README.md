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

## License
This project is licensed under the [Apache License 2.0](LICENSE).

## Contact
For questions or inquiries, please contact [Farhatun Shama](mailto:farhatunshama@gmail.com), and [Lamisa Bintee Mizan Deya](mailto:lamisa.deya2001@gmail.com).



