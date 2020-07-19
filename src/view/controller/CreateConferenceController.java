package view.controller;

import dao.Business;
import entity.Location;
import entity.Meeting;
import java.io.File;
import static java.io.FileDescriptor.out;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CreateConferenceController implements Initializable {
    @FXML
    private TextField name;
    
    @FXML
    private Button chooseLocation;
    
    @FXML
    private TextArea location;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private TextField hour;
    
    @FXML
    private TextField minute;
    
    @FXML
    private TextArea shortDescription;
    
    @FXML
    private TextArea longDescription;
    
    @FXML
    private ImageView image;
    
    @FXML
    private Button chooseImage;
    
    @FXML
    private Button create;
    
    @FXML
    private TextField size;
    
    private FileChooser fileChooser=new FileChooser();
    
    private Path imagePath=null;
    
    private String imageString=null;
    
    private Location choice=new Location(-1);
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Cài đặt sự kiện chọn địa điểm tổ chức
        chooseLocation.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            Parent frame=null;
            FXMLLoader loader=null;
            try {
                loader = new FXMLLoader(getClass().getResource("../frame/Location.fxml"));
                frame = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(CreateConferenceController.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            
            //Gửi thông điệp
            LocationController locationController=loader.getController();
            locationController.transfer(choice, location, size);           
            
            //Khởi tạo màn hình Danh sách địa điểm tổ chức hội nghị
            Stage listLocation=new Stage();
            listLocation.setTitle("Chọn Địa Điểm Tổ Chức Hội Nghị");
            Scene scene=new Scene(frame, 1280, 700);
            listLocation.setScene(scene);
            listLocation.setResizable(false);
            listLocation.centerOnScreen();
            
            listLocation.show();
            
            chooseLocation.setStyle("-fx-background-color: #ff9900;");
        });

        //Cài đặt sự kiện chọn hình từ máy tính
        chooseImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            Stage stage=(Stage)(((Button)event.getSource()).getScene().getWindow());
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.tiff", "*.bmp", "*.svg", "*.jpeg"));
            File file=fileChooser.showOpenDialog(stage);
            if (file!=null){
                //Kiểm tra tỷ lệ file được chọn có phù hợp hay không?
                Image imageFile=new Image(file.toURI().toString());
                if (imageFile.getWidth()/imageFile.getHeight()>2){
                    Business.alertError("Chọn file hình ảnh", "Không chấp nhận File hình ảnh có tỷ lệ Width/Height>2\n Vui lòng chọn file khác!");
                    return;
                }
                
                //Kiểm tra xem đã tồn tại file hình ảnh chưa, nếu có thì xóa đi
                if (imagePath!=null){
                    try {
                        Files.delete(imagePath);
                    } catch (IOException ex) {
                        Logger.getLogger(CreateConferenceController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                int i=file.getName().lastIndexOf('.');
                String extension=file.getName().substring(i);
                String unique=String.valueOf((new Date()).getTime());
                File destination=new File("avatar/"+unique+extension);
                try {
                    Files.copy(file.toPath(), destination.toPath());
                } catch (IOException ex) {
                    Logger.getLogger(CreateConferenceController.class.getName()).log(Level.SEVERE, null, ex);
                }

                Image imageDestination=new Image(destination.toURI().toString());
                image.setImage(imageDestination);
                imagePath=destination.toPath();
                imageString=destination.getPath();
                
                //Di chuyển ảnh ra vị trí trung tâm
                double scale=imageFile.getHeight()/245;
                double width=imageFile.getWidth()/scale;
                double height=imageFile.getHeight()/scale;
                double x=(485-width)/2;
                image.setLayoutX(749+x);
            }
        });
        
        //Cài đặt sự kiện tạo hội nghị
        create.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            //Kiểm tra hợp lệ
            if (name.getText().isEmpty()){
                Business.alertError("Tạo hội nghị", "Tên hội nghị không được bỏ trống");
                return;
            }
            
            if (choice.getLocationId()<0){
                Business.alertError("Tạo hội nghị", "Địa điểm tổ chức không được bỏ trống");
                return;
            }
            
            if (datePicker.getValue()==null){
                Business.alertError("Tạo hội nghị", "Ngày/Tháng/Năm không được bỏ trống");
                return;
            }
            
            int hourInt=-1;
            int minuteInt=-1;
            try{
                hourInt = Integer.parseInt(hour.getText());
                minuteInt = Integer.parseInt(minute.getText());
            }catch(NumberFormatException e){
                Business.alertError("Tạo hội nghị", "Thời gian không hợp lệ");
                return;
            }
            
            if (shortDescription.getText().isEmpty()){
                Business.alertError("Tạo hội nghị", "Miêu tả ngắn không được bỏ trống");
                return;
            }
            
            if (longDescription.getText().isEmpty()){
                Business.alertError("Tạo hội nghị", "Miêu tả chi tiết không được bỏ trống");
                return;
            }
            
            if (imageString==null){
                Business.alertError("Tạo hội nghị", "Bạn chưa chọn hình đại diện");
                return;
            }
            
            //Khởi tạo đối tượng thời gian
            LocalDate a = datePicker.getValue();
            Date date=new Date(a.getYear()-1900, a.getMonthValue(), a.getDayOfMonth(), hourInt, minuteInt);
            
            //Khởi tạo đối tượng vị trí
            Location location=new Location();
            location.setLocationId(1);
            Meeting meeting = new Meeting(choice, name.getText(), shortDescription.getText(), longDescription.getText(), imageString, date);
            Business.createConference(meeting);
            
            //Thông báo
            Business.alertInformation("Tạo hội nghị", "Tạo hội nghị thành công!");
            
            //Đóng cửa sổ
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        });
        
    }    
    
}
