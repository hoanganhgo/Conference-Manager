package view.controller;

import dao.Business;
import entity.Location;
import java.io.IOException;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import view.model.MeetingModel;

public class HomeController implements Initializable {
    @FXML
    public Button login;
    
    @FXML Button register;
    
    @FXML
    private TableView<MeetingModel> tbData;
    
    @FXML
    public TableColumn<MeetingModel, Integer> number;
    
    @FXML
    public TableColumn<MeetingModel, String> name;
    
    @FXML
    public TableColumn<MeetingModel, String> time;
    
    @FXML
    public TableColumn<MeetingModel, String> status;
    
    @FXML
    public TableColumn<MeetingModel, Button> description;
    
    @FXML
    public Label label;
    
    @FXML
    public MenuButton persional;
    
    @FXML
    public MenuItem information;
    
    @FXML
    public MenuItem logout;
    
    @FXML
    public Button myConference;
    
    @FXML
    public Button admin;
    
    @FXML
    public Button search;
    
    @FXML
    public TextField searchBox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
       
        //Vô hiệu hóa sorting ở các cột
        number.setSortable(false);
        name.setSortable(false);
        time.setSortable(false);
        status.setSortable(false);
        description.setSortable(false);
        
        //Căn trái cột tên hội nghị
        name.setStyle("-fx-alignment: CENTER-LEFT;");
        
        
        //Lấy danh sách hội nghị
        List<Object[]> meetings = Business.getAllMeetings();
       
        //Tạo danh sách hiển thị
        ObservableList<MeetingModel> list=FXCollections.observableArrayList();
        int num=1;
        
        for (Object[] e : meetings)
        {
            //Kiểm tra tình trạng hội nghị
            Integer size=((Location)e[4]).getSize();
            Integer participants=Business.countParticipants((int)e[0]);

            String status = Business.checkStatus(participants, size, (Date)e[2]);
            
            //Thêm hội nghị vào giao diện
            list.add(new MeetingModel((int)e[0], num++, e[1].toString(), (Date)e[2], status, e[3].toString()));
        }
        
