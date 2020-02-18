package cn.edu.bucm.mapper;


import org.springframework.stereotype.Component;
@Component
public class UserFace {



    private String xgh;
    private byte[] tzm;


    public String getXgh() {
        return xgh;
    }

    public byte[] getTzm() {
        return tzm;
    }

    public void setTzm(byte[] tzm) {
        this.tzm = tzm;
    }

    public void setXgh(String xgh) {
        this.xgh = xgh;
    }


}
