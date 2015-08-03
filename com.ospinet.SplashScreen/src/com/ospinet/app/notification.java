package com.ospinet.app;


public class notification {

    private String type_id;
    private String id;
    private String from_user_id;
    private String member_record_id;
    private String email;
    private String fname;
    private String lname;

    public String getfname() {
		return fname;
	}
	public void setfname(String fname) {
		this.fname = fname;
	}

	public String getlname() {
		return lname;
	}
	public void setlname(String lname) {
		this.lname = lname;
	}

    public notification() {

    }

    public String gettype_id() {
        return type_id;
    }
    public void settype_id(String type_id) {
        this.type_id = type_id;
    }

    public String getfrom_user_id() {
        return from_user_id;
    }
    public void setfrom_user_id(String from_user_id) {
        this.from_user_id = from_user_id;
    }


    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }

    public String getmember_record_id() {
        return member_record_id;
    }
    public void setmember_record_id(String member_record_id) {
        this.member_record_id = member_record_id;
    }

    public String getemail() {
        return email;
    }
    public void setemail(String email) {
        this.email = email;
    }
}