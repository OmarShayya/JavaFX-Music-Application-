# JavaFX-Music-Application-
OSTunes is a music application built using JavaFX 
This music application is similar to spotify where a user can login, sign up, logout, play any available song, add song to likes, upload a song if he has a premium version, and logout.

to run the app on your computer, you need to create and connect to a database called ostunes on mysql workbench that contains the following tables:

1. songs: it has the following columns: 
    -song_id int Auto increment and primary key
    -song_name varchar(255)
    -artist_name varchar(255)
    -url varchar(255)
    -song_image varchar(500)
 
2. user: it has the following columns: 
    -ID int AI pk
    -user_name varchar(255)
    -email varchar(255)
    -password varchar(255)
    -is_premium bit default 0
    
3. likes: it has the following columns: 
      -ID int AI pk
      -song_id int AI PK
    
    
