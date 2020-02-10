# SpinApp
The Wheely Cool App

The App only has two screens; one that allows a user to add values to “The Wheel”, and one
where the Wheel can be spun for a random result. It should only take a few hours of your time, but
feel free to do less or more depending on what you’re comfortable with and your skill level.

# Application Requirements
### Screen A
The initial screen of the app should be the input screen. Here you should be able to add the
values for the various options which will appear on “The Wheel” and navigate to the next screen.
Ideally these inputs should persist between application launches.
### Screen B
The main screen of the app should showcase “The Wheel”. The options entered by the user in
Screen A should appear evenly distributed in some form. Clicking the “Spin” button should cause
a rotation animation of “The Wheel” and finally settle on a random option.

# Installation
Clone the repo and install the dependencies.
          
      git clone git@github.com:maquadir/SpinApp.git
      
# Architecture and Design
The application follows an MVVM architecture as given below

<img width="330" alt="architecture" src="https://user-images.githubusercontent.com/19331629/70860286-0d6aec80-1f74-11ea-9ce8-dd78a41b8238.png">

# Setup Screen A
We add the values for the various options which will appear on “The Wheel” and display them in a list.Provide a button to navigate to the next screen.

<img width="350" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/74199412-2d8b0380-4cb8-11ea-82ea-75e3cf8019e0.png"> <img width="350" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/74199416-324fb780-4cb8-11ea-9fd0-4ac63c05cc72.png">

### Data Binding
The Data Binding Library is an Android Jetpack library that allows you to bind UI components in your XML layouts to data sources in your app using a declarative format rather than programmatically.All the UIView elements in the layout are binded to views through data binding.

### Setup databinding

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        
### Construct repository to handle data operations with database and dao
        
        itemdao = ItemDatabase.getDatabase(application).getItemDao()
        val repository = ItemRepository(itemdao)

### Setup view model
        
        factory =
            ItemViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, factory).get(ItemViewModel::class.java)
        
### Bind layout to viewmodel

        binding.item = viewModel


### Model
A Modelcontains all the data classes, database classes, and repository.
An Item data class .A repository takes care of how data will be fetched from the database.

      @Entity(tableName = "item_table")
      
      data class Item(
      
          @ColumnInfo(name = "item")
          val item: String
      
      ){
          @PrimaryKey(autoGenerate = true)
          var id : Int = 0
      }
      
       class ItemRepository(
          private val itemdao: Itemdao
      ) {
      
          fun insertItems(item: Item) =  itemdao.insert(item)
      
          fun getItemsDb() =  itemdao.getAll()
      
          fun getCount() =  itemdao.getRowCount()
      
      
      }
      
### Room Database

The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
Basically, with the help of room we can quickly create sqlite databases and perform the operations like create, read, update and delete. Room makes everything very easy and quick.
We have the Data Access Object(DAO) which has all database related functions to insert,fetch,delete and update in the Room Database

### Components of Room
We have 3 components of room.

- Entity: Instead of creating the SQLite table, we will create the Entity. Entity is nothing but a model class annotated with @Entity. The variables of this class is our columns, and the class is our table.

      @Entity(tableName = "item_table")
      
      data class Item(
      
          @ColumnInfo(name = "item")
          val item: String
      
      ){
          @PrimaryKey(autoGenerate = true)
          var id : Int = 0
      }
      
