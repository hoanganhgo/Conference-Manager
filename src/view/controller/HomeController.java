package view.controller;

import dao.Business;
import entity.Location;
import entity.User;
import java.io.File;
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
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import view.model.MeetingModel;

public class HomeController implements Initializable {
    @FXML
    private Button login;
    
    @FXML Button register;
    
    @FXML
    private TableView<MeetingModel> tbData;
    
    @FXML
    private TableColumn<MeetingModel, Integer> number;
    
    @FXML
    private TableColumn<MeetingModel, String> name;
    
    @FXML
    private TableColumn<MeetingModel, String> time;
    
    @FXML
    private TableColumn<MeetingModel, String> status;
    
    @FXML
    private TableColumn<MeetingModel, Button> description;
    
    @FXML
    private GridPane gridPane;
    
    @FXML
    private Label label;
    
    @FXML
    private MenuButton persional;
    
    @FXML
    private MenuItem information;
    
    @FXML
    private MenuItem logout;
    
    @FXML
    private Button myConference;
    
    @FXML
    private MenuButton admin;
    
    @FXML
    private MenuItem manageConference;
    
    @FXML
    private MenuItem manageUser;
    
    @FXML
    private Button search;
    
    @FXML
    private TextField searchBox;
    
    @FXML
    private MenuButton searchFilter;
    
    @FXML
    private MenuItem searchConference;   //type=1
    
    @FXML
    private MenuItem searchLocation;   //type=2
    
    @FXML
    private MenuItem searchDescription;    //type=3
    
    @FXML
    private ScrollPane gridScrollPane;
    
    @FXML
    private MenuButton viewStyle;
    
    @FXML
    private MenuItem tableStyle;
    
    @FXML
    private MenuItem gridStyle;
    
    private int searchType=1;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Xác minh đăng nhập
        User user = Business.authenticator; 
        if (user!=null){
            //Xử lý sau khi đăng nhập
            login.setVisible(false);
            register.setVisible(false);
            label.setVisible(false);
                
            persional.setVisible(true);
            persional.setText("Xin chào! "+Business.detachName(user.getName()));
            myConference.setVisible(true);
            if (user.getAdmin()==1){
                admin.setVisible(true);
            }
        }
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
            Integer participants=Business.countParticipants((int)e[0], 1);

            String status = Business.checkStatus(participants, size, (Date)e[2]);
            
