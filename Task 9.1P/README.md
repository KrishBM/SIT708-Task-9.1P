# Lost & Found Mobile Application - Task 9.1P

A comprehensive Android mobile application for managing lost and found items with integrated map functionality and location services.

## Features

### Core Functionality
- **Create Lost/Found Advertisements**: Users can post details about lost or found items
- **View All Items**: Browse through all posted lost and found items in a list format
- **Interactive Map View**: Display all items with location coordinates on Google Maps
- **Location Services**: Support for both GPS current location and manual location entry
- **Item Details**: View comprehensive details of any posted item
- **Remove Items**: Delete items from the database when no longer needed

### Technical Features
- **Google Maps Integration**: Real-time map display with custom markers
- **Location Permission Handling**: Secure access to device location services
- **SQLite Database**: Local storage for all item data
- **Material Design UI**: Modern and intuitive user interface
- **Form Validation**: Comprehensive input validation for all fields

## Technologies Used

- **Platform**: Android (API Level 24+)
- **Language**: Java
- **Database**: SQLite with custom DatabaseHelper
- **Maps**: Google Maps SDK for Android
- **Location**: Google Play Services Location API
- **UI Framework**: Material Design Components
- **Architecture**: Activity-based with singleton database pattern

## Setup Instructions

### Prerequisites
- Android Studio (Latest version recommended)
- Android SDK API Level 24 or higher
- Google Play Services installed on target device
- Valid Google Maps API Key

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/lost-found-app.git
   cd lost-found-app
   ```

2. **Configure Google Maps API Key**
   - Open `app/src/main/res/values/strings.xml`
   - Replace the API key value with your valid Google Maps API key:
   ```xml
   <string name="google_maps_key">YOUR_API_KEY_HERE</string>
   ```

3. **Build and Run**
   - Open the project in Android Studio
   - Sync project with Gradle files
   - Run the application on an emulator or physical device

### API Key Setup

1. Visit [Google Cloud Console](https://console.cloud.google.com)
2. Create a new project or select existing project
3. Enable "Maps SDK for Android" API
4. Create credentials (API Key)
5. Configure API key restrictions for security
6. Add the API key to your `strings.xml` file

## Application Structure

### Activities
- **MainActivity**: Home screen with navigation cards
- **CreateAdvertActivity**: Form for posting new lost/found items
- **ItemListActivity**: Display all items in RecyclerView
- **MapActivity**: Interactive map showing item locations
- **ItemDetailActivity**: Detailed view of individual items

### Database Schema
- **Items Table**: id, type, name, phone, description, date, location, latitude, longitude
- **DatabaseHelper**: Singleton pattern for database operations

### Key Components
- **Custom Markers**: Red markers for lost items, green for found items
- **Location Services**: GPS integration with permission handling
- **Form Validation**: Required field validation and data integrity checks

## Usage Guide

### Posting an Item
1. Tap "CREATE ADVERT" on the home screen
2. Select "Lost" or "Found" type
3. Fill in item details (name, phone, description, date)
4. Enter location manually or use "GET CURRENT LOCATION"
5. Tap "SAVE" to post the item

### Viewing Items
1. Tap "VIEW ALL ITEMS" to see list view
2. Tap "SHOW ON MAP" to see items on interactive map
3. Tap any item in the list to view detailed information

### Managing Items
1. Open item details view
2. Use "REMOVE" button to delete items when resolved

## Permissions Required

- **ACCESS_FINE_LOCATION**: For GPS-based current location
- **ACCESS_COARSE_LOCATION**: For network-based location
- **INTERNET**: For Google Maps and location services

## Testing

### Test Scenarios
1. **Create Items**: Test both lost and found item creation
2. **Location Services**: Test both manual location entry and GPS location
3. **Map Display**: Verify markers appear correctly with proper colors
4. **Data Persistence**: Ensure items persist after app restart
5. **Permission Handling**: Test location permission request flow

### Known Limitations
- Items with manual location entry (no GPS coordinates) won't appear on map
- Requires active internet connection for map functionality
- Current location requires device location services to be enabled

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request

## License

This project is developed as part of SIT708 Mobile Application Development coursework.

## Support

For technical issues or questions related to this project, please create an issue in the GitHub repository. 