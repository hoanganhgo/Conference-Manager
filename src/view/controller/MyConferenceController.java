package view.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    }    
    
}
