package view.controller;

import dao.Business;
import entity.Location;
import entity.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import view.model.ManageMeetingModel;
import view.model.ManageUserModel;

public class ManageUserController implements Initializable {
    @FXML
    private TextField searchBox;
    
    @FXML
    private Button search;
    
    @FXML
    private TableView<ManageUserModel> tbData;
    
    @FXML
    public TableColumn<ManageUserModel, Integer> number;
    
    @FXML
    public TableColumn<ManageUserModel, String> fullname;
    
    @FXML
    public TableColumn<ManageUserModel, String> username;
    
    @FXML
    public TableColumn<ManageUserModel, String> email;
    
    @FXML
    public TableColumn<ManageUserModel, Integer> numberOfConference;
    
    @FXML
    public TableColumn<ManageUserModel, Button> lock;
    
    @FXML
    private Label total;
    
    @FXML
    private Button btnBack;
    
    @FXML
    private MenuButton sort;
    
    @FXML
    private MenuItem descrease;
    
    @FXML
    private MenuItem increase;
    
    @FXML
    private MenuItem sortname;
    
    @FXML
    private MenuButton filter;
    
    @FXML
    private MenuItem unlocked;
    
    @FXML
    private MenuItem locked;
    
    @FXML
    private MenuItem allStatus;
    
    @FXML
    private MenuButton searchFilter;
    
    @FXML
    private MenuItem searchName;    //type=1;
    
    @FXML
    private MenuItem searchUsername;    //type=2
    
    @FXML
    private MenuItem searchEmail;     //type=3
    
    private int searchType=1;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        numberOfConference.setCellValueFactory(new PropertyValueFactory<>("numberOfConference"));
        lock.setCellValueFactory(new PropertyValueFactory<>("lock"));
       
        //Vô hiệu hóa sorting ở các cột
        number.setSortable(false);
        fullname.setSortable(false);
        username.setSortable(false);
        email.setSortable(false);
        numberOfConference.setSortable(false);
        lock.setSortable(false);
        
        //Căn trái cột tên hội nghị
        fullname.setStyle("-fx-alignment: CENTER-LEFT;");
        username.setStyle("-fx-alignment: CENTER-LEFT;");
        email.setStyle("-fx-alignment: CENTER-LEFT;");
        
        //Lấy danh sách users
        List<User> users=Business.getAllUsers();
        
        //Chuyển danh sách users sang dạng model
        ArrayList<ManageUserModel> models=new ArrayList<>();
        for (User e : users){
            Integer numConferences=Business.countConference(e.getUserId());
            models.add(new ManageUserModel(e.getUserId(), 0, e.getName(), e.getUsername(), numConferences, e.getEmail(), e.getActive(), e.getAdmin()));
        }
        
        //Sắp xếp theo tên
        sortName(models);
        
        //Tạo danh sách hiển thị
        ObservableList<ManageUserModel> list=FXCollections.observableArrayList();
        int num=1;
        
        for (ManageUserModel e : models){
            e.setNumber(num++);
            list.add(e);
        }
        total.setText("Tổng số: "+(num-1));
        tbData.setItems(list);
        
        descrease.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.clear();
                System.out.println("descrease");
                sortNumberOfConferenceDESC(models);
                int num=1;
                for (ManageUserModel e : models){
                    e.setNumber(num++);
                    list.add(e);
                }
                
