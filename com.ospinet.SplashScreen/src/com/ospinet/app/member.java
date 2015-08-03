package com.ospinet.app;


public class member {

	private String membername;
	private int memberid;
	private String gender;
	private String age;
	private String birth_info;
	private String birth_day;
	private String birth_month;
	private String birth_year;
	private String email;
	private String profile_pic;
    public member() {
       
    }

    public String getMemberName() {
        return membername;
    }
    public void setMemberName(String membername) {
        this.membername = membername;
    }
    public int getMemberId() {
        return memberid;
    }
    public void setMemberId(int memberid) {
        this.memberid = memberid;
    }
  
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    
    public String getBirth_Info() {
        return birth_info;
    }
    public void setBirth_Info(String birth_info) {
        this.birth_info = birth_info;
    }
    
    public String getBirth_day() {
        return birth_day;
    }
    public void setBirth_Day(String birth_day) {
        this.birth_day = birth_day;
    }
    

    public String getBirth_Month() {
        return birth_month;
    }
    public void setBirth_Month(String birth_month) {
        this.birth_month = birth_month;
    }
    
    public String getBirth_Year() {
        return birth_year;
    }
    public void setBirth_Year(String birth_year) {
        this.birth_year = birth_year;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getProfile_Pic() {
        return profile_pic;
    }
    public void setProfile_Pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
