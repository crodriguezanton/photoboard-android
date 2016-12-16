package tech.photoboard.photoboard.Classes;

import java.util.ArrayList;

/**
 * Created by sergi on 16/12/2016.
 */

public class PictureGallery {

    ArrayList<Photo> pictures;
    Subject subject;

    public PictureGallery(ArrayList<Photo> pictures, Subject subject) {
        this.pictures = pictures;
        this.subject = subject;
    }

    public ArrayList<Photo> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<Photo> pictures) {
        this.pictures = pictures;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
