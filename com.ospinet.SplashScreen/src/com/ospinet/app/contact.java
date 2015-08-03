package com.ospinet.app;


public class contact {

    private String friend_id;
    private String email;
    public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	private String fname;
    private String lname;
    public contact() {

    }
    public String getfriend_id() {
        return friend_id;
    }
    public void setfriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getemail() {
        return email;
    }
    public void setemail(String email) {
        this.email = email;
    }
}