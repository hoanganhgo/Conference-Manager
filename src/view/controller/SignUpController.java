package view.controller;

import dao.Business;
import entity.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SignUpController implements Initializable {
    @FXML
    private TextField name;
    
    @FXML
    private TextField email;
    
    @FXML
    private TextField username;
    
    @FXML
    private PasswordField password;
    
    @FXML
    private PasswordField rePassword;
    
    @FXML
    private Label notify;
    
    @FXML
    private Button register;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Business.saveUser(new User());
        register.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            register.setStyle("-fx-background-color: #ff9900;");            
            //Kiểm tra hợp lệ
            String notifyContent=null;
            
            notifyContent = Business.checkNameField(name.getText());
            if (notifyContent!=null){
                notify.setText(notifyContent);
                name.requestFocus();
                return;
            }

            notifyContent = Business.checkEmailField(email.getText());
            if (notifyContent!=null)
            {
                notify.setText(notifyContent);
                email.requestFocus();
                return;
            }           
            
            notifyContent=Business.checkUserNameField(username.getText());
            if (notifyContent!=null){
                notify.setText(notifyContent);
                username.requestFocus();
                return;
            }

            notifyContent=Business.checkPasswordField(password.getText());
            if (notifyContent!=null){
                notify.setText(notifyContent);
                password.requestFocus();
                return;
            }

            if (!password.getText().equals(rePassword.getText())){
                notify.setText("Mật khẩu không khớp");
                rePassword.setText("");
                password.setText("");
                password.requestFocus();
                return;
            }

            notify.setText("");
            
            //Lưu thông tin người dùng vào CSDL
            String passwordHash=Business.hashMD5(password.getText());
            User user=new User(name.getText(), username.getText(), passwordHash, (byte)0, email.getText(), (byte)1);
            boolean saved = Business.saveUser(user);
            
            if (saved){
                //Hiển thị thông báo đăng ký thành công
                Business.alertInformation("Đăng ký", "Đăng ký tài khoản thành công!");
                
                //Đóng cửa sổ đăng ký
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close(); 
            }else{
                //Hiển thị thông báo đăng ký thất bại
                Business.alertError("Đăng ký", "Tài khoản đã tồn tại");
            }
        });
        
        
        register.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            register.setStyle("-fx-background-color: #ff5500;");
        });
    }    
    
}
