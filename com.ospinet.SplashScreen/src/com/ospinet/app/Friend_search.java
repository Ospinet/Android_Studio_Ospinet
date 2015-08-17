package com.ospinet.app;


public class Friend_search {

    private String uid;
    private String login_status;
    private String type;
    private String email;
    private String id;
    private String ns;
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

    public Friend_search() {

    }

    public String gettype() {
        return type;
    }
    public void settype(String type) {
        this.type = type;
    }

    public String getns() {
        return ns;
    }
    public void setns(String ns) {
        this.ns = ns;
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

    public String getuid() {
        return uid;
    }
    public void setuid(String uid) {
        this.uid = uid;
    }

    public String getlogin_status() {
        return login_status;
    }
    public void setlogin_status(String login_status) {
        this.login_status = login_status;
    }

    public String getemail() {
        return email;
    }
    public void setemail(String email) {
        this.email = email;
    }
}