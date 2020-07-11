package view.controller;

import dao.Business;
import entity.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class InformationController implements Initializable {
    @FXML
    private TextField name;
    
    @FXML
    private Button editName;
    
    @FXML
    private ImageView imageName;
    
    @FXML
    private Label noteName;
    
    @FXML
    private TextField email;
    
    @FXML
    private Button editEmail;
    
    @FXML
    private ImageView imageEmail;
    
    @FXML
    private Label noteEmail;
    
    @FXML
    private TextField username;
    
    @FXML
    private Label labelPassword;
    
    @FXML
    private TextField password;
    
    @FXML
    private Button editPassword;
    
    @FXML
    private Label notePassword;
    
    @FXML
    private Label labelNewPassword;
    
    @FXML
    private TextField newPassword;
    
    @FXML
    private Label labelRePassword;
    
    @FXML
    private TextField rePassword;
    
    @FXML
    private Button confirmPassword;
    
    private MenuButton persional;
    
    private Stage stage; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int userId = Business.authenticator.getUserId();
        User user=Business.getUserInfo(userId);
        
        //Khởi tạo giao diện
        name.setText(user.getName());
        email.setText(user.getEmail());
        username.setText(user.getUsername());
        name.setFocusTraversable(false);
        
        //Sự kiện thay đổi tên
        editName.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            if (name.isEditable()){
                //Kiểm tra tên hợp lệ ko?
                String result=Business.checkNameField(name.getText());
                if (result!=null){
                    Business.alertError("Thay đổi tên", result);
                    name.requestFocus();
                    editName.setStyle("-fx-background-color: #F2F2F2;");
                    return;
                }
                
                //Update DB
                Business.updateName(userId, name.getText());
                
                //Thông báo
                Business.alertInformation("Thay đổi tên", "Thay đổi tên thành công!");
                
                name.setEditable(false);
                imageName.setImage(new Image("././images/edit.png"));
                noteName.setText("Chỉnh sửa tên");
                
                //Cập nhật
                Business.authenticator.setName(name.getText());
                this.persional.setText("Xin chào! "+Business.detachName(name.getText()));
            }else{
                name.setEditable(true);
                imageName.setImage(new Image("././images/save.png"));
                noteName.setText("Lưu lại");
            }

        editName.setStyle("-fx-background-color: #F2F2F2;");
        });
        
        editName.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            editName.setStyle("-fx-background-color: #ff9900;");
        });
        
        //Sự kiện thay đổi Email
        editEmail.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            if (email.isEditable()){
                //Kiểm tra Email hợp lệ không?
                String result=Business.checkEmailField(email.getText());
                if (result!=null){
                    Business.alertError("Thay đổi Email", result);
                    email.requestFocus();
                    editEmail.setStyle("-fx-background-color: #F2F2F2;");
                    return;
                }
                
                //Update DB
                Business.updateEmail(userId, email.getText());
                
                //Thông báo
                Business.alertInformation("Thay đổi Email", "Thay đổi Email thành công!");
                
                email.setEditable(false);
                imageEmail.setImage(new Image("././images/edit.png"));
                noteEmail.setText("Chỉnh sửa Email");
            }else{
                email.setEditable(true);
                imageEmail.setImage(new Image("././images/save.png"));
                noteEmail.setText("Lưu lại");
            }
            
            editEmail.setStyle("-fx-background-color: #F2F2F2;");
        });
        
        editEmail.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            editEmail.setStyle("-fx-background-color: #ff9900;");
        });
        
        //Cài đặt sự kiện thay đổi mật khẩu
        editPassword.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            editPassword.setStyle("-fx-background-color: #F2F2F2;");
            
            stage.setHeight(425f);
            
            labelPassword.setText("Mật khẩu cũ");
            labelPassword.setFont(Font.font(null, FontWeight.BOLD, 12));
            labelPassword.setLayoutY(225f);
            password.setText("");
            password.setEditable(true);
            password.requestFocus();
            editPassword.setVisible(false);
            notePassword.setVisible(false);
            
            labelNewPassword.setText("Mật khẩu mới");
            labelNewPassword.setLayoutY(272f);
            labelNewPassword.setVisible(true);
            newPassword.setVisible(true);
            
            labelRePassword.setText("Nhập lại\nmật khẩu mới");
            labelRePassword.setVisible(true);
            rePassword.setVisible(true);
            
            confirmPassword.setVisible(true);
        });
        
        editPassword.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            editPassword.setStyle("-fx-background-color: #ff9900;");
        });
        
        //Cài đặt sự kiện xác nhận thay đổi mật khẩu
        confirmPassword.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            confirmPassword.setStyle("-fx-background-color: #ff9900;");
            
            //Kiểm tra mật khẩu hợp lệ
            String result=Business.checkPasswordField(password.getText());
            if (result!=null){
                Business.alertError("Thay đổi mật khẩu", result);
                password.requestFocus();
                return;
            }
            
            result=Business.checkPasswordField(newPassword.getText());
            if (result!=null){
                Business.alertError("Thay đổi mật khẩu", result);
                newPassword.requestFocus();
                return;
            }
            
            if (!newPassword.getText().equals(rePassword.getText())){
                Business.alertError("Thay đổi mật khẩu", "Mật khẩu không khớp");
                newPassword.setText("");
                rePassword.setText("");
                newPassword.requestFocus();
                return;
            }
            
            //Update DB
            boolean isUpdated=Business.updatePassword(userId, password.getText(), newPassword.getText());
            
            if (isUpdated){
                Business.alertInformation("Thay đổi Mật khẩu", "Thay đổi Mật khẩu thành công!");
            }else{
                Business.alertError("Thay đổi mật khẩu", "Mật khẩu cũ không đúng! Vui lòng kiểm tra lại.");
                return;
            }

            stage.setHeight(340);
            
            labelPassword.setText("Mật khẩu");
            labelPassword.setFont(Font.font(null, FontWeight.BOLD, 18));
            labelPassword.setLayoutY(220f);
            password.setText("**********");
            password.setEditable(false);
            
            editPassword.setVisible(true);
            notePassword.setVisible(true);
            
            labelNewPassword.setVisible(false);
            newPassword.setVisible(false);
            newPassword.setText("");
            
            labelRePassword.setVisible(false);
            rePassword.setVisible(false);
            rePassword.setText("");
            
            confirmPassword.setVisible(false);
        });
        
        confirmPassword.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            confirmPassword.setStyle("-fx-background-color: #ff5500;");
        });
    }    
    
    public void transfer(Stage stage, MenuButton persional)
    {
        this.stage=stage;
        this.persional=persional;
    }
}
