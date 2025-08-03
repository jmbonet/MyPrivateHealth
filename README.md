# MyPrivateHealth
A health monitoring application that uses embedded database for privacy. A comprehensive Android application for tracking and visualizing personal health data. Built with Java and SQLite, this app allows users to manage multiple user profiles and track various health metrics over time.

## Features

### User Management
- Add, edit, and delete user profiles
- Store user information: name, age, and height
- Modern Material Design interface with RecyclerView

### Health Data Tracking
- Record health measurements for each user
- Track multiple health variables:
  - Systolic and Diastolic Blood Pressure
  - Weight
  - Body Fat Percentage
  - BMI (automatically calculated)
- Date-based data entry with automatic BMI calculation
- Support for one health record per user per date (unique constraint)

### Data Visualization
- Interactive line charts using MPAndroidChart library
- Multiple chart types:
  - Blood Pressure trends (Systolic/Diastolic)
  - Weight progression
  - Body composition (Fat percentage)
  - BMI evolution
- User-specific data filtering
- Time-based data visualization

### Database Features
- SQLite embedded database
- Proper data relationships and constraints
- Efficient data retrieval and storage
- Automatic data validation

## Technical Specifications

### Architecture
- **Language**: Java
- **Database**: SQLite (embedded)
- **UI Framework**: Android Material Design Components
- **Charts**: MPAndroidChart library
- **Minimum SDK**: API 21 (Android 5.0)
- **Target SDK**: API 33 (Android 13)

### Database Schema

#### Users Table
- `id` (INTEGER PRIMARY KEY AUTOINCREMENT)
- `name` (TEXT NOT NULL)
- `age` (INTEGER NOT NULL)
- `height` (REAL NOT NULL)

#### Health Data Table
- `id` (INTEGER PRIMARY KEY AUTOINCREMENT)
- `user_id` (INTEGER NOT NULL, FOREIGN KEY)
- `date` (TEXT NOT NULL)
- `systolic_pressure` (INTEGER)
- `diastolic_pressure` (INTEGER)
- `weight` (REAL)
- `fat_percentage` (REAL)
- `bmi` (REAL)
- UNIQUE constraint on (user_id, date)

## Setup Instructions

### Prerequisites
- Android Studio (latest version recommended)
- Android SDK (API 21 or higher)
- Java Development Kit (JDK 8 or higher)

### Installation

1. **Clone the repository**
   ```bash
   cd /home/miquel/git
   git clone <repository-url> MyPrivateHealth
   cd MyPrivateHealth
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the MyPrivateHealth directory and select it

3. **Sync Gradle**
   - Android Studio will automatically sync the project
   - If not, click "Sync Now" in the notification bar

4. **Build the project**
   - Go to Build → Make Project (Ctrl+F9)
   - Resolve any dependency issues if they arise

### Running the Application

1. **Connect a device or start an emulator**
   - Connect an Android device via USB with USB debugging enabled
   - Or start an Android Virtual Device (AVD)

2. **Run the application**
   - Click the "Run" button (green play icon) in Android Studio
   - Select your target device
   - The app will install and launch automatically

## Usage Guide

### Getting Started

1. **Launch the app**
   - The main screen displays three main options

2. **Add Users**
   - Tap "Manage Users"
   - Use the floating action button (+) to add a new user
   - Enter name, age, and height
   - Save the user

3. **Record Health Data**
   - Tap "Add Health Data"
   - Select a user from the dropdown
   - Choose a date
   - Enter health measurements
   - Tap "Calculate BMI" to automatically compute BMI
   - Save the data

4. **View Graphs**
   - Tap "View Graphs"
   - Select a user from the dropdown
   - View the evolution of health metrics over time
   - Interact with charts (zoom, pan, etc.)

### Data Management

- **Editing Users**: Tap the edit icon next to any user in the user list
- **Deleting Users**: Tap the delete icon (this will also delete all associated health data)
- **Updating Health Data**: Select the same user and date to update existing records
- **Data Validation**: The app validates all input data and shows appropriate error messages

## Project Structure

```
MyPrivateHealth/
├── app/
│   ├── src/main/
│   │   ├── java/com/myprivatehealth/app/
│   │   │   ├── MainActivity.java
│   │   │   ├── UserManagementActivity.java
│   │   │   ├── HealthDataActivity.java
│   │   │   ├── GraphActivity.java
│   │   │   ├── UserAdapter.java
│   │   │   ├── DatabaseHelper.java
│   │   │   ├── User.java
│   │   │   └── HealthData.java
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   ├── values/
│   │   │   └── drawable/
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

## Dependencies

The project uses the following main dependencies:

- **AndroidX AppCompat**: For backward compatibility
- **Material Design Components**: For modern UI components
- **MPAndroidChart**: For data visualization
- **SQLite**: For local data storage
- **RecyclerView**: For efficient list display
- **ConstraintLayout**: For flexible layouts

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, questions, or feature requests, please create an issue in the project repository.

## Future Enhancements

Potential improvements for future versions:

- Data export functionality (CSV, PDF)
- Cloud synchronization
- Advanced analytics and insights
- Customizable health metrics
- Reminder notifications
- Data backup and restore
- Multi-language support
- Dark theme support 
