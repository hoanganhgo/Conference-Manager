package view.model;

import dao.Business;
import java.util.Date;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ManageMeetingModel {
    private int id;
    private int number;
    private String name;
    private Date date;
    private String time;
    private Button requirement;
    private Button attended;
    private Button edit;

    public ManageMeetingModel(int id, int number, String name, Date date) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.date = date;
        this.time = Business.formatDateTime(date);
        this.requirement = new Button("Hiển thị");
        this.attended = new Button("Hiển thị");
        Image image=new Image("./././images/edit.png");
        ImageView imageView=new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        this.edit = new Button();
        this.edit.setGraphic(imageView);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public Button getRequirement() {
        return requirement;
    }

    public void setRequirement(Button requirement) {
        this.requirement = requirement;
    }

    public Button getAttended() {
        return attended;
    }

    public void setAttended(Button attended) {
        this.attended = attended;
    }

    public Button getEdit() {
        return edit;
    }

    public void setEdit(Button edit) {
        this.edit = edit;
    }
    
    
}
