package view.controller;

import dao.Business;
import entity.Attendance;
import entity.User;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import view.model.ManageMeetingModel;
import view.model.UserModel;

public class ApprovalController implements Initializable {
    @FXML
    private Label nameOfConference;
    
    @FXML
    private TableView<UserModel> tbData;
    
    @FXML
    public TableColumn<UserModel, Integer> number;
    
    @FXML
    public TableColumn<UserModel, String> fullname;
    
    @FXML
    public TableColumn<UserModel, String> username;
    
    @FXML
    public TableColumn<UserModel, String> email;
    
    @FXML
    public TableColumn<UserModel, Button> approval;
    
    @FXML
    public TableColumn<UserModel, Button> noApproval;
    
    @FXML
    private Label total;
    
    @FXML
    private Label participants;
    
    @FXML
    private Label size;
    
    @FXML
    private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        approval.setCellValueFactory(new PropertyValueFactory<>("approval"));
        noApproval.setCellValueFactory(new PropertyValueFactory<>("noApproval"));
       
        //Vô hiệu hóa sorting ở các cột
        number.setSortable(false);
        fullname.setSortable(false);
        username.setSortable(false);
        email.setSortable(false);
        approval.setSortable(false);
        noApproval.setSortable(false);
        
        //Căn trái cột tên hội nghị
        fullname.setStyle("-fx-alignment: CENTER-LEFT;");
        username.setStyle("-fx-alignment: CENTER-LEFT;");
        email.setStyle("-fx-alignment: CENTER-LEFT;");        
        
        btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            Business.closeWindow(event);
        });
    }    
    
    public void transferData(int meetingId, String name,int size){        
        int[] participants=new int[1];
        participants[0]=Business.countParticipants(meetingId, 1);

        //Lấy danh sách hội nghị
        List<Object[]> attendances = Business.getUserJoinAttendance(meetingId);
        
        //Tạo danh sách hiển thị
        ObservableList<UserModel> list=FXCollections.observableArrayList();
        int[] num=new int[1];
        num[0]=1;
        
        for (Object[] a : attendances){     
            int id=(int)a[0];
            String fullname=a[1].toString();
            String username=a[2].toString();
            String email=a[3].toString();
            list.add(new UserModel(id, meetingId, num[0]++, fullname, username, email, size, participants, num, this.participants, total));
        }
        
        tbData.setItems(list);
        
        this.nameOfConference.setText(name);
        this.size.setText("Tối đa: "+size);
        
        this.participants.setText("Số người đã tham dự: "+participants[0]);
        this.total.setText("Tổng số: "+(num[0]-1));
    }
}
