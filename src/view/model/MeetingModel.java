package view.model;

import dao.Business;
import java.util.Date;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;


public class MeetingModel {
    private Integer id;
    private Integer number;
    private String name;
    private String time;
    private Date dateTime;
    private String status;
    private String descriptionContent;
    private MenuButton description=new MenuButton("Mô tả");

    public MeetingModel(Integer id, Integer number, String name, Date dateTime, String status, String description) {
        this.id=id;
        this.number = number;
        this.name = name;
        this.dateTime=dateTime;
        this.time = Business.formatDateTime(dateTime);
        this.status = status;
        this.descriptionContent=description;
        this.description.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent)->{
            if (this.description.getItems().isEmpty()){
                String shortDescription=Business.convertShortDescription(description);
                this.description.getItems().add(new MenuItem(shortDescription));    
            }           
        });
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }       

    public MenuButton getDescription() {
        return description;
    }

    public void setDescription(MenuButton description) {
        this.description = description;
    }
    
    public String getDescriptionContent() {
        return descriptionContent;
    }

    public void setDescriptionContent(String descriptionContent) {
        this.descriptionContent = descriptionContent;
    }

    public Integer getId() {
        return id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    
    
}
