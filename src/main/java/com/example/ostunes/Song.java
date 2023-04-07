package com.example.ostunes;

public class Song {
    private String songName;
    private String artistName;
    private String url;
    private String songImage;
    private  int songID;

    public Song(String songName, String artistName, String url, String songImage, int songID) {
        this.songName = songName;
        this.artistName = artistName;
        this.url = url;
        this.songImage = songImage;
        this.songID = songID;
    }

    // Getters and setters

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getUrl() {
        return url;
    }

    public String getSongImage() {
        return songImage;
    }
    public int getSongID() {
        return songID;
    }

    public void setSong(String songName, String artistName, String url, String songImage){
        setSongName(songName);
        setArtistName(artistName);
        setUrl(url);
        setSongImage(songImage);
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSongImage(String songImage) {
        this.songImage = songImage;
    }
}
