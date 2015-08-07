package com.ospinet.app;


public class Friend_request_notification {

    private String type;
    private String id;
    private String user_id;
    private String profile;
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

    public Friend_request_notification() {

    }

    public String gettype() {
        return type;
    }
    public void settype(String type) {
        this.type = type;
    }

    public String getuser_id() {
        return user_id;
    }
    public void setuser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }

    public String getprofile() {
        return profile;
    }
    public void setprofile(String profile) {
        this.profile = profile;
    }
}