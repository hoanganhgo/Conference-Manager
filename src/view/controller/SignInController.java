package view.controller;

import dao.Business;
import entity.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class SignInController implements Initializable {
    @FXML
    private TextField username;
    
    @FXML
    private PasswordField password;
    
    @FXML
    private Label notify;
    
    @FXML
    private Button login;   
    
    private Button signIn;
    
    private Button register;
    
    private Label label;
    
    private MenuButton persional;
    
    private MenuItem information;
    
    private MenuItem logout;
    
    private Button myConference;
    
    private MenuButton admin;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        login.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            login.setStyle("-fx-background-color: #ff9900;");
            //Kiểm tra hợp lệ
            String notifyContent=null;
            
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
            
            notify.setText("");            
            
            //Kiểm tra thông tin đăng nhập
            User user=Business.login(username.getText(), password.getText());
            
            if (user==null){
                notify.setText("Sai tài khoản mật khẩu!");
                username.requestFocus();
                return;
            }else{
                notify.setText("");
                Business.authenticator=user;
                
                //Đóng cửa sổ đăng nhập
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
                
                //Hiển thị alert thông báo
                Business.alertInformation("Đăng nhập", "Đăng nhập thành công!");
                
                //Xử lý sau khi đăng nhập
                signIn.setVisible(false);
                register.setVisible(false);
                label.setVisible(false);
                
                persional.setVisible(true);
                persional.setText("Xin chào! "+Business.detachName(user.getName()));
                myConference.setVisible(true);
                if (user.getAdmin()==1){
                    admin.setVisible(true);
                }

            }
        });
        
        login.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)->{
            login.setStyle("-fx-background-color: #ff5500;");
        });
    }       
    
    
    public void transferMessage(Button login, Button register, Label label, MenuButton persional, MenuItem information, MenuItem logout, Button myConference, MenuButton admin){
        this.signIn=login;
        this.register=register;
        this.label=label;
        this.persional=persional;
        this.information=information;
        this.logout=logout;
        this.myConference=myConference;
        this.admin=admin;
    }
}
