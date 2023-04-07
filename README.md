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
    
    

![OSTunes login](https://user-images.githubusercontent.com/115045576/230653412-3df92dbe-079e-47eb-a9e2-f7a45dcf958d.png)

![OSTunes sign up](https://user-images.githubusercontent.com/115045576/230653457-d248cdc0-a849-495b-bfb0-76274dbe1dfb.png)

![OSTunes Home](https://user-images.githubusercontent.com/115045576/230653486-b919037b-38e6-441f-b8e6-d0855af92ba0.png)

![OSTunes likes](https://user-images.githubusercontent.com/115045576/230653559-f1976745-ad5b-4069-a49c-49e18520a29f.png)

![OSTunes upload](https://user-images.githubusercontent.com/115045576/230653606-b55b6565-bab2-4f1d-9156-14eec7471109.png)
