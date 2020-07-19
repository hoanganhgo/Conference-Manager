package view.controller;

import dao.Business;
import entity.Location;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import view.model.LocationModel;
import view.model.MeetingModel;


public class LocationController implements Initializable {
    @FXML
    private TableView<LocationModel> tbData;
    
    @FXML
    public TableColumn<LocationModel, Integer> number;
    
    @FXML
    public TableColumn<LocationModel, String> name;
    
    @FXML
    public TableColumn<LocationModel, String> address;
    
    @FXML
    public TableColumn<LocationModel, String> size;
    
    @FXML
    public TableColumn<LocationModel, Button> choose;
    
    @FXML
    private Button addLocation; 
    
    @FXML
    private TextField searchBox;
    
    @FXML
    private Button search;
    
    private Location choice;
    private TextArea locationBox;
    private TextField sizeField;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        choose.setCellValueFactory(new PropertyValueFactory<>("choose"));
       
        //Vô hiệu hóa sorting ở các cột
        number.setSortable(false);
        name.setSortable(false);
        address.setSortable(false);
        size.setSortable(false);
        choose.setSortable(false);
        
        //Căn trái cột tên hội nghị
        name.setStyle("-fx-alignment: CENTER-LEFT;");
        
        //Lấy tất cả địa điểm
        List<Location> data=Business.getAllLocation();
        
        //Tạo danh sách hiển thị
        ObservableList<LocationModel> list=FXCollections.observableArrayList();
        ArrayList<LocationModel> models=new ArrayList<>();
        
        int num=1;
        for (Location location : data){
            Button choose=new Button();
            choose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
                choice.setLocationId(location.getLocationId());
                choice.setName(location.getName());
                choice.setAdress(location.getAdress());
                choice.setSize(location.getSize());            
                locationBox.setText(location.getName()+", "+location.getAdress());
                sizeField.setText(location.getSize().toString());
                
                //Đóng cửa sổ
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            });
            
            models.add(new LocationModel(num++, location.getLocationId(), location.getName(), location.getAdress(), location.getSize(), choose));
        }
        list.addAll(models);
        tbData.setItems(list);
        
        //Cài đặt sự kiện tìm kiếm địa chỉ
        search.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            list.clear();
            int number=1;
            String content=searchBox.getText().toLowerCase();
            for (LocationModel e : models){
                if (e.getName().toLowerCase().contains(content) || e.getAddress().toLowerCase().contains(content)){
                    e.setNumber(number++);
                    list.add(e);
                }
            }
            
            tbData.setItems(list);
        });
        
        //Cài đặt sự kiện thêm địa điểm
        addLocation.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            Parent frame=null;
            FXMLLoader loader=null;
            try {
                loader = new FXMLLoader(getClass().getResource("../frame/CreateLocation.fxml"));
                frame = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(LocationController.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            
            //Gửi dữ liệu
            Stage stage=(Stage)(((Button)event.getSource()).getScene().getWindow());
            CreateLocationController createLocationController=loader.getController();
            createLocationController.transfer(stage, choice, locationBox, sizeField);
            
            //Khởi tạo màn hình đăng ký
            Stage addLocation=new Stage();
            addLocation.setTitle("Thêm địa điểm");
            Scene scene=new Scene(frame, 600, 300);
            addLocation.setScene(scene);
            addLocation.setResizable(false);
            addLocation.centerOnScreen();
            
            addLocation.show();
        });
    }    
    
    public void transfer(Location choice, TextArea locationBox, TextField size){
        this.choice=choice;
        this.locationBox=locationBox;
        this.sizeField=size;
    }
    
}
