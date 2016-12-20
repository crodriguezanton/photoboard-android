package tech.photoboard.photoboard.Classes;

/**
 * Created by Elias on 02/12/2016.
 */

public class Subject {
    String name;
    String short_name;
    String code;
    String subject_gallery;
    int id;

    public Subject(String name, String short_name, String subject_gallery, String code, int id) {
        this.name = name;
        this.short_name = short_name;
        this.subject_gallery = subject_gallery;
        this.code = code;
        this.id = id;
    }

    public String getSubject_gallery() {
        return subject_gallery;
    }

    public void setSubject_gallery(String subject_gallery) {
        this.subject_gallery = subject_gallery;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShort_name() {
        if(short_name== null) return name;
        return short_name;
    }

    public Subject(String name, String short_name, String code, String url) {
        this.name = name;
        this.short_name = short_name;
        this.code = code;
        this.subject_gallery = url;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public Subject(String code, String name, String url) {
        this.name = name;
        this.code = code;
        this.subject_gallery = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return subject_gallery;
    }

    public void setUrl(String url) {
        this.subject_gallery = url;
    }
}
