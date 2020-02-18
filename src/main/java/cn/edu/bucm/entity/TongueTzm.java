package cn.edu.bucm.entity;

import java.sql.SQLException;

public class TongueTzm {
    private int id;
    private String xgh;

    private String tzm;

    public TongueTzm(){

    }

    public TongueTzm( String xgh, String tzm) throws SQLException {
        this.xgh = xgh;
        this.tzm = tzm;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXgh() {
        return xgh;
    }

    public void setXgh(String xgh) {
        this.xgh = xgh;
    }

    public String getTzm() {
        return tzm;
    }

    public void setTzm(String tzm) {
        this.tzm = tzm;
    }
}
