package gui.layout.login.create_id_window;

class Member {
    private String name;
    private String pwd;
    private String phoneNum;
    private String email;
    
    public Member(String email, String pwd, String name, String phoneNum) {
    	
    	this.email = email;
        this.pwd = pwd;
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
    	return email;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    	
    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    
    @Override
    public String toString() {
        return "메일주소 : " + email + "\n" +
               "비밀번호 : " + pwd + "\n" +
               "이　　름 : " + name + "\n" +
               "전화번호 : " + phoneNum + "\n";
    }
}