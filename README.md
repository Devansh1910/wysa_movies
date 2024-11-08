Wysa Movies 🎬
An Android Application showcasing the latest movies with data from the TMDB API. This project is developed in Kotlin using the MVVM architecture as part of the Wysa Android Assessment. It features a sleek, modern UI and smooth performance, allowing users to explore and view movie details easily.

Features ✨
Explore Latest Movies: View a curated list of trending movies.
Detailed View: Get in-depth information on each movie, including release date, synopsis, rating, and more.
Favorite Selection: Mark and save favorite movies for quick access.
Endless Scrolling: Seamlessly browse through a continuous list of movies.
Offline Access: Access saved favorites offline with RoomDB integration.
Jetpack Compose UI: A responsive, modern UI built with Jetpack Compose.
API Integration with Retrofit: Reliable, high-performance network calls for fetching data.
Screenshots 📸
Explore the app's sleek and intuitive interface:

<p float="left" align="center"> <img src="https://github.com/user-attachments/assets/21fb71e8-bbd5-4bc3-8e27-9cba2e17685a" width="45%" alt="Explore Interface"/> <img src="https://github.com/user-attachments/assets/3fbce2e1-9ef9-4d51-8b83-9fcc23f95e76" width="45%" alt="Details Interface"/> </p>
Tech Stack 🛠️
Language: Kotlin
Architecture: MVVM (Model-View-ViewModel)
UI Framework: Jetpack Compose
Networking: Retrofit for network requests
Database: RoomDB for offline data storage
Image Loading: Coil for smooth and efficient image handling
How to Run 🚀
Clone the repository:
bash
Copy code
git clone https://github.com/yourusername/WysaMovies.git
Navigate to the project directory:
bash
Copy code
cd WysaMovies
Get an API Key from TMDB and add it to your local.properties:
properties
Copy code
TMDB_API_KEY=your_api_key_here
Open the project in Android Studio, sync the Gradle files, and run the app on an emulator or physical device.
Project Structure 📂
This project follows the MVVM architecture pattern:

Data: Manages the data sources and models.
UI (View): Jetpack Compose for declarative UI.
ViewModel: Connects the UI to the data and handles logic.
Requirements 📋
Minimum SDK: Android 5.0 (Lollipop)
Tools: Android Studio Bumblebee or later
Contributing 🤝
Contributions are welcome! If you'd like to improve the project, please fork the repository and make a pull request. Follow these steps:

Fork the project
Create a feature branch (git checkout -b feature/NewFeature)
Commit your changes (git commit -m 'Add a new feature')
Push to the branch (git push origin feature/NewFeature)
Open a Pull Request
License 📜
This project is licensed under the MIT License - see the LICENSE file for details.
