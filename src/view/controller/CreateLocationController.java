package view.controller;

import dao.Business;
import entity.Location;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CreateLocationController implements Initializable {
    @FXML
    private TextField name;
    
    @FXML
    private TextArea address;
    
    @FXML
    private TextField size;
    
    @FXML
    private Button confirm;
    
    private Location choice;
    private TextArea locationBox;
    private TextField sizeField;
    private Stage locationScreen;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            String nameString=name.getText();
            String addressString=address.getText();
            int sizeInt=-1;
            
            if (nameString.isEmpty()){
                Business.alertError("Lỗi", "Tên địa điểm không được bỏ trống");
                return;
            }
            
            if (addressString.isEmpty()){
                Business.alertError("Lỗi", "Địa chỉ không được bỏ trống");
                return;
            }
            
            try{
                sizeInt=Integer.parseInt(size.getText());
            }catch (NumberFormatException e){
                Business.alertError("Lỗi", "Sức chứa phải là số");
                return;
            }
            
            int id = Business.addLocation(new Location(nameString, addressString, sizeInt));
            
            //Cập nhật địa điểm
            System.out.println("ID="+id);
            choice.setLocationId(id);
            choice.setName(nameString);
            choice.setAdress(addressString);
            choice.setSize(sizeInt);
            locationBox.setText(nameString+", "+addressString);
            sizeField.setText(size.getText());
            
            //Đóng cửa sổ
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            this.locationScreen.close();
        });
    }    
    
    public void transfer(Stage stage, Location choice, TextArea locationBox, TextField size){
        this.locationScreen=stage;
        this.choice=choice;
        this.locationBox=locationBox;
        this.sizeField=size;
    }
    
}
