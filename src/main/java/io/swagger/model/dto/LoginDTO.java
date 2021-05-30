package io.swagger.model.dto;

public class LoginDTO {
    private String emailaddress;
    private String password;

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginDTO(){

    }

    public LoginDTO(String emailaddress, String password) {
        this.emailaddress = emailaddress;
        this.password = password;
    }
}