- Database: It is an abstract class where we define all our entities.


       @Database(entities = [Headline::class], version = 2, exportSchema = false)
       abstract class HeadlineDatabase:RoomDatabase() {
      
          abstract fun getHeadlineDao(): Headlinedao
      
          companion object{
      
              @Volatile
              private var INSTANCE: HeadlineDatabase? = null
      
      
              fun getDatabase(context: Context): HeadlineDatabase {
                  val tempInstance = INSTANCE
                  if (tempInstance != null) {
                      return tempInstance
                  }
      
                  synchronized(this) {
      
                      val instance = Room.databaseBuilder(
                              context.applicationContext,
                              HeadlineDatabase::class.java,
                              "headline_database"
                          )
                              .fallbackToDestructiveMigration()
                              .build()
                          INSTANCE = instance
      
                      return instance
                  }
              }
          }
          
 - DAO: Stands for Data Access Object. It is an interface that defines all the operations that we need to perform in our database.
 
                  @Dao
                  interface Itemdao {
                  
                      @Query("SELECT * FROM item_table")
                      fun getAll(): List<Item>
                  
                      @Insert
                      fun insert(item:Item)
                  
                      @Query("SELECT COUNT(item) FROM item_table")
                      fun getRowCount(): Int
                  
                  
                  }
                  
### Coroutines
Coroutines are a great way to write asynchronous code that is perfectly readable and maintainable. We use it to perform room database operations.

           //coroutines with a callback parameter
    fun<T: Any> ioThenMain(work: suspend (() -> T?), callback: ((T?)->Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async  rt@{
                return@rt work()
            }.await()
            callback(data)
        }

    //coroutines with a single parameter used during inserting data
    fun<T: Any> ioThenMain(work: suspend (() -> T?)) =
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async  rt@{
                return@rt work()
            }.await()

        }
        
    //couroutines job to insert data into Room Database
    job =

            viewModelScope.launch {
                Coroutines.ioThenMain(
                    { repository.insertItems(item) }
                )
            }
            
    //coroutines job to get data from Room database     
      job =

            viewModelScope.launch {
                Coroutines.ioThenMain(
                    { repository.getItemsDb() },
                    { _itemsfetch.value =  it }
                )
            }
            
     // observe data fetch from Room database and display via list view
        viewModel.itemsfetch.observe(this, Observer { items ->

            itemArray.clear()
            if(items != null) {

                for (i in 0 until items.size) {
                    Log.i("Items", items[i].item)
                    itemArray.add(items[i].item)

                }
                val adapter = ArrayAdapter(this,
                    R.layout.list_view_item, itemArray)

                val listView:ListView = findViewById(R.id.list_view)
                listView.adapter = adapter
            }

        })
        
# Setup Screen B

This screen of the app showcases “The Spinning Wheel”. The options entered by the user in
Screen A should appear evenly distributed in some form. Clicking the “Spin” button should cause
a rotation animation of “The Wheel” and finally settle on a random option.

<img width="350" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/74199423-37ad0200-4cb8-11ea-976d-a111412232aa.png"> <img width="350" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/74199427-3aa7f280-4cb8-11ea-9f43-5673be15db5a.png">    

### Spinning Wheel
The custom spinning wheel has been utilized from Ade Fruandta(https://github.com/adef145/SpinningWheelAndroid)

        val wheelView: SpinningWheelView = findViewById<SpinningWheelView>(R.id.wheel)

        // add array of items to wheerlview
        wheelView.items = itemArray

        // Set listener for rotation event
        wheelView.onRotationListener = object : SpinningWheelView.OnRotationListener<String?> {
            // Call once when start rotation
            override fun onRotation() {
                Log.i("Rotation", "On Rotation")
            }

            // Call once when stop rotation
            override fun onStopRotation(item: String?) {
                Log.i("Rotation", item.toString())
                displayToast(item.toString())

            }
        }

        // If true: user can rotate by touch
        // If false: user can not rotate by touch
        wheelView.isEnabled = false
        
        //button click to rotate wheel
        findViewById<Button>(R.id.spinButton).setOnClickListener{
            wheelView.rotate(50f, 3000, 50)
        }

### Build Gradle
We declare the respective dependencies 

    def room_version = "2.2.3"

    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.core:core-ktx:1.2.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'

    //recycler view and card view
    implementation 'androidx.recyclerview:recyclerview:1.1.0'

    // ViewModel and LiveData
    implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"

    //New Material Design
    implementation 'com.google.android.material:material:1.2.0-alpha04'

    //Room and lifecycle dependencies
    kapt "androidx.room:room-compiler:$room_version"
    implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"

# Generating signed APK
From Android Studio:

- Build menu
- Generate Signed APK...

# Support
- Stack Overflow
- Google
- Spinning Wheel - https://github.com/adef145/SpinningWheelAndroid

