package ch.uzh.model;

import javafx.scene.image.Image;

import java.util.Observable;

/**
 * Created by jesus on 19.03.2017.
 */
public class Friend extends Observable {
    private String name;
    private Image img;

    public Friend(String name){
        this.name = name;
    }

    public Friend(){
        this.name = "Mr. NoName";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }


}