            //Thêm hội nghị vào giao diện
            list.add(new MeetingModel((int)e[0], num++, e[1].toString(), (Date)e[2], status, e[3].toString()));
        }
        
        //Cài đặt sự kiện khi double click vào hội nghị
        tbData.setOnMouseClicked((MouseEvent event)->{
            if (event.getClickCount()>=2){
                //Lấy dữ liệu hội nghị
                MeetingModel data=tbData.getSelectionModel().getSelectedItem();
                               
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
                Business.previous_screen=1;
                
                //Khởi tạo frame
                Stage detail=new Stage();
                detail.setTitle("Quản lý hội nghị");
                
                Scene scene=new Scene(frame, 1280,700);
                detail.setScene(scene);
                detail.setResizable(false);
                detail.centerOnScreen();
                
                detail.show();
                Business.homeStage.close();
            }
        });
        
        tbData.setItems(list); 
        
        //GridView
        List<Object[]> dataGrid=Business.getMeetingForGirdPane();
        int length=dataGrid.size();
        
        for (int i=0;i<length;i++){
            final int id=(int)dataGrid.get(i)[0];
            String avatar=String.valueOf(dataGrid.get(i)[1]);
            String name=String.valueOf(dataGrid.get(i)[2]);
            Date time=(Date)(dataGrid.get(i)[3]);
            String shortDescription=String.valueOf(dataGrid.get(i)[4]);
                                    
            File imageFile=new File(avatar.replace((char)92, (char)47));
            ImageView imageView=new ImageView();
            if (imageFile.exists()){
                Image image=new Image(imageFile.toURI().toString());
            
                imageView.setImage(image);
                imageView.setFitHeight(170);
                imageView.setFitWidth(260);
            }

            
            Label nameText=new Label(name);
            nameText.setFont(new Font("",22));
            
            String timeString=Business.formatDateTime(time);
            Label timeText=new Label(timeString);
            timeText.setFont(new Font("", 18));
            
            Button button = new Button("Xem Chi Tiết");
            button.setFont(new Font("", 18));
            button.addEventHandler((MouseEvent.MOUSE_CLICKED), (MouseEvent event)->{                              
                //Lấy dữ liệu miêu tả và hình ảnh
                Object[] objects=Business.getMeetingDetail(id);
                Location ln=(Location)objects[0];
                String longDescription=objects[1].toString();
                String avatarString=objects[2].toString();
                
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
                detailController.transferMessage(id, name, time, location, size, shortDescription, longDescription, avatar);
                detailController.transferButton(login, register, label, persional, information, logout, myConference, admin);
                Business.previous_screen=1;
                
                //Khởi tạo frame
                Stage detail=new Stage();
                detail.setTitle("Chi tiết hội nghị");
                
                Scene scene=new Scene(frame, 1280,700);
                detail.setScene(scene);
                detail.setResizable(false);
                detail.centerOnScreen();
                
                detail.show();   
                Business.closeWindow(event);
            });
            
            VBox box=new VBox();
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(20,80,20,20));
            box.getChildren().add(imageView);
            box.getChildren().add(nameText);
            box.getChildren().add(timeText);
            box.getChildren().add(button);
            gridPane.add(box,i%3,i/3);
        }   
        
               
        //Cài đặt sự kiện tìm kiếm
        search.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            search.setStyle("-fx-background-color: #ff9900;");
            list.clear();  //Xóa dữ liệu trên giao diện
            String content=searchBox.getText().toLowerCase();
            
           
            int number=1;
            
            switch (searchType){
                case 1:
                    for (Object[] e : meetings){
                        String name=e[1].toString().toLowerCase();
                
                        if (name.contains(content)){
                            //Kiểm tra tình trạng hội nghị
                            Integer size=((Location)e[4]).getSize();
                            Integer participants=Business.countParticipants((int)e[0], 1);
                            String status = Business.checkStatus(participants, size, (Date)e[2]);
            
                            //Thêm hội nghị vào giao diện
                            list.add(new MeetingModel((int)e[0], number++, e[1].toString(), (Date)e[2], status, e[3].toString()));
                        }               
                    } 
                    break;
                case 2:
                    for (Object[] e : meetings){
                        String location=((Location)e[4]).getName().toLowerCase();
                
                        if (location.contains(content)){
                            //Kiểm tra tình trạng hội nghị
                            Integer size=((Location)e[4]).getSize();
                            Integer participants=Business.countParticipants((int)e[0], 1);
                            String status = Business.checkStatus(participants, size, (Date)e[2]);
            
                            //Thêm hội nghị vào giao diện
                            list.add(new MeetingModel((int)e[0], number++, e[1].toString(), (Date)e[2], status, e[3].toString()));
                        }               
                    } 
                    break;
                case 3:
                    for (Object[] e : meetings){
                        String description=e[3].toString().toLowerCase();
                
                        if (description.contains(content)){
                            //Kiểm tra tình trạng hội nghị
                            Integer size=((Location)e[4]).getSize();
                            Integer participants=Business.countParticipants((int)e[0], 1);
                            String status = Business.checkStatus(participants, size, (Date)e[2]);
            
                            //Thêm hội nghị vào giao diện
                            list.add(new MeetingModel((int)e[0], number++, e[1].toString(), (Date)e[2], status, e[3].toString()));
                        }               
                    } 
                    break;
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
            Business.myConferenceStage=conference;
            
            conference.show();
            
            myConference.setStyle("-fx-background-color: #ff9900;");
            Business.closeWindow(event);
        });
        
        myConference.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            myConference.setStyle("-fx-background-color: #ff5500;");
        });
        
        //Sự kiện click manager Conference
        manageConference.setOnAction((ActionEvent event) -> {
            Parent frame=null;
            FXMLLoader loader=null;
            try {
                loader = new FXMLLoader(getClass().getResource("../frame/ManageConference.fxml"));
                frame = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            
            Stage conference=new Stage();
            conference.setTitle("Quản lý hội nghị");
            Scene scene=new Scene(frame, 1280, 700);
            conference.setScene(scene);
            conference.setResizable(false);
            conference.centerOnScreen();
            Business.manageConferenceStage=conference;
            
            conference.show();
            Business.homeStage.close();
        });
        
        //Sự kiện click manage Users
        manageUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               Parent frame=null;
               FXMLLoader loader=null;
               try {
                  loader = new FXMLLoader(getClass().getResource("../frame/ManageUser.fxml"));
                  frame = loader.load();
               } catch (IOException ex) {
                   Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                   return;
               }
            
               Stage userScreen=new Stage();
               userScreen.setTitle("Quản lý người dùng");
               Scene scene=new Scene(frame, 1280, 700);
               userScreen.setScene(scene);
               userScreen.setResizable(false);
               userScreen.centerOnScreen();
            
               userScreen.show();
               Business.homeStage.close();
            }
        });
        
        //search type
        searchConference.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchType=1;
                searchFilter.setText("Hội nghị");
                searchBox.setPromptText("Tìm kiếm theo tên hội nghị");
            }
        });
        
        searchLocation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchType=2;
                searchFilter.setText("Địa điểm");
                searchBox.setPromptText("Tìm kiếm theo địa điểm");
            }
        });
        
        searchDescription.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchType=3;
                searchFilter.setText("Mô tả");
                searchBox.setPromptText("Tìm kiếm theo mô tả");
            }
        });
        
        tableStyle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gridScrollPane.setVisible(false);
                
                searchFilter.setDisable(false);
                searchBox.setDisable(false);
                search.setDisable(false);
                
                tbData.setVisible(true);
                if (Business.authenticator==null){
                    label.setVisible(true);   
                }
                
                viewStyle.setText("Dạng bảng");
            }
        });
        
        gridStyle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tbData.setVisible(false);
                label.setVisible(false);
                
                searchFilter.setDisable(true);
                searchBox.setDisable(true);
                search.setDisable(true);
                
                gridScrollPane.setVisible(true);
                
                viewStyle.setText("Dạng lưới");
            }
        });
    }
}
