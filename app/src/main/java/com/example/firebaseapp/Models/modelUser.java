package com.example.firebaseapp.Models;

public class modelUser{
      String Name,Image,Cover,Phone,Email,Search,UID;

    public modelUser() {
    }

    public modelUser(String name, String image, String cover, String phone, String email, String search,String uid) {
        this.Name = name;
        this.Image = image;
        this.Cover = cover;
        this.Phone = phone;
        this.Email = email;
        this.Search = search;
        this.UID=uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String cover) {
        this.Cover = cover;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getSearch() {
        return Search;
    }

    public void setSearch(String search) {
        this.Search = search;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
