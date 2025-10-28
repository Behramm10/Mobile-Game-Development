# 🎮 Tetris Game Collection

A modern Android game collection featuring multiple classic games with Material Design 3 UI, Firebase integration, and enhanced gameplay features.

## 🎯 Features

### 🎮 Games Included

1. **Tetris** - Classic block-stacking puzzle game
2. **Tic-Tac-Toe** - Strategic X & O game with AI opponent
3. **Clicker Game** - Fast-paced clicking challenge
4. **Snake** - Classic snake game with swipe controls
5. **Memory Match** - Card matching memory game

### 🎨 Modern UI Features

- **Material Design 3** - Latest Android design system
- **Gradient Backgrounds** - Beautiful visual effects
- **Card-based Layout** - Modern card design for all screens
- **Dark Mode Support** - Automatic theme switching
- **Smooth Animations** - Enhanced user experience
- **Responsive Design** - Optimized for different screen sizes

### 🔥 Firebase Integration

- **User Authentication** - Secure login/registration
- **Score Tracking** - Best scores saved per user per game
- **Leaderboards** - Global and personal high scores
- **Offline Support** - Works without internet connection
- **Real-time Updates** - Live leaderboard updates

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Android SDK API 21+ (Android 5.0+)
- Firebase project setup
- Google Services configuration

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/tetris-game-collection.git
   cd tetris-game-collection
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Configure Firebase**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Enable Authentication (Email/Password)
   - Enable Firestore Database
   - Download `google-services.json` and place it in `app/` directory

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

## 🎮 Game Details

### Tetris Game
- **Board Size**: 8 columns × 19 rows
- **Features**:
  - Next piece preview
  - Difficulty levels (Easy/Medium/Hard)
  - Level progression with increasing speed
  - Hard drop functionality
  - Score tracking with line clear bonuses
  - Thick border visualization
  - Well-defined grid lines

### Tic-Tac-Toe
- **AI Opponent**: Minimax algorithm implementation
- **Difficulty Levels**: Easy/Medium/Hard
- **Game Modes**: Single player vs AI, Two player
- **Features**:
  - Win streak tracking
  - Score tracking across rounds
  - Animated winning line highlights

### Clicker Game
- **Combo System**: Multiplier based on click speed
- **Difficulty Modes**: Different time limits
- **Features**:
  - CPS (Clicks Per Second) tracking
  - Visual feedback animations
  - High score persistence

### Snake Game
- **Controls**: Swipe gestures or button controls
- **Features**:
  - Increasing speed as snake grows
  - Score based on food eaten
  - High score tracking
  - Modern grid-based rendering

### Memory Match
- **Grid Sizes**: 4×4, 5×5, 6×6 (Easy/Medium/Hard)
- **Features**:
  - Card flip animations
  - Move counter and timer
  - Score based on time and moves
  - Difficulty progression

## 🏗️ Project Structure

```
app/
├── src/main/
│   ├── java/com/MAD/tetrisgame/
│   │   ├── GameManager.java              # Main menu activity
│   │   ├── LoginActivity.java           # User authentication
│   │   ├── RegistrationActivity.java    # User registration
│   │   ├── TetrisActivity.java          # Tetris game
│   │   ├── GameEngine.java              # Tetris game logic
│   │   ├── GameView.java                # Tetris rendering
│   │   ├── NextPieceView.java           # Next piece preview
│   │   ├── TicTacToeActivity.java       # Tic-Tac-Toe game
│   │   ├── ClickerGameActivity.java     # Clicker game
│   │   ├── SnakeGameActivity.java       # Snake game
│   │   ├── SnakeEngine.java             # Snake game logic
│   │   ├── SnakeView.java               # Snake rendering
│   │   ├── MemoryMatchActivity.java     # Memory Match game
│   │   ├── MemoryCard.java              # Memory card model
│   │   ├── MemoryCardAdapter.java       # Memory card adapter
│   │   ├── LeaderboardActivity.java     # Leaderboard display
│   │   └── ScoreManager.java            # Firebase score management
│   ├── res/
│   │   ├── layout/                      # XML layouts
│   │   ├── values/                      # Colors, themes, strings
│   │   ├── drawable/                    # Gradients, backgrounds
│   │   └── mipmap/                      # App icons
│   └── AndroidManifest.xml
└── build.gradle.kts                     # Dependencies
```

## 🎨 Design System

### Color Palette
- **Primary**: Vibrant blue (#2196F3)
- **Secondary**: Purple (#9C27B0)
- **Tertiary**: Teal (#009688)
- **Error**: Red (#F44336)
- **Surface**: White/Light gray
- **Background**: Gradient backgrounds

### Typography
- **Headings**: Bold, large text
- **Body**: Regular weight
- **Buttons**: Bold, uppercase
- **Scores**: Large, prominent display

## 🔧 Technical Details

### Dependencies
```kotlin
implementation("com.google.android.material:material:1.11.0")
implementation("androidx.cardview:cardview:1.0.0")
implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
implementation("com.google.firebase:firebase-auth:22.3.0")
implementation("com.google.firebase:firebase-firestore:24.9.1")
```

### Architecture
- **Activities**: Each game has its own activity
- **Custom Views**: Game rendering with custom drawing
- **Game Engines**: Separate logic classes for game mechanics
- **Firebase**: Authentication and Firestore for data storage
- **Material Design**: Modern UI components and theming

### Performance Optimizations
- **Efficient Rendering**: Custom view drawing with minimal allocations
- **Firebase Caching**: Offline persistence enabled
- **Score Optimization**: Only best scores stored per user
- **Memory Management**: Proper cleanup in game loops

## 🏆 Scoring System

### Tetris Scoring
- **Single Line**: 100 points
- **Double Lines**: 300 points
- **Triple Lines**: 500 points
- **Tetris (4 lines)**: 800 points
- **Level Bonus**: Speed increases with level

### Other Games
- **Tic-Tac-Toe**: Win/loss tracking
- **Clicker**: Points per click + combo multiplier
- **Snake**: 10 points per food eaten
- **Memory Match**: 100 points per match + time bonus

## 🔐 Firebase Configuration

### Authentication
```java
// Email/Password authentication enabled
FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
```

### Firestore Collections
- **userScores**: Best scores per user per game
- **scores**: General leaderboard data
- **userStats**: User statistics and achievements

### Security Rules
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```


## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**BEHRAMM UMRIGAR**
- GitHub: (https://github.com/behramm10)
- Email: behramm.umrigar200@nmims.edu.in

