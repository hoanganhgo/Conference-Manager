package view.model;

import dao.Business;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ManageUserModel {
    private int id;
    private int number;
    private String name;
    private String fullname;
    private String username;
    private String email;
    private Integer numberOfConference;
    private int active;
    private Button lock;

    public ManageUserModel(int id, int number, String fullname, String username, Integer numberOfConference, String email, int active, int admin) {
        this.id = id;
        this.number = number;
        this.fullname = fullname;
        this.name=fullname.substring(fullname.lastIndexOf(' ')+1);
        this.username = username;
        this.email = email;
        this.numberOfConference=numberOfConference;
        this.active=active;
        this.lock = new Button();
        Image unlock=new Image("./././images/unlock.png");
        Image lock=new Image("./././images/lock.png");
        final ImageView imageView=new ImageView();
        if (active==1){
            imageView.setImage(unlock);
        }else{
            imageView.setImage(lock);
        }
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        this.lock.setGraphic(imageView);
        
        this.lock.addEventHandler((MouseEvent.MOUSE_CLICKED), (MouseEvent event)->{
            //Bạn chỉ có thể khóa user bình thường chứ không thể khóa user admin
            if (admin==1){
                Business.alertError("Khóa tài khoản", "Bạn chỉ có thể khóa người dùng thường.\nBạn không được phép khóa tài khoản admin.");
                return;
            }
            
            if (this.active==1){
               imageView.setImage(lock);
               this.active=0;
               Business.setActiveUser(id, false);
               Business.rejectIfLocked(id);
            }else{
               imageView.setImage(unlock);
               this.active=1;
               Business.setActiveUser(id, true);
            }
        });
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNumberOfConference() {
        return numberOfConference;
    }

    public void setNumberOfConference(Integer numberOfConference) {
        this.numberOfConference = numberOfConference;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Button getLock() {
        return lock;
    }

    public void setLock(Button lock) {
        this.lock = lock;
    }
    
    
}
