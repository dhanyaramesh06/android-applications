package com.example.musixmatch;

public class Musixmatch {
    String track_name, album_name, artist_name, updated_time, track_share_url;

    public Musixmatch() {
    }

    @Override
    public String toString() {
        return "Musixmatch{" +
                "track_name='" + track_name + '\'' +
                ", album_name='" + album_name + '\'' +
                ", artist_name='" + artist_name + '\'' +
                ", updated_time='" + updated_time + '\'' +
                ", track_share_url='" + track_share_url + '\'' +
                '}';
    }
}
