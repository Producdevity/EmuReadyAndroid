# EmuReady Mobile App - Complete Technical Specification for Native Android

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Architecture & Design Patterns](#architecture--design-patterns)
4. [UI/UX Specifications](#uiux-specifications)
5. [Feature Specifications](#feature-specifications)
6. [Data Models & API Integration](#data-models--api-integration)
7. [Authentication & Security](#authentication--security)
8. [Storage & Data Management](#storage--data-management)
9. [Eden Emulator Integration](#eden-emulator-integration)
10. [Performance & Optimization](#performance--optimization)
11. [Build & Distribution](#build--distribution)
12. [Development Guidelines](#development-guidelines)

---

## Project Overview

### App Identity
- **App Name**: EmuReady
- **Package Name**: `com.emuready.emuready`
- **Version**: 1.0.0
- **Target Platform**: Android (minimum SDK 24, target SDK 34)
- **Primary Use Case**: Gaming handheld device compatibility for Nintendo Switch emulation

### Core Purpose
EmuReady is a companion app for Android gaming handhelds that provides:
- Curated game listings with compatibility ratings
- Device-specific emulator configurations
- Direct integration with Eden Nintendo Switch emulator
- Community-driven compatibility database
- Performance optimization presets

---

## Technology Stack

### Core Framework
- **Language**: Kotlin 100%
- **UI Framework**: Jetpack Compose with Material Design 3
- **Architecture**: MVVM with Clean Architecture principles
- **Dependency Injection**: Hilt
- **Navigation**: Jetpack Navigation Compose

### Networking & Data
- **HTTP Client**: Retrofit 2 with OkHttp3
- **JSON Parsing**: Kotlinx Serialization
- **Image Loading**: Coil for Compose
- **Data Caching**: Room Database + DataStore

### Authentication
- **Auth Provider**: Clerk Android SDK
- **Token Management**: Encrypted SharedPreferences
- **Biometric Auth**: AndroidX Biometric

### UI & Animation
- **Theme System**: Material Design 3 Dynamic Colors
- **Animations**: Jetpack Compose Animations
- **Loading States**: Custom skeleton loaders
- **Icons**: Material Icons Extended

### Storage & Performance
- **Local Database**: Room with SQLite
- **Preferences**: DataStore (Preferences & Proto)
- **Image Caching**: Coil with disk cache
- **Background Tasks**: WorkManager

### Device Integration
- **Intent Handling**: Native Android Intent system
- **File System**: Storage Access Framework
- **Hardware Detection**: Custom device detection library
- **Performance Monitoring**: Custom metrics collection

---

## Architecture & Design Patterns

### Clean Architecture Layers

```
┌─────────────────────────────────────┐
│           Presentation Layer        │
│  (UI, ViewModels, Compose Screens)  │
├─────────────────────────────────────┤
│           Domain Layer              │
│     (Use Cases, Repositories,       │
│      Entities, Business Logic)      │
├─────────────────────────────────────┤
│            Data Layer               │
│   (API, Database, Preferences,      │
│    External Services, Mappers)      │
└─────────────────────────────────────┘
```

### Project Structure
```
app/src/main/java/com/emuready/
├── presentation/
│   ├── ui/
│   │   ├── screens/
│   │   ├── components/
│   │   └── theme/
│   ├── viewmodels/
│   └── navigation/
├── domain/
│   ├── usecases/
│   ├── repositories/
│   ├── entities/
│   └── exceptions/
├── data/
│   ├── remote/
│   ├── local/
│   ├── repositories/
│   └── mappers/
├── core/
│   ├── di/
│   ├── utils/
│   └── constants/
└── MainActivity.kt
```

### Key Design Patterns
- **Repository Pattern**: Data abstraction layer
- **Use Case Pattern**: Business logic encapsulation
- **Observer Pattern**: UI state management with StateFlow
- **Factory Pattern**: ViewModels and use case creation
- **Adapter Pattern**: API response mapping
- **Strategy Pattern**: Different emulator launch methods

---

## UI/UX Specifications

### Design System

#### Color Palette
```kotlin
// Primary Brand Colors (matching website)
val PrimaryBlue = Color(0xFF023c69)
val PrimaryBlueDark = Color(0xFF012a4a)
val PrimaryBlueLight = Color(0xFF1565c0)

// Secondary Colors
val AccentGreen = Color(0xFF4caf50)
val AccentOrange = Color(0xFFff9800)
val AccentRed = Color(0xFFf44336)

// Neutral Colors
val SurfaceLight = Color(0xFFffffff)
val SurfaceDark = Color(0xFF121212)
val OnSurfaceLight = Color(0xFF1a1a1a)
val OnSurfaceDark = Color(0xFFe0e0e0)
```

#### Typography Scale
```kotlin
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
)
```

#### Spacing System
```kotlin
object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
}
```

#### Animation Specifications
```kotlin
object AnimationConstants {
    const val FAST_ANIMATION = 200
    const val NORMAL_ANIMATION = 300
    const val SLOW_ANIMATION = 500
    
    val FastEasing = FastOutSlowInEasing
    val StandardEasing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
}
```

### Layout Guidelines

#### Responsive Design
- **Portrait Mode**: Standard mobile layout with bottom navigation
- **Landscape Mode**: Optimized for gaming handhelds with side navigation
- **Tablet Mode**: Two-pane layout with master-detail pattern
- **Adaptive Layouts**: Dynamic sizing based on screen size and orientation

#### Navigation Pattern
```kotlin
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Browse : Screen("browse")
    object Create : Screen("create")
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")
    object GameDetail : Screen("game/{gameId}")
    object ListingDetail : Screen("listing/{listingId}")
    object DeviceSettings : Screen("devices")
    object EmulatorTest : Screen("emulator-test")
}
```

#### Bottom Navigation
- **Home**: Game discovery and featured content
- **Browse**: Search and filter games/listings
- **Create**: Add new game listings
- **Notifications**: User alerts and updates
- **Profile**: User settings and account management

---

## Feature Specifications

### 1. Home Screen

#### Components
- **Hero Section**: Featured games and announcements
- **Quick Actions**: Direct access to common tasks
- **Recent Activity**: User's recent games and listings
- **Recommended Games**: Personalized recommendations
- **Device Status**: Current handheld device information

#### Implementation Details
```kotlin
@Composable
fun HomeScreen(
    homeState: HomeState,
    onGameClick: (Game) -> Unit,
    onCreateListing: () -> Unit,
    onTestEmulator: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg)
    ) {
        item { HeroSection(homeState.featuredGames, onGameClick) }
        item { QuickActionsRow(onCreateListing, onTestEmulator) }
        item { RecentActivitySection(homeState.recentActivity) }
        item { RecommendationsSection(homeState.recommendations, onGameClick) }
        item { DeviceStatusCard(homeState.deviceInfo) }
    }
}
```

### 2. Game Browser

#### Features
- **Search**: Real-time search with suggestions
- **Filters**: Platform, compatibility rating, genre, popularity
- **Sorting**: Alphabetical, rating, date added, popularity
- **Grid/List Toggle**: User preference for display mode
- **Infinite Scroll**: Paginated loading

#### Search Implementation
```kotlin
@Composable
fun BrowseScreen(
    browseState: BrowseState,
    onSearchQuery: (String) -> Unit,
    onFilterChange: (GameFilter) -> Unit,
    onGameClick: (Game) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = browseState.searchQuery,
            onQueryChange = onSearchQuery,
            placeholder = "Search games..."
        )
        
        FilterChips(
            selectedFilters = browseState.filters,
            onFilterChange = onFilterChange
        )
        
        GameGrid(
            games = browseState.games,
            isLoading = browseState.isLoading,
            onGameClick = onGameClick
        )
    }
}
```

### 3. Game Detail Screen

#### Content Sections
- **Game Header**: Title, cover art, basic info
- **Compatibility Ratings**: Device-specific performance data
- **Screenshots**: Gallery of gameplay images
- **Community Listings**: User-submitted configurations
- **Comments**: User reviews and discussions
- **Emulator Actions**: Direct launch options

#### Data Structure
```kotlin
data class GameDetail(
    val id: String,
    val title: String,
    val titleId: String, // Nintendo Switch Title ID
    val coverImageUrl: String,
    val screenshotUrls: List<String>,
    val description: String,
    val releaseDate: LocalDate,
    val developer: String,
    val publisher: String,
    val genres: List<String>,
    val compatibilityRatings: Map<String, CompatibilityRating>,
    val listings: List<GameListing>,
    val averageRating: Float,
    val totalRatings: Int,
    val lastUpdated: Instant
)
```

### 4. Listing Creation

#### Form Fields
- **Game Selection**: Searchable dropdown
- **Device**: User's registered devices
- **Performance Rating**: 1-5 stars with categories
- **Configuration**: GPU driver, settings presets
- **Screenshots**: Optional performance captures
- **Description**: User notes and tips

#### Validation Rules
```kotlin
data class CreateListingForm(
    val gameId: String = "",
    val deviceId: String = "",
    val performanceRating: Int = 0,
    val playabilityRating: Int = 0,
    val gpuDriver: String = "",
    val configurationPreset: String = "",
    val customSettings: String = "",
    val screenshots: List<Uri> = emptyList(),
    val description: String = "",
    val isPublic: Boolean = true
) {
    fun isValid(): Boolean = listOf(
        gameId.isNotEmpty(),
        deviceId.isNotEmpty(),
        performanceRating in 1..5,
        playabilityRating in 1..5,
        description.length >= 10
    ).all { it }
}
```

### 5. Device Management

#### Device Registration
- **Automatic Detection**: Hardware specs and model identification
- **Manual Entry**: Custom device configurations
- **Performance Baseline**: Benchmark testing
- **Driver Management**: GPU driver installation and updates

#### Device Data Model
```kotlin
data class Device(
    val id: String,
    val name: String,
    val manufacturer: String,
    val model: String,
    val chipset: String,
    val gpu: String,
    val ramSize: Int,
    val storageSize: Int,
    val screenSize: Float,
    val screenResolution: String,
    val androidVersion: String,
    val isVerified: Boolean,
    val benchmarkScore: Int?,
    val registeredAt: Instant
)
```

### 6. Eden Emulator Integration

#### Core Functionality
- **Installation Check**: Verify Eden emulator presence
- **Configuration Management**: Preset and custom settings
- **Direct Launch**: Intent-based game launching
- **Performance Monitoring**: FPS and stability tracking

#### Intent Implementation
```kotlin
class EdenEmulatorService @Inject constructor() {
    
    suspend fun launchGame(
        titleId: String,
        configuration: EmulatorConfiguration
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val intent = Intent().apply {
                action = "dev.eden.eden_emulator.LAUNCH_WITH_CUSTOM_CONFIG"
                setPackage("dev.eden.eden_emulator")
                putExtra("title_id", titleId)
                putExtra("custom_settings", configuration.toINIString())
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            context.startActivity(intent)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(EmulatorLaunchException("Failed to launch Eden emulator", e))
        }
    }
    
    private fun EmulatorConfiguration.toINIString(): String {
        return buildString {
            appendLine("[Controls]")
            appendLine("vibration_enabled\\\\use_global=${controls.vibrationEnabled}")
            appendLine("motion_enabled\\\\use_global=${controls.motionEnabled}")
            appendLine()
            appendLine("[Renderer]")
            appendLine("use_vsync\\\\use_global=false")
            appendLine("use_vsync=0")
            appendLine("use_asynchronous_shaders\\\\use_global=false")
            appendLine("use_asynchronous_shaders=true")
            // ... additional configuration sections
        }
    }
}
```

### 7. User Profile & Settings

#### Profile Features
- **Account Information**: Name, avatar, member since
- **Statistics**: Games tested, listings created, community score
- **Device Library**: Registered gaming handhelds
- **Preferences**: Theme, notifications, privacy settings

#### Settings Categories
```kotlin
sealed class SettingsCategory {
    object Account : SettingsCategory()
    object Devices : SettingsCategory()
    object Notifications : SettingsCategory()
    object Appearance : SettingsCategory()
    object Privacy : SettingsCategory()
    object About : SettingsCategory()
}
```

---

## Data Models & API Integration

### Core Entities

#### Game Entity
```kotlin
@Entity(tableName = "games")
data class Game(
    @PrimaryKey val id: String,
    val title: String,
    val titleId: String,
    val coverImageUrl: String,
    val developer: String,
    val publisher: String,
    val releaseDate: String,
    val genres: List<String>,
    val averageCompatibility: Float,
    val totalListings: Int,
    val lastUpdated: Long,
    val isFavorite: Boolean = false
)
```

#### Game Listing Entity
```kotlin
@Entity(tableName = "game_listings")
data class GameListing(
    @PrimaryKey val id: String,
    val gameId: String,
    val userId: String,
    val deviceId: String,
    val performanceRating: Int,
    val playabilityRating: Int,
    val gpuDriver: String,
    val configurationPreset: String,
    val customSettings: String,
    val description: String,
    val screenshotUrls: List<String>,
    val isVerified: Boolean,
    val likes: Int,
    val createdAt: Long,
    val updatedAt: Long
)
```

#### User Entity
```kotlin
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val totalListings: Int,
    val totalLikes: Int,
    val memberSince: Long,
    val isVerified: Boolean,
    val lastActive: Long
)
```

### API Service Interfaces

#### Game API
```kotlin
interface GameApiService {
    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("search") search: String? = null,
        @Query("genre") genre: String? = null,
        @Query("sort") sort: String = "popularity"
    ): Response<PaginatedResponse<Game>>
    
    @GET("games/{id}")
    suspend fun getGameDetail(@Path("id") gameId: String): Response<GameDetail>
    
    @GET("games/{id}/listings")
    suspend fun getGameListings(
        @Path("id") gameId: String,
        @Query("device") deviceFilter: String? = null
    ): Response<List<GameListing>>
}
```

#### Listing API
```kotlin
interface ListingApiService {
    @POST("listings")
    suspend fun createListing(@Body listing: CreateListingRequest): Response<GameListing>
    
    @PUT("listings/{id}")
    suspend fun updateListing(
        @Path("id") listingId: String,
        @Body listing: UpdateListingRequest
    ): Response<GameListing>
    
    @DELETE("listings/{id}")
    suspend fun deleteListing(@Path("id") listingId: String): Response<Unit>
    
    @POST("listings/{id}/like")
    suspend fun likeListing(@Path("id") listingId: String): Response<Unit>
}
```

### Repository Implementation

#### Game Repository
```kotlin
@Singleton
class GameRepository @Inject constructor(
    private val gameApiService: GameApiService,
    private val gameDao: GameDao,
    private val networkMonitor: NetworkMonitor
) {
    fun getGames(
        page: Int,
        search: String? = null,
        genre: String? = null
    ): Flow<PagingData<Game>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { GamePagingSource(gameApiService, search, genre) }
    ).flow.cachedIn(viewModelScope)
    
    suspend fun getGameDetail(gameId: String): Result<GameDetail> = try {
        val response = gameApiService.getGameDetail(gameId)
        if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            Result.failure(ApiException("Failed to fetch game details"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

---

## Authentication & Security

### Clerk Integration

#### Authentication Setup
```kotlin
@HiltAndroidApp
class EmuReadyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Clerk
        Clerk.initialize(
            context = this,
            publishableKey = BuildConfig.CLERK_PUBLISHABLE_KEY,
            frontendApi = BuildConfig.CLERK_FRONTEND_API
        )
    }
}
```

#### Auth State Management
```kotlin
@Singleton
class AuthRepository @Inject constructor(
    private val clerk: Clerk,
    private val dataStore: DataStore<Preferences>
) {
    val authState: Flow<AuthState> = clerk.user.map { user ->
        when {
            user != null -> AuthState.Authenticated(user)
            else -> AuthState.Unauthenticated
        }
    }
    
    suspend fun signIn(email: String, password: String): Result<Unit> = try {
        clerk.client.signIn.create(
            identifier = email,
            password = password
        )
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(AuthException("Sign in failed", e))
    }
}
```

### Security Measures

#### API Token Management
```kotlin
@Singleton
class TokenManager @Inject constructor(
    private val encryptedPrefs: EncryptedSharedPreferences,
    private val clerk: Clerk
) {
    suspend fun getAuthToken(): String? = withContext(Dispatchers.IO) {
        clerk.session?.getToken()?.jwt
    }
    
    suspend fun refreshToken(): Result<String> = try {
        val token = clerk.session?.getToken()?.jwt
        if (token != null) {
            Result.success(token)
        } else {
            Result.failure(AuthException("Failed to refresh token"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

#### Network Security
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenManager: TokenManager
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenManager))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        })
        .certificatePinner(
            CertificatePinner.Builder()
                .add("api.emuready.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
                .build()
        )
        .build()
}
```

---

## Storage & Data Management

### Room Database Schema

#### Database Configuration
```kotlin
@Database(
    entities = [
        Game::class,
        GameListing::class,
        User::class,
        Device::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class EmuReadyDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun listingDao(): ListingDao
    abstract fun userDao(): UserDao
    abstract fun deviceDao(): DeviceDao
    abstract fun notificationDao(): NotificationDao
}
```

#### Data Access Objects
```kotlin
@Dao
interface GameDao {
    @Query("SELECT * FROM games WHERE title LIKE '%' || :search || '%' ORDER BY title ASC")
    fun searchGames(search: String): Flow<List<Game>>
    
    @Query("SELECT * FROM games WHERE id = :gameId")
    suspend fun getGameById(gameId: String): Game?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<Game>)
    
    @Update
    suspend fun updateGame(game: Game)
    
    @Query("UPDATE games SET isFavorite = :isFavorite WHERE id = :gameId")
    suspend fun updateFavoriteStatus(gameId: String, isFavorite: Boolean)
}
```

### DataStore Configuration

#### Preferences DataStore
```kotlin
@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val AUTO_LAUNCH_EMULATOR = booleanPreferencesKey("auto_launch_emulator")
    }
    
    val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserPreferences(
                themeMode = ThemeMode.valueOf(
                    preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
                ),
                language = preferences[PreferencesKeys.LANGUAGE] ?: "en",
                notificationsEnabled = preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true,
                autoLaunchEmulator = preferences[PreferencesKeys.AUTO_LAUNCH_EMULATOR] ?: false
            )
        }
}
```

---

## Eden Emulator Integration

### Intent System Architecture

#### Launch Configuration
```kotlin
data class EmulatorConfiguration(
    val controls: ControlsConfig = ControlsConfig(),
    val core: CoreConfig = CoreConfig(),
    val renderer: RendererConfig = RendererConfig(),
    val audio: AudioConfig = AudioConfig(),
    val system: SystemConfig = SystemConfig(),
    val gpuDriver: GpuDriverConfig? = null
) {
    fun toINIFormat(): String = buildString {
        appendLine("[Controls]")
        appendLine("vibration_enabled\\\\use_global=${controls.vibrationEnabled}")
        appendLine("enable_accurate_vibrations\\\\use_global=${controls.accurateVibrations}")
        appendLine("motion_enabled\\\\use_global=${controls.motionEnabled}")
        appendLine()
        
        appendLine("[Core]")
        appendLine("use_multi_core\\\\use_global=${core.useMultiCore}")
        appendLine("memory_layout_mode\\\\use_global=${core.memoryLayoutMode}")
        appendLine("use_speed_limit\\\\use_global=${core.useSpeedLimit}")
        appendLine()
        
        appendLine("[Renderer]")
        appendLine("backend\\\\use_global=${renderer.backend}")
        appendLine("shader_backend\\\\use_global=${renderer.shaderBackend}")
        appendLine("use_vsync\\\\use_global=false")
        appendLine("use_vsync\\\\default=false")
        appendLine("use_vsync=${if (renderer.useVsync) 1 else 0}")
        appendLine("use_asynchronous_shaders\\\\use_global=false")
        appendLine("use_asynchronous_shaders\\\\default=false")
        appendLine("use_asynchronous_shaders=${renderer.useAsyncShaders}")
        appendLine()
        
        appendLine("[Audio]")
        appendLine("output_engine\\\\use_global=${audio.outputEngine}")
        appendLine("volume\\\\use_global=${audio.volume}")
        appendLine()
        
        appendLine("[System]")
        appendLine("use_docked_mode\\\\use_global=${system.useDockedMode}")
        appendLine("language_index\\\\use_global=${system.languageIndex}")
        appendLine()
        
        gpuDriver?.let { driver ->
            appendLine("[GpuDriver]")
            appendLine("driver_path\\\\use_global=false")
            appendLine("driver_path\\\\default=false")
            appendLine("driver_path=${driver.driverPath}")
        }
    }
}
```

#### Configuration Presets
```kotlin
object EmulatorPresets {
    val KNOWN_WORKING_CONFIG = EmulatorConfiguration(
        controls = ControlsConfig(
            vibrationEnabled = true,
            accurateVibrations = true,
            motionEnabled = true
        ),
        core = CoreConfig(
            useMultiCore = true,
            memoryLayoutMode = true,
            useSpeedLimit = true
        ),
        renderer = RendererConfig(
            backend = true,
            shaderBackend = true,
            useVsync = false,
            useAsyncShaders = true,
            gpuAccuracy = true,
            useReactiveFlushing = true,
            useFastGpuTime = true
        ),
        audio = AudioConfig(
            outputEngine = true,
            volume = true
        ),
        system = SystemConfig(
            useDockedMode = true,
            languageIndex = true
        )
    )
    
    val HIGH_PERFORMANCE = EmulatorConfiguration(
        core = CoreConfig(
            useMultiCore = true,
            cpuAccuracy = 0 // Fastest
        ),
        renderer = RendererConfig(
            useVsync = false,
            useAsyncShaders = true,
            gpuAccuracy = 0 // High speed
        ),
        system = SystemConfig(
            useDockedMode = true // Better performance
        )
    )
    
    val BATTERY_OPTIMIZED = EmulatorConfiguration(
        core = CoreConfig(
            useSpeedLimit = true,
            speedLimit = 50 // Limit to 50% speed
        ),
        renderer = RendererConfig(
            useVsync = true,
            resolutionSetup = 2 // Lower resolution
        ),
        system = SystemConfig(
            useDockedMode = false // Handheld mode
        )
    )
}
```

#### Launch Service Implementation
```kotlin
@Singleton
class EdenLaunchService @Inject constructor(
    private val context: Context,
    private val packageManager: PackageManager
) {
    companion object {
        private const val EDEN_PACKAGE = "dev.eden.eden_emulator"
        private const val EDEN_LAUNCH_ACTION = "dev.eden.eden_emulator.LAUNCH_WITH_CUSTOM_CONFIG"
        private const val EMULATION_ACTIVITY = "org.yuzu.yuzu_emu.activities.EmulationActivity"
    }
    
    suspend fun isEdenInstalled(): Boolean = withContext(Dispatchers.IO) {
        try {
            packageManager.getPackageInfo(EDEN_PACKAGE, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
    
    suspend fun launchGame(
        titleId: String,
        configuration: EmulatorConfiguration
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (!isEdenInstalled()) {
                return@withContext Result.failure(
                    EmulatorException("Eden emulator is not installed")
                )
            }
            
            val intent = Intent().apply {
                action = EDEN_LAUNCH_ACTION
                setPackage(EDEN_PACKAGE)
                setClassName(EDEN_PACKAGE, EMULATION_ACTIVITY)
                putExtra("title_id", titleId)
                putExtra("custom_settings", configuration.toINIFormat())
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            // Verify intent can be resolved
            val resolveInfo = packageManager.resolveActivity(intent, 0)
            if (resolveInfo == null) {
                return@withContext Result.failure(
                    EmulatorException("Eden emulator does not support custom configuration")
                )
            }
            
            context.startActivity(intent)
            Result.success(Unit)
            
        } catch (e: ActivityNotFoundException) {
            Result.failure(EmulatorException("Failed to launch Eden emulator", e))
        } catch (e: Exception) {
            Result.failure(EmulatorException("Unexpected error launching emulator", e))
        }
    }
    
    suspend fun launchGameWithPreset(
        titleId: String,
        presetName: String
    ): Result<Unit> {
        val configuration = when (presetName) {
            "Known Working Config" -> EmulatorPresets.KNOWN_WORKING_CONFIG
            "High Performance" -> EmulatorPresets.HIGH_PERFORMANCE
            "Battery Optimized" -> EmulatorPresets.BATTERY_OPTIMIZED
            "Balanced" -> EmulatorPresets.BALANCED
            else -> return Result.failure(
                EmulatorException("Unknown preset: $presetName")
            )
        }
        
        return launchGame(titleId, configuration)
    }
}
```

### Emulator Test Screen

#### Screen Implementation
```kotlin
@Composable
fun EmulatorTestScreen(
    viewModel: EmulatorTestViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Test Eden Emulator") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                EmulatorStatusCard(
                    isInstalled = state.isEdenInstalled,
                    onCheckInstallation = viewModel::checkInstallation
                )
            }
            
            item {
                GameConfigurationCard(
                    titleId = state.titleId,
                    onTitleIdChange = viewModel::updateTitleId,
                    selectedPreset = state.selectedPreset,
                    onPresetChange = viewModel::selectPreset
                )
            }
            
            if (state.showAdvancedOptions) {
                item {
                    AdvancedOptionsCard(
                        configuration = state.customConfiguration,
                        onConfigurationChange = viewModel::updateConfiguration
                    )
                }
            }
            
            item {
                LaunchButtonsCard(
                    isLoading = state.isLaunching,
                    onLaunch = { viewModel.launchGame() },
                    onShowAdvanced = { viewModel.toggleAdvancedOptions() }
                )
            }
            
            if (state.testResults.isNotEmpty()) {
                item {
                    TestResultsCard(results = state.testResults)
                }
            }
        }
    }
    
    // Handle launch results
    LaunchedEffect(state.launchResult) {
        state.launchResult?.let { result ->
            result.onFailure { error ->
                // Show error dialog
            }.onSuccess {
                // Show success message
            }
            viewModel.clearLaunchResult()
        }
    }
}
```

---

## Performance & Optimization

### Memory Management

#### Image Loading Strategy
```kotlin
@Singleton
class ImageLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val coilImageLoader = ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25) // Use 25% of available memory
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizeBytes(100 * 1024 * 1024) // 100MB
                .build()
        }
        .build()
    
    fun loadImage(url: String): ImageRequest = ImageRequest.Builder(context)
        .data(url)
        .crossfade(true)
        .placeholder(R.drawable.game_placeholder)
        .error(R.drawable.game_error)
        .build()
}
```

#### Database Optimization
```kotlin
@Database(
    entities = [Game::class, GameListing::class, User::class, Device::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class EmuReadyDatabase : RoomDatabase() {
    
    companion object {
        @Volatile
        private var INSTANCE: EmuReadyDatabase? = null
        
        fun getDatabase(context: Context): EmuReadyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmuReadyDatabase::class.java,
                    "emuready_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Create indexes for better query performance
                        db.execSQL("CREATE INDEX IF NOT EXISTS index_games_title ON games(title)")
                        db.execSQL("CREATE INDEX IF NOT EXISTS index_listings_game_id ON game_listings(gameId)")
                        db.execSQL("CREATE INDEX IF NOT EXISTS index_listings_device_id ON game_listings(deviceId)")
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

### Network Optimization

#### Caching Strategy
```kotlin
@Singleton
class CacheManager @Inject constructor(
    private val context: Context
) {
    private val cache = Cache(
        directory = File(context.cacheDir, "http_cache"),
        maxSize = 50L * 1024L * 1024L // 50 MB
    )
    
    fun provideCacheInterceptor(): Interceptor = Interceptor { chain ->
        val request = chain.request()
        val cacheControl = CacheControl.Builder()
            .maxAge(5, TimeUnit.MINUTES) // Cache for 5 minutes
            .build()
        
        val newRequest = request.newBuilder()
            .cacheControl(cacheControl)
            .build()
        
        chain.proceed(newRequest)
    }
}
```

#### Background Sync
```kotlin
@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val gameRepository: GameRepository,
    private val listingRepository: ListingRepository
) : CoroutineWorker(context, workerParams) {
    
    override suspend fun doWork(): Result = try {
        // Sync games data
        gameRepository.syncGamesFromRemote()
        
        // Sync user listings
        listingRepository.syncUserListings()
        
        Result.success()
    } catch (e: Exception) {
        if (runAttemptCount < 3) {
            Result.retry()
        } else {
            Result.failure()
        }
    }
    
    @AssistedFactory
    interface Factory {
        fun create(context: Context, params: WorkerParameters): DataSyncWorker
    }
}
```

---

## Build & Distribution

### Build Configuration

#### Gradle Setup (app/build.gradle.kts)
```kotlin
android {
    namespace = "com.emuready.emuready"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.emuready.emuready"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        
        buildConfigField("String", "API_BASE_URL", "\"https://api.emuready.com/\"")
        buildConfigField("String", "CLERK_PUBLISHABLE_KEY", "\"${project.findProperty("CLERK_PUBLISHABLE_KEY")}\"")
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        )
    }
    
    buildFeatures {
        compose = true
        buildConfig = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")
    
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    kapt("com.google.dagger:hilt-compiler:2.48")
    
    // Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // Image Loading
    implementation("io.coil-kt:coil-compose:2.5.0")
    
    // Paging
    implementation("androidx.paging:paging-runtime:3.2.1")
    implementation("androidx.paging:paging-compose:3.2.1")
    
    // Work Manager
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.hilt:hilt-work:1.1.0")
    
    // Clerk Authentication
    implementation("com.clerk:clerk-android:1.0.0")
    
    // Security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.biometric:biometric:1.1.0")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

### ProGuard Configuration

#### proguard-rules.pro
```proguard
# Keep Retrofit interfaces
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Keep Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.emuready.emuready.**$$serializer { *; }
-keepclassmembers class com.emuready.emuready.** {
    *** Companion;
}

# Keep Room entities
-keep class com.emuready.emuready.data.local.entities.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel

# Keep Clerk classes
-keep class com.clerk.** { *; }
```

### Continuous Integration

#### GitHub Actions Workflow
```yaml
name: Android CI

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Cache Gradle packages
      uses: actions/cache@v3
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
        restore-keys: |
          ${{ runner.os }}-gradle-
          
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Run tests
      run: ./gradlew test
      
    - name: Run lint
      run: ./gradlew lint
      
  build:
    needs: test
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Build debug APK
      run: ./gradlew assembleDebug
      
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: debug-apk
        path: app/build/outputs/apk/debug/app-debug.apk
```

---

## Development Guidelines

### Code Style

#### Kotlin Style Guide
```kotlin
// Naming conventions
class GameRepository          // PascalCase for classes
fun getUserById()            // camelCase for functions
val userName: String         // camelCase for properties
const val MAX_RETRY_COUNT    // SCREAMING_SNAKE_CASE for constants

// Function structure
suspend fun fetchGameDetails(
    gameId: String,
    includeListings: Boolean = false
): Result<GameDetail> = withContext(Dispatchers.IO) {
    try {
        val response = apiService.getGameDetail(gameId)
        Result.success(response.body()!!)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

// Data class structure
data class Game(
    val id: String,
    val title: String,
    val coverImageUrl: String,
    val releaseDate: LocalDate,
    val averageRating: Float = 0f,
    val isBookmarked: Boolean = false
) {
    companion object {
        fun empty() = Game(
            id = "",
            title = "",
            coverImageUrl = "",
            releaseDate = LocalDate.now(),
            averageRating = 0f,
            isBookmarked = false
        )
    }
}
```

### Testing Strategy

#### Unit Tests
```kotlin
@ExtendWith(MockitoExtension::class)
class GameRepositoryTest {
    
    @Mock
    private lateinit var apiService: GameApiService
    
    @Mock
    private lateinit var gameDao: GameDao
    
    private lateinit var repository: GameRepository
    
    @BeforeEach
    fun setup() {
        repository = GameRepository(apiService, gameDao)
    }
    
    @Test
    fun `getGameDetail returns success when API call succeeds`() = runTest {
        // Given
        val gameId = "test-game-id"
        val expectedGame = GameDetail(id = gameId, title = "Test Game")
        whenever(apiService.getGameDetail(gameId)).thenReturn(
            Response.success(expectedGame)
        )
        
        // When
        val result = repository.getGameDetail(gameId)
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedGame, result.getOrNull())
    }
}
```

#### UI Tests
```kotlin
@HiltAndroidTest
class GameDetailScreenTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
    
    @Test
    fun displayGameDetailsCorrectly() {
        val testGame = GameDetail(
            id = "1",
            title = "Test Game",
            description = "Test Description"
        )
        
        composeTestRule.setContent {
            GameDetailScreen(
                gameDetail = testGame,
                onBackClick = {},
                onLaunchGame = {}
            )
        }
        
        composeTestRule.onNodeWithText("Test Game").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Description").assertIsDisplayed()
    }
}
```

### Error Handling

#### Exception Hierarchy
```kotlin
sealed class EmuReadyException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

class NetworkException(
    message: String,
    cause: Throwable? = null
) : EmuReadyException(message, cause)

class AuthException(
    message: String,
    cause: Throwable? = null
) : EmuReadyException(message, cause)

class EmulatorException(
    message: String,
    cause: Throwable? = null
) : EmuReadyException(message, cause)

class ValidationException(
    message: String,
    cause: Throwable? = null
) : EmuReadyException(message, cause)
```

#### Global Error Handler
```kotlin
@Singleton
class GlobalErrorHandler @Inject constructor() {
    
    fun handleError(error: Throwable): String = when (error) {
        is NetworkException -> "Network connection failed. Please check your internet connection."
        is AuthException -> "Authentication failed. Please sign in again."
        is EmulatorException -> "Emulator launch failed. ${error.message}"
        is ValidationException -> "Invalid input. ${error.message}"
        else -> "An unexpected error occurred. Please try again."
    }
}
```

### Documentation Standards

#### Code Documentation
```kotlin
/**
 * Repository for managing game data from remote API and local database.
 * 
 * This repository implements the single source of truth pattern, where:
 * - Data is fetched from remote API when needed
 * - Results are cached in local database
 * - UI observes local database for consistent state
 * 
 * @param apiService Remote API service for game data
 * @param gameDao Local database access object
 * @param networkMonitor Network connectivity monitor
 */
@Singleton
class GameRepository @Inject constructor(
    private val apiService: GameApiService,
    private val gameDao: GameDao,
    private val networkMonitor: NetworkMonitor
) {
    
    /**
     * Fetches paginated list of games with optional search and filtering.
     * 
     * @param search Optional search query to filter games by title
     * @param genre Optional genre filter
     * @param sortBy Sorting criteria (title, rating, date)
     * @return Flow of PagingData for efficient list loading
     */
    fun getGames(
        search: String? = null,
        genre: String? = null,
        sortBy: GameSortOption = GameSortOption.TITLE
    ): Flow<PagingData<Game>> = // Implementation...
}
```

---

## Conclusion

This specification provides a comprehensive blueprint for building the EmuReady mobile app as a native Android application. The architecture prioritizes:

1. **Performance**: Native Android components for optimal speed
2. **Maintainability**: Clean architecture with clear separation of concerns
3. **Scalability**: Modular design supporting future feature additions
4. **User Experience**: Smooth animations and responsive design
5. **Reliability**: Robust error handling and offline capabilities

### Key Implementation Priorities

1. **Core Features**: Implement game browsing, listing creation, and user management first
2. **Eden Integration**: Focus on reliable emulator launching with multiple fallback methods
3. **Performance**: Optimize image loading, database queries, and UI rendering
4. **Testing**: Comprehensive unit and integration tests for all critical paths
5. **Security**: Secure authentication, API communication, and data storage

### Development Timeline

- **Phase 1 (Weeks 1-4)**: Core architecture, authentication, and basic UI
- **Phase 2 (Weeks 5-8)**: Game browsing, search, and listing features
- **Phase 3 (Weeks 9-12)**: Eden emulator integration and device management
- **Phase 4 (Weeks 13-16)**: Performance optimization, testing, and polish

This specification serves as a complete guide for any development team to recreate the EmuReady app with improved performance, reliability, and maintainability compared to the original React Native implementation.