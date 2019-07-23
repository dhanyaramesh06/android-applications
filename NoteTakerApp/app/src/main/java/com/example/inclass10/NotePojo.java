package com.example.inclass10;

import java.io.Serializable;

public class NotePojo implements Serializable {
    String note_id;
    String note;

    public NotePojo() {
    }

    @Override
    public String toString() {
        return "NotePojo{" +
                "note_id='" + note_id + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
