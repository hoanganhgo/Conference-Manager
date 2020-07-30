package view.model;

import dao.Business;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

public class UserModel {
    private int attendanceId;
    private int number;
    private String fullname;
    private String username;
    private String email;
    private Button approval;
    private Button noApproval;

    public UserModel(int attendanceId, int meetingId, int number, String fullname, String username, String email,final int size, int[] participants, int[] num, Label participanted, Label total) {
        this.attendanceId = attendanceId;
        this.number = number;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        
        Image imageTick=new Image("./././images/tick.png");
        ImageView imageViewTick=new ImageView(imageTick);
        imageViewTick.setFitHeight(20);
        imageViewTick.setFitWidth(20);
        
        Image imageCancel=new Image("./././images/cancel.png");
        ImageView imageViewCancel=new ImageView(imageCancel);
        imageViewCancel.setFitHeight(20);
        imageViewCancel.setFitWidth(20);
        
        this.approval=new Button();
        this.approval.setGraphic(imageViewTick);
        this.noApproval=new Button();
        this.noApproval.setGraphic(imageViewCancel);
        
        this.approval.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            if (participants[0]==size){
                Business.alertError("Duyệt đăng ký tham dự", "Hội nghị đã đủ số người quy định");
                return;
            }
            
            Business.setStatusAttendance(attendanceId, 1);
            this.approval.setVisible(false);
            this.noApproval.setVisible(false);
            participants[0]++;
            participanted.setText("Số người đã tham dự: "+participants[0]);
            
            num[0]--;
            total.setText("Tổng số: "+(num[0]-1));
            
            //Nếu đã đủ người
//            if (participants[0]==size){
//                //Tất cả các yêu cầu còn lại điều bị từ chối
//                Business.rejectAll(meetingId);
//            }
        });
        
        this.noApproval.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->{
            if (participants[0]==size){
                Business.alertInformation("Duyệt đăng ký tham dự", "Hệ thông đã từ chối các yêu cầu tham dự còn lại\nBạn không cần phải tự hủy.");
            }else{
                Business.setStatusAttendance(attendanceId, 0);
            }
                        
            this.approval.setVisible(false);
            this.noApproval.setVisible(false);
            
            num[0]--;
            total.setText("Tổng số: "+(num[0]-1));
        });
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getNumber() {
        return number;
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

    public Button getApproval() {
        return approval;
    }

    public void setApproval(Button approval) {
        this.approval = approval;
    }

    public Button getNoApproval() {
        return noApproval;
    }

    public void setNoApproval(Button noApproval) {
        this.noApproval = noApproval;
    }
    
    
}
