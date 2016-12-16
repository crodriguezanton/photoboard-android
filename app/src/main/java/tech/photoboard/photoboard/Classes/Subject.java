package tech.photoboard.photoboard.Classes;

/**
 * Created by Elias on 02/12/2016.
 */

public class Subject {
    String name;
    int id;
    String group;

    public Subject(int id, String name, String group) {
        this.name = name;
        this.id = id;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
