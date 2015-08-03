package com.ospinet.app;


public class record {

    private String title;
    private String id;
    private String description;
    private String record_date;
    private String tag;
    private String attachment_path;
    private String member_id;
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
    public record() {

    }
    public String getmember_id() {
        return member_id;
    }
    public void setmember_id(String member_id) {
        this.member_id = member_id;
    }

    public String gettitle() {
        return title;
    }
    public void settitle(String title) {
        this.title = title;
    }


    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }

    public String getdescription() {
        return description;
    }
    public void setdescription(String description) {
        this.description = description;
    }

    public String getrecord_date() {
        return record_date;
    }
    public void setrecord_date(String record_date) {
        this.record_date = record_date;
    }

    public String gettag() {
        return tag;
    }
    public void settag(String tag) {
        this.tag = tag;
    }


    public String getattachment_path() {
        return attachment_path;
    }
    public void setattachment_path(String attachment_path) {
        this.attachment_path = attachment_path;
    }
}