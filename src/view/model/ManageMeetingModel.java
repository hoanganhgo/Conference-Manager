package view.model;

import dao.Business;
import entity.Meeting;
import entity.Location;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import view.controller.ApprovalController;
import view.controller.CreateConferenceController;
import view.controller.HomeController;

public class ManageMeetingModel {
    private int id;
    private int number;
    private String name;
    private Date date;
    private String time;
    private Button requirement;
    private Button edit;

    public ManageMeetingModel(int id, int number, String name, Date date, Integer size) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.date = date;
        this.time = Business.formatDateTime(date);
        this.requirement = new Button("Hiển thị");
        Image image=new Image("./././images/edit.png");
        ImageView imageView=new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        this.edit = new Button();
        this.edit.setGraphic(imageView);
        
        if (Business.countParticipants(id, 1)>=size){
            this.requirement.setText("Đã đủ người");
        }else if ((new Date()).compareTo(date)>=0){
            this.requirement.setText("Đã diễn ra");
        }else{
            this.requirement.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            //Load frame
            Parent frame=null;
            FXMLLoader loader=null;
            try {
                loader=new FXMLLoader(getClass().getResource("../frame/Approval.fxml"));
                frame = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            //Truyền dữ liệu cho frame list users
            ApprovalController approvalController = loader.getController();
            approvalController.transferData(id, name, size);
                
            //Khởi tạo frame users
            Stage users=new Stage();
            users.setTitle("Danh sách đăng ký tham dự");
                
            Scene scene=new Scene(frame, 1280,700);
            users.setScene(scene);
            users.setResizable(false);
            users.centerOnScreen();
                
            users.show();
            });
        }
          
        this.edit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            if ((new Date()).compareTo(date)>=0){
                Business.alertError("Chỉnh sửa", "Bạn không thể chỉnh sửa hội nghị đã diễn ra");
                return;
            }
            
            Parent frame=null;
            FXMLLoader loader=null;
            try {
                loader=new FXMLLoader(getClass().getResource("../frame/CreateConference.fxml"));
                frame = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            
            //Lấy dữ liệu
            Meeting meeting=Business.getMeetingByID(id);

            Location location=Business.getLocationByID(meeting.getLocation().getLocationId());
            String locationString=location.getName()+", "+location.getAdress();
            
            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
            String dateString=formatter.format(meeting.getTime());
            
            CreateConferenceController createConferenceController=loader.getController();
            createConferenceController.transferEdit(meeting.getName(), meeting.getLocation().getLocationId(), locationString, location.getSize(), meeting.getTime().getHours(), meeting.getTime().getMinutes(), dateString, meeting.getShortDescription(), meeting.getLongDescription(), meeting.getAvatar());
            createConferenceController.initEventUpdate(id);
                        
            Stage editConference=new Stage();
            editConference.setTitle("Chỉnh sửa hội nghị");
            Scene scene=new Scene(frame, 1280, 700);
            editConference.setScene(scene);
            editConference.setResizable(false);
            editConference.centerOnScreen();
                        
            editConference.show();
            
            //Đóng cửa sổ hiện tại
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        });
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

    public Button getEdit() {
        return edit;
    }

    public void setEdit(Button edit) {
        this.edit = edit;
    }
    
    
}
