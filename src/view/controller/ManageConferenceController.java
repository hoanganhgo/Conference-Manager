package view.controller;

import dao.Business;
import entity.Location;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import view.model.ManageMeetingModel;
import view.model.MeetingModel;

public class ManageConferenceController implements Initializable {

    @FXML
    private TableView<ManageMeetingModel> tbData;
    
    @FXML
    public TableColumn<ManageMeetingModel, Integer> number;
    
    @FXML
    public TableColumn<ManageMeetingModel, String> name;
    
    @FXML
    public TableColumn<ManageMeetingModel, String> time;
    
    @FXML
    public TableColumn<ManageMeetingModel, Button> requirement;
    
    @FXML
    public TableColumn<ManageMeetingModel, Button> attended;
    
    @FXML
    public TableColumn<ManageMeetingModel, Button> edit;
    
    @FXML
    private Label total;
    
    @FXML
    private Button search;
    
    @FXML
    private TextField searchBox;
    
    @FXML
    private Button createConference;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        requirement.setCellValueFactory(new PropertyValueFactory<>("requirement"));
        attended.setCellValueFactory(new PropertyValueFactory<>("attended"));
        edit.setCellValueFactory(new PropertyValueFactory<>("edit"));
       
        //Vô hiệu hóa sorting ở các cột
        number.setSortable(false);
        name.setSortable(false);
        time.setSortable(false);
        requirement.setSortable(false);
        edit.setSortable(false);
        
        //Căn trái cột tên hội nghị
        name.setStyle("-fx-alignment: CENTER-LEFT;");
        
        //Lấy danh sách hội nghị
        List<Object[]> meetings = Business.getAllMeetings();
       
        //Tạo danh sách hiển thị
        ObservableList<ManageMeetingModel> list=FXCollections.observableArrayList();
        int num=1;
        
        for (Object[] e : meetings){           
            list.add(new ManageMeetingModel((int)e[0], num++, e[1].toString(), (Date)e[2]));
        }
        
        //Cài đặt sự kiện khi double click vào hội nghị
        tbData.setOnMouseClicked((MouseEvent event)->{
            if (event.getClickCount()>=2){
                //Lấy dữ liệu hội nghị
                ManageMeetingModel data=tbData.getSelectionModel().getSelectedItem();
                               
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
                detailController.transferMessage(data.getId(), data.getName(), data.getDate(), location, size, Business.getShortDescription(data.getId(), meetings), longDescription, avatar);
                detailController.setVisibleRegister(false);
                
                //Khởi tạo frame
                Stage detail=new Stage();
                detail.setTitle("Chi tiết hội nghị");
                
                Scene scene=new Scene(frame, 1280,700);
                detail.setScene(scene);
                detail.setResizable(false);
                detail.centerOnScreen();
                
                detail.show();
            }
        });
        
        tbData.setItems(list);
        
        total.setText("Tổng số: "+list.size());
        
        //Cài đặt sự kiện tìm kiếm
        search.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            search.setStyle("-fx-background-color: #ff9900;");
          
            String content=searchBox.getText().toLowerCase();
            list.add(new ManageMeetingModel(1, 1, "", new Date()));
            list.clear();  //Xóa dữ liệu trên giao diện
            
            int number=0;
            for (Object[] e : meetings){
                String name=e[1].toString().toLowerCase();
                String location=((Location)e[4]).getName().toLowerCase();
                String description=e[3].toString().toLowerCase();
                
                if (name.contains(content) || location.contains(content) || description.contains(content)){           
                    //Thêm hội nghị vào giao diện
                    number+=1;
                    list.add(new ManageMeetingModel((int)e[0], number, e[1].toString(), (Date)e[2]));
                }
            }
            
            tbData.setItems(list);
            
            total.setText("Tổng số: "+number);
        });
        
        //Tạo sự kiện tạo hội nghị mới
        createConference.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            Parent frame=null;
            try {
                frame = FXMLLoader.load(getClass().getResource("../frame/CreateConference.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
                        
            Stage signUp=new Stage();
            signUp.setTitle("Tạo hội nghị mới");
            Scene scene=new Scene(frame, 1280, 700);
            signUp.setScene(scene);
            signUp.setResizable(false);
            signUp.centerOnScreen();
                        
            signUp.show();
            
            createConference.setStyle("-fx-background-color: #ff9900;");
        });
        
        createConference.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            createConference.setStyle("-fx-background-color: #ff5500;");
        });
    }    
    
}
