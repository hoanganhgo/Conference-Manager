package view.controller;

import dao.Business;
import entity.Location;
import java.io.IOException;
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
import javafx.scene.control.Button;
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

public class MyConferenceController implements Initializable {

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
    private Label total;
    
    @FXML
    private Button search;
    
    @FXML
    private TextField searchBox;
    
    @FXML
    private MenuButton sort;
    
    @FXML
    private MenuItem newest;
    
    @FXML
    private MenuItem oldest;
    
    @FXML
    private MenuButton statusFilter;
    
    @FXML
    private MenuItem accepted;
    
    @FXML
    private MenuItem waiting;
    
    @FXML
    private MenuItem rejected;
    
    @FXML
    private MenuItem statusAll;
    
    @FXML
    private MenuButton locationFilter;
    
    @FXML
    private MenuItem locationAll;
    
    @FXML
    private MenuButton searchFilter;
    
    @FXML
    private Button btnBack;
    
    @FXML
    private MenuItem searchConference;   //type=1
    
    @FXML
    private MenuItem searchLocation;   //type=2
    
    @FXML
    private MenuItem searchDescription;    //type=3
    
    private int searchType=1;
    
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
        
        //Lấy danh sách hội nghị của tôi
        List<Object[]> meetings=Business.getMyConferences(Business.authenticator.getUserId());
        
        //Tạo danh sách hiển thị
        ObservableList<MeetingModel> list=FXCollections.observableArrayList();
        int num=1;
        
