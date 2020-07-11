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
import view.model.MeetingModel;

public class MyConferenceController implements Initializable {

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
    public Label total;
    
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
        
        //Lấy danh sách hội nghị của tôi
        List<Object[]> meetings=Business.getMyConferences(Business.authenticator.getUserId());
        
        //Tạo danh sách hiển thị
        ObservableList<MeetingModel> list=FXCollections.observableArrayList();
        int num=1;
        
        for (Object[] e : meetings)
        {
            String status = Business.checkStatus((int)e[5]);
            String dateTime=Business.formatDateTime((Date)e[2]);
            
            //Thêm hội nghị vào giao diện
            list.add(new MeetingModel((int)e[0], num++, e[1].toString(), dateTime, status, e[3].toString()));
        }
        
        //Cài đặt sự kiện khi double click vào hội nghị
        tbData.setOnMouseClicked((MouseEvent event)->{
            if (event.getClickCount()>=2){
                //Lấy dữ liệu hội nghị
                MeetingModel data=tbData.getSelectionModel().getSelectedItem();
                
                //Lấy vị trí được double click
                Integer pos=data.getNumber()-1;
                
                //Lấy thời gian
                Date date=(Date)meetings.get(pos)[2];
                
                //Lấy đối tượng vị trí
                Location ln=(Location)meetings.get(pos)[4];
                
                //Tạo chuỗi địa điểm
                String location=ln.getName()+", "+ln.getAdress();
                
                //Lấy kích thước hội nghị
                int size=ln.getSize();
                
                //Lấy dữ liệu miêu tả và hình ảnh
                Object[] objects=Business.getMeetingDetail(data.getId());
                String longDescription=objects[0].toString();
                String avatar=objects[1].toString();
                
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
                detailController.transferMessage(data.getId(), data.getName(), date, location, size, data.getDescriptionContent(), longDescription, avatar);
                detailController.setVisibleRegister(false);
                
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
        
        total.setText("Tổng số: "+list.size());
        
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
                    String status = Business.checkStatus((int)e[5]);
                    String dateTime=Business.formatDateTime((Date)e[2]);
            
                    //Thêm hội nghị vào giao diện
                    list.add(new MeetingModel((int)e[0], number++, e[1].toString(), dateTime, status, e[3].toString()));
                }
                    
                
            }
            
            tbData.setItems(list);
        });
        
        search.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            search.setStyle("-fx-background-color: #ff5500;");
        });
    }    
    
}
