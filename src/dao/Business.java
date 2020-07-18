package dao;

import entity.User;
import entity.Meeting;
import entity.Attendance;
import entity.Location;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import view.model.MeetingModel;

public class Business {    
    public static User authenticator=null;
    //Function for Admin ----------------------
    
    //Function for User ------------------------
    public static List<User> getAllUsers()
    {
        UserDAO userDAO=new UserDAO();
        return userDAO.getAllUser();
    }
    
    public static boolean saveUser(User user){
        UserDAO userDAO=new UserDAO();
        return userDAO.saveUser(user);
    }
    
    public static User login(String username, String password){
        UserDAO userDAO=new UserDAO();
        String passwordHash=Business.hashMD5(password);
        return userDAO.login(username, passwordHash);
    }
    
    public static User getUserInfo(int userId){
        UserDAO userDAO=new UserDAO();
        return userDAO.getUserInfo(userId);
    }
    
    public static void updateName(int userId, String newName){
        UserDAO userDAO=new UserDAO();
        userDAO.updateName(userId, newName);
    }
    
    public static void updateEmail(int userId, String newEmail){
        UserDAO userDAO=new UserDAO();
        userDAO.updateEmail(userId, newEmail);
    }
    
    public static boolean updatePassword(int userId, String oldPassword, String newPassword)
    {
        UserDAO userDAO=new UserDAO();
        String oldPasswordHash=Business.hashMD5(oldPassword);
        String newPasswordHash=Business.hashMD5(newPassword);
        return userDAO.updatePassword(userId, oldPasswordHash, newPasswordHash);
    }
    
    //Function for Meeting -----------------------
    public static List<Object[]> getAllMeetings(){
        MeetingDAO meetingDAO=new MeetingDAO();
        return meetingDAO.getAllMeeting();
    }
    
    public static Object[] getMeetingDetail(int meetingId){
        MeetingDAO meetingDAO=new MeetingDAO();
        return meetingDAO.getMeetingDetail(meetingId);
    }
    
    public static List<Object[]> getMyConferences(int meetingId)
    {
        MeetingDAO meetingDAO=new MeetingDAO();
        return meetingDAO.getMyConferences(meetingId);
    }
    
    //Function for Attendane ----------------------------
    public static Integer countParticipants(Integer meetingID)
    {
        AttendanceDAO attendanceDAO=new AttendanceDAO();
        return attendanceDAO.countParticipants(meetingID);
    }
    
    public static void attendConference(int meetingId, int userId)
    {
        AttendanceDAO attendanceDAO=new AttendanceDAO();
        attendanceDAO.attendConference(meetingId, userId);
    }
    
    public static int getStatus(int meetingId, int userId)
    {
        AttendanceDAO attendanceDAO=new AttendanceDAO();
        return attendanceDAO.getStatus(meetingId, userId);
    }
    
    //Function for Location ------------------------------
    public static List<Location> getAllLocation(){
        LocationDAO locationDAO=new LocationDAO();
        return locationDAO.getAllLocation();
    }
    
    public static int addLocation(Location location){
        LocationDAO locationDAO=new LocationDAO();
        return locationDAO.addLocation(location);
    }
  
    //Login funcion ---------------------
    //Kiểm tra tình trạng của hội nghị
    public static String checkStatus(Integer participants, Integer size, Date time){
        Date current=new Date();
        if (time.compareTo(current)<=0){
            return "Đã diễn ra";
        }
        
        if (participants>=size){
            return "Đủ người";
        }else{
            return "Chưa đủ người";
        }
        
    }
    
    public static String checkStatus(int status){
        if (status==0){
            return "Bị từ chối";
        }else if (status==1){
            return "Đã duyệt";
        }else if(status==2){
            return "Đang chờ duyệt";
        }
        return null;
    }
    
    //Xử lý đoạn miêu tả ngắn
    public static String convertShortDescription(String description){
        String[] tokens=description.split(" ");
        StringBuilder result=new StringBuilder();
        
        for (int i=0;i<tokens.length;i++){
            result.append(tokens[i]);
            result.append(" ");
            if ((i+1)%25==0){
                result.append("\n");
            }
        }
        
        return result.toString();
    }
    
