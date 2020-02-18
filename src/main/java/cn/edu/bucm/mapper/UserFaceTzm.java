package cn.edu.bucm.mapper;

import org.springframework.stereotype.Component;

@Component
public class UserFaceTzm {



    private String xgh;
    private String tzm;


    public String getXgh() {
        return xgh;
    }

    public String getTzm() {
        return tzm;
    }

    public void setTzm(String tzm) {
        this.tzm = tzm;
    }

    public void setXgh(String xgh) {
        this.xgh = xgh;
    }
}