                tbData.setItems(list);
                sort.setText("Giảm dần số lượt tham dự");
                filter.setText("Tất cả");
            }
        });
        
        increase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.clear();
                System.out.println("increase");
                sortNumberOfConferenceASC(models);
                int num=1;
                for (ManageUserModel e : models){
                    e.setNumber(num++);
                    list.add(e);
                }
                
                tbData.setItems(list);
                sort.setText("Tăng dần số lượt tham dự");
                filter.setText("Tất cả");
            }
        });
        
        sortname.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.clear();
                
                sortName(models);
                int num=1;
                for (ManageUserModel e : models){
                    e.setNumber(num++);
                    list.add(e);
                }
                
                tbData.setItems(list);
                sort.setText("Sắp xếp");
            }
        });
        
        unlocked.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.clear();
                
                int num=1;
                for (ManageUserModel e : models)
                {
                    if (e.getActive()==1){
                        e.setNumber(num++);
                        list.add(e);
                    }
                }
                
                tbData.setItems(list);
                filter.setText("Không chặn");
                sort.setText("Sắp xếp");
            }
        });
        
        locked.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.clear();
                
                int num=1;
                for (ManageUserModel e : models)
                {
                    if (e.getActive()==0){
                        e.setNumber(num++);
                        list.add(e);
                    }
                }
                
                tbData.setItems(list);
                filter.setText("Bị chặn");
                sort.setText("Sắp xếp");
            }
        });
        
        allStatus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.clear();
                
                sortName(models);
                int num=1;
                for (ManageUserModel e : models){
                    e.setNumber(num++);
                    list.add(e);
                }
                
                tbData.setItems(list);
                filter.setText("Tất cả");
                sort.setText("Sắp xếp");
            }
        });
        
        searchName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchType=1;
                searchFilter.setText("Họ và Tên");
                searchBox.setPromptText("Tìm kiếm theo Họ và Tên");
            }
        });
        
        searchUsername.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchType=2;
                searchFilter.setText("Username");
                searchBox.setPromptText("Tìm kiếm theo Username");
            }
        });
        
        searchEmail.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchType=3;
                searchFilter.setText("Email");
                searchBox.setPromptText("Tìm kiếm theo Email");
            }
        });
        
        search.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            list.clear();                    
            int number=1;
            String content=searchBox.getText().toLowerCase();
            switch (searchType){
                case 1:
                    for (ManageUserModel e : models){
                        if (e.getFullname().toLowerCase().contains(content)){
                            list.add(e);
                        }
                    }
                    break;
                case 2:
                    for (ManageUserModel e : models){
                        if (e.getUsername().toLowerCase().contains(content)){
                            list.add(e);
                        }
                    }
                    break;
                case 3:
                    for (ManageUserModel e : models){
                        if (e.getEmail().toLowerCase().contains(content)){
                            list.add(e);
                        }
                    }
                    break;
            }
            
            tbData.setItems(list);
            filter.setText("Tất cả");
            sort.setText("Sắp xếp");
        });
        
        btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            Business.homeStage = Business.back(getClass().getResource("../frame/Home.fxml"), "Trang chủ");
            Business.closeWindow(event);
        });
    }    
    
    
    private void sortName(ArrayList<ManageUserModel> models)
    {
        int n=models.size();
        for (int i=0;i<n-1;i++){
            int min=i;
            for (int j=i+1;j<n;j++){
                //Chuyển 'Đ' lên dưới chữ 'D'
                if (models.get(j).getName().compareTo("Zzz")>0){
                    if (models.get(min).getName().compareTo("Dzz")>0){
                        min=j;
                    }
                }else if (models.get(min).getName().compareTo("Zzz")>0){
                    if (models.get(j).getName().compareTo("Dzz")<0){
                        min=j;
                    }
                }else if (models.get(min).getName().compareTo(models.get(j).getName())>0){
                    min=j;
                }
            }
            if (min!=i){
                ManageUserModel temp=models.get(min);
                models.set(min, models.get(i));
                models.set(i, temp);
            }
        }
    }
    
    private void sortNumberOfConferenceDESC(ArrayList<ManageUserModel> models)
    {
        int n=models.size();
        for (int i=0;i<n-1;i++){
            int max=i;
            for (int j=i+1;j<n;j++){
                if (models.get(max).getNumberOfConference()<models.get(j).getNumberOfConference())
                {
                    max=j;
                }
            }
            if (max!=i){
                ManageUserModel temp=models.get(max);
                models.set(max, models.get(i));
                models.set(i, temp);
            }
        }
    }
    
    private void sortNumberOfConferenceASC(ArrayList<ManageUserModel> models)
    {
        int n=models.size();
        for (int i=0;i<n-1;i++){
            int min=i;
            for (int j=i+1;j<n;j++){
                if (models.get(min).getNumberOfConference()>models.get(j).getNumberOfConference())
                {
                    min=j;
                }
            }
            if (min!=i){
                ManageUserModel temp=models.get(min);
                models.set(min, models.get(i));
                models.set(i, temp);
            }
        }
    }
    
}
