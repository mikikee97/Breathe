package com.example.breathe;

import com.google.firebase.database.Exclude;

class UploadSong {
    public String songTitle, songDuration, songLink, mKey;

    public UploadSong(){}
    public UploadSong(String songTitle, String songDuration, String songLink, String mKey){
        if (songTitle.trim().equals("")){
            this.songTitle = "No title";
        }
        this.songTitle = songTitle;
        this.songDuration = songDuration;
        this.songLink = songLink;

    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }

    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
