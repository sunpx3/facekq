package cn.edu.bucm.mapper;

import org.springframework.stereotype.Component;

@Component
public class UserAccount {

    private String xgh;

    public String getXgh() {
        return xgh;
    }

    public void setXgh(String xgh) {
        this.xgh = xgh;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;
}