    //Định dạng lại Date
    public static String formatDateTime(Date date){
        String pattern="hh:mm - dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
    
    //Kiểm tra ô họ và tên
    public static String checkNameField(String name){
        if (name.length()>50){
            return "Họ và tên không được quá 50 ký tự";
        }
        if (name.length()<=0){
            return "Họ và tên không được bỏ trống";
        }
        
        //Chỉ chấp nhận ký tự tiếng việt a->z, A->Z, space
        for (int i=0;i<name.length();i++){
            if (name.charAt(i)==' '){
                continue;
            }
                    
            if (name.charAt(i)>='0' && name.charAt(i)<='9'){
                return "Họ và tên không thể chứa ký tự số";
            }
            
            if (!Character.isLetter(name.charAt(i))){
                return "Họ và tên không thể chứa ký tự đặc biệt";
            }
        }
        
        return null;
    }
    
    //Kiểm tra ô Email
    public static String checkEmailField(String email)
    {
        if (!email.matches("^[a-z0-9_.]{1,32}@([a-z0-9]{1,12}).([a-z]{1,12})+$"))
        {
            return "Email không hợp lệ";
        }else{
            return null;   
        }
    }
    
    //Kiểm tra ô username
    public static String checkUserNameField(String username)
    {
        if (username.length()<=0){
            return "Username không được bỏ trống";
        }
        
        if (username.length()>20){
            return "Username không được quá 20 ký tự";
        }
        
        if (!username.matches("^[a-z0-9_-]{1,21}$"))
        {
            return "Username không hợp lệ";
        }
        
        return null;
    }
    
    //Kiểm tra mật khẩu
    public static String checkPasswordField(String password)
    {
        if (password.length()<6){
            return "Mật khẩu phải có ít nhất 6 ký tự";
        }
        
        if (password.length()>30){
            return "Mật khẩu không được quá 30 ký tự";
        }
        
        if (!password.matches("^[A-Za-z0-9]{6,30}$"))
        {
            return "Mật khẩu không hợp lệ";
        }
        
        return null;
    }
    
    //Hàm băm MD5
    public static String hashMD5(String password)
    {
        try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            byte[] messageDigest=md.digest(password.getBytes());
            BigInteger num=new BigInteger(1, messageDigest);
            String hashText=num.toString(16);
            while (hashText.length()<32){
                hashText="0"+hashText;
            }
            return hashText;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Business.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }       
    }
    
    //Hàm tách lấy tên người dùng
    public static String detachName(String fullName){
        String[] words=fullName.split(" ");
        int len=words.length;
        if (len==1) {
            return words[0];
        }else{
            return words[len-2]+" "+words[len-1];
        }
    }
    
    //Hiển thị thông báo thông tin
    public static void alertInformation(String title, String content){
        //Hiển thị thông báo đăng ký thành công
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    //Hiển thị thông báo lỗi
    public static void alertError(String title, String content){
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static void sortMeetingASC(ArrayList<MeetingModel> array)
    {
        for (int i=0;i<array.size()-1;i++){
            int min=i;
            for (int j=i+1;j<array.size();j++){
                if (array.get(j).getDateTime().compareTo(array.get(min).getDateTime())<0){
                    min=j;
                }
            }
            if (min!=i){
                MeetingModel temp=array.get(min);
                array.set(min, array.get(i));
                array.set(i, temp);         
            }
        }
    }
    
    public static void sortMeetingDESC(ArrayList<MeetingModel> array)
    {
        for (int i=0;i<array.size()-1;i++){
            int max=i;
            for (int j=i+1;j<array.size();j++){
                if (array.get(j).getDateTime().compareTo(array.get(max).getDateTime())>0){
                    max=j;
                }
            }
            if (max!=i){
                MeetingModel temp=array.get(max);
                array.set(max, array.get(i));
                array.set(i, temp);         
            }
        }
    }
    
    public static String getShortDescription(int id, List<Object[]> data){
        for (Object[] e : data){
            if ((int)e[0]==id){
                return e[3].toString();
            }
        }
        return null;
    }
    
}
