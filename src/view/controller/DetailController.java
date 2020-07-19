package view.controller;

import dao.Business;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
    
    private Button signIn;
    
    private Button register;
    
    private Label label;
    
    private MenuButton persional;
    
    private MenuItem information;
    
    private MenuItem logout;
    
    private Button myConference;
    
    private MenuButton admin;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
       //Cài đặt sự kiện click đăng ký tham gia
        attend.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            //Kiểm tra đăng nhập
            if (Business.authenticator==null){
                Business.alertInformation("Đăng ký tham dự", "Bạn phải đăng nhập để có thể đăng ký tham dự hội nghị.");     
                
                //Sự kiện đăng nhập
                Parent frame=null;
                FXMLLoader loader=null;
                try {
                    loader = new FXMLLoader(getClass().getResource("../frame/SignIn.fxml"));
                    frame = loader.load();
                } catch (IOException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
            
                //Truyền Button cho frame sau
                SignInController signInController=loader.getController();            
                signInController.transferMessage(signIn, register, label, persional, information, logout, myConference, admin);
            
                //Khởi tạo màn hình đăng ký
                Stage signIn=new Stage();
                signIn.setTitle("Quản lý hội nghị");
                Scene scene=new Scene(frame, 390, 320);
                signIn.setScene(scene);
                signIn.setResizable(false);
                signIn.centerOnScreen();
            
                signIn.show();
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
        
        File imageFile=new File(avatar.replace((char)92, (char)47));
        Image image=new Image(imageFile.toURI().toString());       
        this.avatar.setImage(image);
        
        //Căn chỉ vị trí
        double scale=image.getHeight()/252;
        double width=image.getWidth()/scale;
        double height=image.getHeight()/scale;
        double x=(514-width)/2;
        this.avatar.setLayoutX(719+x); 
    }
    
    public void setVisibleRegister(boolean bool){
        this.attend.setVisible(bool);
        if (!bool){
            this.name.setLayoutX(520f);
        }
    }
    
    public void transferButton(Button login, Button register, Label label, MenuButton persional, MenuItem information, MenuItem logout, Button myConference, MenuButton admin){
        this.signIn=login;
        this.register=register;
        this.label=label;
        this.persional=persional;
        this.information=information;
        this.logout=logout;
        this.myConference=myConference;
        this.admin=admin;
    }
    
}
