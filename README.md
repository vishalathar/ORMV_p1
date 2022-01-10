# ormv_p1

## Project Description
A java based ORM for simplifying connecting to and from an SQL database without the need for SQL or connection management. 

## Technologies Used

* PostgreSQL - version 42.2.12  
* Java - version 8.0  
* Apache commons - version 2.1  
* JUnit
* Log4j

## Features

List of features ready and TODOs for future development  
* Easy to use and straightforward user API.  
* No need for SQL, HQL, or any databse specific language.  
* Straightforward and simple Annotation based for ease of use. 
* etc...

To-do list: [`for future iterations`]
* Mapping of join columns inside of entities.    
* Implement of aggregate functions.  
* Implement insertion of foreign key attributes
* Implement @manytomany, @onetomany and @manytoone anotations 
* Implement getAll(), saveAll(), saveOrUpdate() functions
* Most Importantly, Implement Transactions and Light-Weight Sessions

## Getting Started  
Currently project must be included as local dependency. to do so:
```shell
  git clone https://github.com/vishalathar/ormv_p1
  cd ormv_p1
  mvn install
```
Next, place the following inside your project pom.xml file:
```XML
  <dependency>
    <groupId>com.revature</groupId>
    <artifactId>MYORM</artifactId>
    <version>1.0-SNAPSHOT</version>
  </dependency>

```

Finally, inside your project structure you need a ormv.cfg.proprties file. 
 (located src/main/resources/)
 ``` 
  url=path/to/database
  username=username/of/database
  password=password/of/database  
  ```
  
## Usage  
  ### Annotating classes  
  All classes which represent objects in database must be annotated.
   - #### @Table(tableName = "table_name")  
      - Indicates that this class is associated with table 'table_name'  
   - #### @Column(columnName = "column_name")  
      - Indicates that the Annotated field is a column in the table with the name 'column_name'
   - #### @PrimaryKey(columnName = "column_name", strategy="GenerationType.IDENTITY") 
      - Indicates that the annotated field is the primary key for the table.
      - Strategy indicates that it is a SERIAL TYPE primary key
      - Currently only SERIAL TYPE is supported
   - #### @JoinColumn(columnName = "column_name") 
      - Indicates that the annotated field is the foreign key for the table

  ### User API  
  - To work with the API you need to make an instance of Configuration() as
    Configuration cfg = new Configuration();
  - Then use the method 'public Configuration<T> addAnnotatedClass(Class<?> annotatedClass)' to add annotated classes. Make sure you add the independent classes first,
  and then go for dependent classes. e.g
    cfg.addAnnotatedClass(DemoClass.class);
  - Then call the method of Configuration class public void 'showReflectionMagic()', that as the name explains will show #REFLECTION MAGIC.
  This method Create table queries, get Connection from Connection Pool and Create Database of annotated Classes.
  - To use the further methods to perform CRUD operations on DB Objects. Create an instance of ORMV_Session, passing the Configuration object as
    ORMV_Session ses = new ORMV_Session(cfg);
    It acts as an abstraction over EntityDao Layer. Also, will include features of transaction and session handling in future.
  - For Crud opertaions make use of following methods:
  
  - ### 'public Object save(Object Obj)'
      - Add the Object in the respective table in DB, returning the primary key generated.
  - ### 'public void update(Object Obj);'
      - Updates the Object using id in the respective table in DB.
  - ### 'public void delete(Object Obj)'
      - Deletes the Object in the respective table using id in DB.
  - ### 'public void truncate(Class<?> clazz)'
      - Clears the whole data in the respective table in DB.
  - ### 'public Object get(Class<?> clazz, Object id)'
      - Returns the Object in the respective table in DB represented by id.


## License

This project uses the following license: [GNU Public License 3.0](https://www.gnu.org/licenses/gpl-3.0.en.html).
