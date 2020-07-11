package view.controller;

import dao.Business;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class DetailController implements Initializable {

    @FXML
    private Label name;
    
    @FXML
    private Label time;
    
    @FXML
    private TextArea location;
    
    @FXML
    private Label participants;
    
    @FXML
    private Label size;
    
    @FXML
    private TextArea shortDescription;
    
    @FXML
    private TextArea longDescription;
    
    @FXML
    private ImageView avatar;
    
    @FXML
    private Button attend;
    
    private int meetingId;
    
    private int numSize;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
       //Cài đặt sự kiện click đăng ký tham gia
        attend.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            //Kiểm tra đăng nhập
            if (Business.authenticator==null){
                Business.alertInformation("Đăng ký tham dự", "Bạn phải đăng nhập để có thể đăng ký tham dự hội nghị.");                
                return;
            }
            
            int userId=Business.authenticator.getUserId();
            
            //Kiểm tra tình trạng
            int status=Business.getStatus(meetingId, userId);
            if (status<0){
                //Kiểm tra số người đã đăng ký
                int num=Business.countParticipants(meetingId);
                if (num>=numSize){
                    Business.alertError("Đăng ký tham dự", "Hội nghị đã đủ người!");
                    return;
                }
                
                Business.attendConference(meetingId, userId);
                Business.alertInformation("Đăng ký tham dự", "Đăng ký tham dự hội nghị thành công!\nVui lòng chờ để được chấp nhận!");
            }else{
                if (status==0){
                    Business.alertError("Đăng ký tham dự", "Bạn đã bị từ chối tham dự hội nghị ngày.");
                }else if (status==1){
                    Business.alertError("Đăng ký tham dự", "Bạn đã được duyệt tham dự hội nghị này.");
                }else if (status==2){
                    Business.alertError("Đăng ký tham dự", "Đang chờ để được duyệt.");
                }
            }            
        });
    }    
    
    public void transferMessage(int meetingId, String name, Date time, String location, int size, String shortDescription, String longDescription, String avatar){
        this.meetingId=meetingId;
        this.name.setText(name);
        this.time.setText(Business.formatDateTime(time));
        this.location.setText(location);
        this.numSize=size;
        this.size.setText(size+" người");
        this.shortDescription.setText(shortDescription);
        this.longDescription.setText(longDescription);
        
        //Nếu hội nghị đã diễn ra thì không được đăng ký tham dự
        Date current=new Date();
        if (time.compareTo(current)<=0){
            this.attend.setDisable(true);
        }
        
        //Lấy số người tham gia
        int numParticipant=Business.countParticipants(meetingId);
        this.participants.setText(numParticipant+" người");
        
        //Nếu hội nghị đã đủ người thì không được đăng ký
        if (numParticipant>=size){
            attend.setDisable(true);
        }
    }      
    
}
