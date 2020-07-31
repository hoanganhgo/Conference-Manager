package view.controller;

import dao.Business;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.model.NameModel;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class DetailController implements Initializable {

    @FXML
    private Label name;
    
    @FXML
    private Label time;
    
    @FXML
    private TextArea location;
    
    @FXML
    private Label waiting;
    
    @FXML
    private Label accepted;
    
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
    
    @FXML
    private Button cancel;    
    
    @FXML
    private Button btnDetail;
    
    @FXML
    private Button btnRegister;
    
    @FXML
    private AnchorPane detail;
    
    @FXML
    private AnchorPane listRegister;
    
    @FXML
    private Button btnBack;
    
    @FXML
    private TableView<NameModel> tbRegister;
    
    @FXML
    private TableColumn<NameModel, Integer> number1;
    
    @FXML
    private TableColumn<NameModel, String> fullname1;
    
    @FXML
    private TableView<NameModel> tbAttend;
    
    @FXML
    private TableColumn<NameModel, Integer> number2;
    
    @FXML
    private TableColumn<NameModel, String> fullname2;
    
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
        //Bảng đăng ký tham dự
        number1.setCellValueFactory(new PropertyValueFactory<>("number"));
        fullname1.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        //Vô hiệu hóa sorting ở các cột
        number1.setSortable(false);
        fullname1.setSortable(false);
        
        //Căn trái cột tên
        fullname1.setStyle("-fx-alignment: CENTER-LEFT;");
        
        //Bảng đã được duyệt
        number2.setCellValueFactory(new PropertyValueFactory<>("number"));
        fullname2.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        //Vô hiệu hóa sorting ở các cột
        number2.setSortable(false);
        fullname2.setSortable(false);     
        
        //Căn trái cột tên
        fullname2.setStyle("-fx-alignment: CENTER-LEFT;");
        
       //Cài đặt sự kiện click đăng ký tham gia
        attend.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            //Kiểm tra tình trạng hội nghị
            if (Business.countParticipants(meetingId, 2)>=numSize){
                Business.alertError("Đăng ký tham dự", "Hiện tại số người đăng ký đã đủ.\nVì vậy, tính năng này sẽ tạm khóa!");
                return;
            }
            if (Business.countParticipants(meetingId, 1)>=numSize){
                Business.alertError("Đăng ký tham dự", "Hiện tại số người tham gia hội nghị đã đủ!");
                return;
            }
            
            //Kiểm tra đăng nhập
            if (Business.authenticator==null){                
                //Lựa chọn đăng nhập hay đăng ký
                ButtonType yes = new ButtonType("Đăng nhập", ButtonBar.ButtonData.YES);
                ButtonType no = new ButtonType("Chưa có tài khoản", ButtonBar.ButtonData.NO);
                Alert alert=new Alert(Alert.AlertType.INFORMATION, "Bạn phải đăng nhập để có thể đăng ký tham dự hội nghị!", yes, no);
                alert.setTitle("Đăng nhập/Đăng ký");
                alert.setHeaderText(null);
                Optional<ButtonType> option=alert.showAndWait();
                if (option.orElse(yes)==yes){
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
                    signIn.setTitle("Đăng nhập");
                    Scene scene=new Scene(frame, 390, 320);
                    signIn.setScene(scene);
                    signIn.setResizable(false);
                    signIn.centerOnScreen();
            
                    signIn.show();        
                }else if (option.orElse(no)==no){
                    Parent frame=null;
                    try {
                        frame = FXMLLoader.load(getClass().getResource("../frame/SignUp.fxml"));
                    } catch (IOException ex) {
                        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        return;
                    }
                        
                    Stage signUp=new Stage();
                    signUp.setTitle("Đăng ký");
                    Scene scene=new Scene(frame, 390, 500);
                    signUp.setScene(scene);
                    signUp.setResizable(false);
                    signUp.centerOnScreen();
                        
                    signUp.show();    
                }
                
                return;
            }
            
            int userId=Business.authenticator.getUserId();
            
            //Kiểm tra tình trạng
            int status=Business.getStatus(meetingId, userId);
            if (status<0){
                //Kiểm tra số người đang chờ duyệt
                int num=Business.countParticipants(meetingId, 2);
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
        
        btnDetail.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            listRegister.setVisible(false);
            detail.setVisible(true);
            
            btnRegister.setStyle(null);
            btnDetail.setStyle("-fx-background-color:#ffffff; -fx-border-color:#ff9900");
        });
        
        btnRegister.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            detail.setVisible(false);
            listRegister.setVisible(true);
            
            btnDetail.setStyle(null);
            btnRegister.setStyle("-fx-background-color:#ffffff; -fx-border-color:#ff9900");
            
            //Tạo danh sách hiển thị người đăng ký tham dự
            ObservableList<NameModel> list1=FXCollections.observableArrayList();
            int number=1;        
            List<String> data1=Business.getUserByMeeting(meetingId, 2);
            for (String s : data1){
                list1.add(new NameModel(number++, s));
            }
            tbRegister.setItems(list1); 
            
            //Tạo danh sách hiển thị người được duyệt
            ObservableList<NameModel> listAttend=FXCollections.observableArrayList();
            number=1;
            List<String> data2=Business.getUserByMeeting(meetingId, 1);
            for (String s : data2){
                listAttend.add(new NameModel(number++, s));
            }
            tbAttend.setItems(listAttend);
        });
        
        btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            //Xác định màn hình trước đó
            int previous=Business.previous_screen;
            if (previous==1){
                //Home
                Business.homeStage=Business.back(getClass().getResource("../frame/Home.fxml"), "Trang chủ");
            }else if (previous==2){
                //My Conference
                Business.myConferenceStage=Business.back(getClass().getResource("../frame/MyConference.fxml"), "Hội nghị của tôi");
            }else if (previous==3){
                //Manage Conference
                Business.manageConferenceStage=Business.back(getClass().getResource("../frame/ManageConference.fxml"), "Quản lý hội nghị");
            }
                       
            Business.closeWindow(event);
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
        
        //Lấy số người đang chờ duyệt
        int numParticipant=Business.countParticipants(meetingId, 2);
        this.waiting.setText(numParticipant+" người");
        
        //Lấy số người đã được duyệt
        int numAccepted=Business.countParticipants(meetingId, 1);
        this.accepted.setText(numAccepted+" người");
        
        //Nếu hội nghị đã đủ người tham gia thì không được đăng ký
        if (numAccepted>=size){
            attend.setDisable(true);
        }
        
        File imageFile=new File(avatar.replace((char)92, (char)47));
        if (imageFile.exists()){
            Image image=new Image(imageFile.toURI().toString());       
            this.avatar.setImage(image);
        
            //Căn chỉ vị trí
            double scale=image.getHeight()/252;
            double width=image.getWidth()/scale;
            double height=image.getHeight()/scale;
            double x=(514-width)/2;
            this.avatar.setLayoutX(707+x);
        }
        

        

    }
    
    public void setVisibleRegister(boolean bool){
        this.attend.setVisible(bool);
    }
    
    public void authorTransfer(int meetingId, String status){        
        if (!status.contentEquals("Bị từ chối")){
            this.cancel.setVisible(true); 
            this.cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
                ButtonType yes = new ButtonType("Vâng", ButtonBar.ButtonData.YES);
                ButtonType no = new ButtonType("Không", ButtonBar.ButtonData.NO);
                Alert alert=new Alert(Alert.AlertType.WARNING, "Bạn có chắc muốn hủy đăng ký tham dự hội nghị này?", yes, no);
                alert.setTitle("Hủy đăng ký");
                alert.setHeaderText(null);
                Optional<ButtonType> option=alert.showAndWait();
                if (option.orElse(yes)==yes){
                    this.cancel.setVisible(false);
                    this.attend.setVisible(true);
                    this.attend.setDisable(false);
                    Business.cancelMeeting(Business.authenticator.getUserId(), meetingId);                   
                }
            });
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