        for (Object[] e : meetings)
        {
            String status = Business.checkStatus((int)e[5]);
            
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
                detailController.setVisibleRegister(false);
                detailController.authorTransfer(data.getId(), data.getStatus());
                Business.previous_screen=2;
                
                //Khởi tạo frame
                Stage detail=new Stage();
                detail.setTitle("Quản lý hội nghị");
                
                Scene scene=new Scene(frame, 1280,700);
                detail.setScene(scene);
                detail.setResizable(false);
                detail.centerOnScreen();
                
                detail.show();
                Business.myConferenceStage.close();
            }
        });
        
        tbData.setItems(list);
        
        total.setText("Tổng số: "+list.size());
        
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
                           String status = Business.checkStatus((int)e[5]);
            
                            //Thêm hội nghị vào giao diện
                            list.add(new MeetingModel((int)e[0], number++, e[1].toString(), (Date)e[2], status, e[3].toString()));
                        }               
                    } 
                    break;
                case 2:
                    for (Object[] e : meetings){
                        String location=((Location)e[4]).getName().toLowerCase();
                
                        if (location.contains(content)){
                            String status = Business.checkStatus((int)e[5]);
            
                            //Thêm hội nghị vào giao diện
                            list.add(new MeetingModel((int)e[0], number++, e[1].toString(), (Date)e[2], status, e[3].toString()));
                        }               
                    } 
                    break;
                case 3:
                    for (Object[] e : meetings){
                        String description=e[3].toString().toLowerCase();
                
                        if (description.contains(content)){
                            String status = Business.checkStatus((int)e[5]);
            
                            //Thêm hội nghị vào giao diện
                            list.add(new MeetingModel((int)e[0], number++, e[1].toString(), (Date)e[2], status, e[3].toString()));
                        }               
                    } 
                    break;
            }
                       
            tbData.setItems(list);
            
            total.setText("Tổng số: "+(number-1));
            sort.setText("Sắp xếp");
            statusFilter.setText("Lọc theo trạng thái");
            locationFilter.setText("Lọc theo địa điểm");
        });
        
        search.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            search.setStyle("-fx-background-color: #ff5500;");
        });
        
        //Cài đặt sự kiện sắp xếp cũ nhất
        oldest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Khởi tạo array MeetingModel
                ArrayList<MeetingModel> array=new ArrayList<>();
                
                for (Object[] e : meetings){
                    String status = Business.checkStatus((int)e[5]);
                    array.add(new MeetingModel((int)e[0], 0, e[1].toString(), (Date)e[2], status, e[3].toString()));
                }
                
                Business.sortMeetingASC(array);
                list.clear();
                
                int number=1;
                for (MeetingModel e : array){
                    e.setNumber(number++);
                    list.add(e);
                }

                tbData.setItems(list);
                
                total.setText("Tổng số: "+(number-1));
                sort.setText("Cũ nhất");
                statusFilter.setText("Lọc theo trạng thái");
                locationFilter.setText("Lọc theo địa điểm");
            }
        });
        
        //Cài đặt sự kiện sắp xếp mới nhất
        newest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Khởi tạo array MeetingModel
                ArrayList<MeetingModel> array=new ArrayList<>();
                
                for (Object[] e : meetings){
                    String status = Business.checkStatus((int)e[5]);
                    array.add(new MeetingModel((int)e[0], 0, e[1].toString(), (Date)e[2], status, e[3].toString()));
                }
                
                Business.sortMeetingDESC(array);
                list.clear();
                
                int number=1;
                for (MeetingModel e : array){
                    e.setNumber(number++);
                    list.add(e);
                }

                tbData.setItems(list);
                
                total.setText("Tổng số: "+(number-1));
                sort.setText("Mới nhất");
                statusFilter.setText("Lọc theo trạng thái");
                locationFilter.setText("Lọc theo địa điểm");
            }
        });
        
        //Cài đặt sự kiện lọc hội nghị được duyệt
        accepted.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.clear();
                
                int number=1;       
                for (Object[] e : meetings)
                {
                    int code=(int)e[5];
                    if (code==1){
                        String status = Business.checkStatus(code);            
                        //Thêm hội nghị vào giao diện
                        list.add(new MeetingModel((int)e[0], number++, e[1].toString(), (Date)e[2], status, e[3].toString()));
                    }
                }
                
                tbData.setItems(list);
                
                total.setText("Tổng số: "+(number-1));
                statusFilter.setText("Đã được duyệt");
                sort.setText("Sắp xếp");
                locationFilter.setText("Lọc theo địa điểm");
            }
        });
        
        //Cài đặt sự kiện lọc hội nghị đang chờ duyệt
        waiting.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.clear();
                
                int number=1;       
                for (Object[] e : meetings)
                {
                    int code=(int)e[5];
                    if (code==2){
                        String status = Business.checkStatus(code);            
                        //Thêm hội nghị vào giao diện
                        list.add(new MeetingModel((int)e[0], number++, e[1].toString(), (Date)e[2], status, e[3].toString()));
                    }
                }
                
                tbData.setItems(list);
                
                total.setText("Tổng số: "+(number-1));
                statusFilter.setText("Đang chờ duyệt");
                sort.setText("Sắp xếp");
                locationFilter.setText("Lọc theo địa điểm");
            }
        });
        
        //Cài đặt sự kiện lọc hội nghị bị từ chối
        rejected.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.clear();
                
                int number=1;       
                for (Object[] e : meetings)
                {
                    int code=(int)e[5];
                    if (code==0){
                        String status = Business.checkStatus(code);            
                        //Thêm hội nghị vào giao diện
                        list.add(new MeetingModel((int)e[0], number++, e[1].toString(), (Date)e[2], status, e[3].toString()));
                    }
                }
                
                tbData.setItems(list);
                
                total.setText("Tổng số: "+(number-1));
                statusFilter.setText("Bị từ chối");
                sort.setText("Sắp xếp");
                locationFilter.setText("Lọc theo địa điểm");
            }
        });
        
        //Cài đặt sự kiện lấy tất cả
        statusAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.clear();
                
                int number=1;       
                for (Object[] e : meetings)
                {
                    int code=(int)e[5];
                    String status = Business.checkStatus(code);            
                    //Thêm hội nghị vào giao diện
                    list.add(new MeetingModel((int)e[0], number++, e[1].toString(), (Date)e[2], status, e[3].toString()));
                }
                
                tbData.setItems(list);
                
                total.setText("Tổng số: "+(number-1));
                statusFilter.setText("Lọc theo trạng thái");
                sort.setText("Sắp xếp");            
                locationFilter.setText("Lọc theo địa điểm");
            }
        });
        
        //Cài đặt sự kiện lọc theo địa điểm
        ArrayList<Integer> codes=new ArrayList<>();
        for (Object[] e : meetings){
            //Lấy tên địa điểm
            Location location=(Location)e[4];
            
            if (!codes.contains(location.getLocationId())){
                codes.add(location.getLocationId());
                String item = location.getName();
            
                //Khởi tạo MenuItem
                MenuItem menuItem=new MenuItem(item);
                
                menuItem.setOnAction(a->{
                    list.clear();
                
                    int number=1;       
                    for (Object[] m : meetings)
                    {
                        int id=((Location)m[4]).getLocationId();
                        if (id==location.getLocationId()){
                            String status = Business.checkStatus((int)m[5]);            
                            //Thêm hội nghị vào giao diện
                            list.add(new MeetingModel((int)m[0], number++, m[1].toString(), (Date)m[2], status, m[3].toString()));
                        }
                    }
                
                    tbData.setItems(list);
                
                    total.setText("Tổng số: "+(number-1));
                    locationFilter.setText(item);
                    sort.setText("Sắp xếp");
                    statusFilter.setText("Lọc theo trạng thái");
                });
            
                locationFilter.getItems().add(menuItem);            
            }
        }
        
        //Cài đặt sự kiện lấy tất cả
        locationAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.clear();
                
                int number=1;       
                for (Object[] e : meetings)
                {
                    int code=(int)e[5];
                    String status = Business.checkStatus(code);            
                    //Thêm hội nghị vào giao diện
                    list.add(new MeetingModel((int)e[0], number++, e[1].toString(), (Date)e[2], status, e[3].toString()));
                }
                
                tbData.setItems(list);
                
                total.setText("Tổng số: "+(number-1));
                sort.setText("Sắp xếp");
                statusFilter.setText("Lọc theo trạng thái");
                locationFilter.setText("Lọc theo địa điểm");
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
        
        btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            Business.homeStage = Business.back(getClass().getResource("../frame/Home.fxml"), "Trang chủ");
            Business.closeWindow(event);
        });
    }    
}
