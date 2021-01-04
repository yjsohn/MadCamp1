package com.example.basicapplicationfunction;

import android.graphics.Bitmap;

/**
 *
 */
public class list_item {
    private long photo_id;
    private long person_id;
    private String nickname;
    private String content;
    private String email;

    public long getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(long photo_id) {
        this.photo_id = photo_id;
    }

    public long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(long person_id) {
        this.person_id = person_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public list_item(long photo_id, long person_id, String nickname, String content, String email) {
        this.photo_id = photo_id;
        this.person_id = person_id;
        this.nickname = nickname;
        this.content = content;
        this.email = email;
    }
}