        //Cài đặt sự kiện khi double click vào hội nghị
        tbData.setOnMouseClicked((MouseEvent event)->{
            if (event.getClickCount()>=2){
                //Lấy dữ liệu hội nghị
                MeetingModel data=tbData.getSelectionModel().getSelectedItem();
                
                //Lấy vị trí được double click
                Integer pos=data.getNumber()-1;
                               
                //Lấy dữ liệu miêu tả và hình ảnh
                Object[] objects=Business.getMeetingDetail(data.getId());
                Location ln=(Location)objects[0];
                String longDescription=objects[1].toString();
                String avatar=objects[2].toString();
                
                //Tạo chuỗi địa điểm
                String location=ln.getName()+", "+ln.getAdress();
                
                //Lấy kích thước hội nghị
                int size=ln.getSize();
                
                //Load frame
                Parent frame=null;
                FXMLLoader loader=null;
                try {
                    loader=new FXMLLoader(getClass().getResource("../frame/Detail.fxml"));
                    frame = loader.load();
                } catch (IOException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //Truyền dữ liệu cho frame detail
                DetailController detailController= loader.getController();
                detailController.transferMessage(data.getId(), data.getName(), data.getDateTime(), location, size, data.getDescriptionContent(), longDescription, avatar);
                detailController.transferButton(login, register, label, persional, information, logout, myConference, admin);
                
                //Khởi tạo frame
                Stage detail=new Stage();
                detail.setTitle("Quản lý hội nghị");
                
                Scene scene=new Scene(frame, 1280,700);
                detail.setScene(scene);
                detail.setResizable(false);
                detail.centerOnScreen();
                
                detail.show();
            }
        });
        
        tbData.setItems(list);   
        
        //Cài đặt sự kiện tìm kiếm
        search.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            search.setStyle("-fx-background-color: #ff9900;");
          
            String content=searchBox.getText().toLowerCase();
            
            list.clear();  //Xóa dữ liệu trên giao diện
            
            int number=1;
            for (Object[] e : meetings){
                String name=e[1].toString().toLowerCase();
                String location=((Location)e[4]).getName().toLowerCase();
                String description=e[3].toString().toLowerCase();
                
                if (name.contains(content) || location.contains(content) || description.contains(content)){
                    //Kiểm tra tình trạng hội nghị
                    Integer size=((Location)e[4]).getSize();
                    Integer participants=Business.countParticipants((int)e[0]);
                    String status = Business.checkStatus(participants, size, (Date)e[2]);
            
                    //Thêm hội nghị vào giao diện
                    list.add(new MeetingModel((int)e[0], number++, e[1].toString(), (Date)e[2], status, e[3].toString()));
                }
                    
                
            }
            
            tbData.setItems(list);
        });
        
        search.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            search.setStyle("-fx-background-color: #ff5500;");
        });
                
        //Cài sự kiện click đăng ký
        register.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            Parent frame=null;
            try {
                frame = FXMLLoader.load(getClass().getResource("../frame/SignUp.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
                        
            Stage signUp=new Stage();
            signUp.setTitle("Quản lý hội nghị");
            Scene scene=new Scene(frame, 390, 500);
            signUp.setScene(scene);
            signUp.setResizable(false);
            signUp.centerOnScreen();
                        
            signUp.show();
            
            register.setStyle("-fx-background-color: #ff9900;");
        });
        
        register.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            register.setStyle("-fx-background-color: #ff5500;");
        });
        
        //Cài đặt sự kiện đăng nhập
        login.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
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
            signInController.transferMessage(login, register, label, persional, information, logout, myConference, admin);
            
            //Khởi tạo màn hình đăng ký
            Stage signIn=new Stage();
            signIn.setTitle("Quản lý hội nghị");
            Scene scene=new Scene(frame, 390, 320);
            signIn.setScene(scene);
            signIn.setResizable(false);
            signIn.centerOnScreen();
            
            signIn.show();
            
            login.setStyle("-fx-background-color: #ff9900;");
        });
        
        login.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            login.setStyle("-fx-background-color: #ff5500;");
        });
        
        //Cài sự kiện logout
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Business.authenticator=null;
                persional.setVisible(false);
                myConference.setVisible(false);
                admin.setVisible(false);
            
                login.setVisible(true);
                register.setVisible(true);
                label.setVisible(true);
            }
        });
        
        
        //Cài sự kiện xem thông tin cá nhân
        information.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent frame=null;
                FXMLLoader loader=null;
                try {
                    loader = new FXMLLoader(getClass().getResource("../frame/Information.fxml"));
                    frame = loader.load();
                } catch (IOException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
                
                
                //Khởi tạo stage
                Stage info=new Stage();
                info.setTitle("Thông tin cá nhân");
                Scene scene=new Scene(frame, 623, 314);
                info.setScene(scene);
                info.setResizable(false);
                info.centerOnScreen();
                
                //Gửi dữ liệu cho màn hình được mở
                InformationController informationController=loader.getController();
                informationController.transfer(info, persional);
                
                info.show();
            }
        });
        
        //Cài đặt sự kiện click MyConference
        myConference.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            Parent frame=null;
            FXMLLoader loader=null;
            try {
                loader = new FXMLLoader(getClass().getResource("../frame/MyConference.fxml"));
                frame = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            
            Stage conference=new Stage();
            conference.setTitle("Hội nghị của tôi");
            Scene scene=new Scene(frame, 1280, 700);
            conference.setScene(scene);
            conference.setResizable(false);
            conference.centerOnScreen();
            
            conference.show();
            
            myConference.setStyle("-fx-background-color: #ff9900;");
        });
        
        myConference.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            myConference.setStyle("-fx-background-color: #ff5500;");
        });
    }  
}
