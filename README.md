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

<img width="449" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/72322705-20034d80-36fb-11ea-8936-7091e6cd1a70.png">

# Setup
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
      
      

