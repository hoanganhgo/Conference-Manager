package view.model;

import entity.Location;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class LocationModel {
    private int number;
    private int id;
    private String name;
    private String address;
    private int size;
    private Button choose;

    public LocationModel(int number, int id, String name, String address, int size, Button choose) {
        this.number = number;
        this.id=id;
        this.name = name;
        this.address = address;
        this.size = size;
        this.choose = choose;
        Image image=new Image("./././images/tick.png");
        ImageView imageView=new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        this.choose.setGraphic(imageView);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Button getChoose() {
        return choose;
    }

    public void setChoose(Button choose) {
        this.choose = choose;
    }
    
    
}
