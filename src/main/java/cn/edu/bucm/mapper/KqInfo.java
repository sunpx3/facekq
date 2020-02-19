package cn.edu.bucm.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class KqInfo {
    private String xgh;
    private String xm;
    private String bmh;
    private String bmmc;
    private Date rq;

    public String getXgh() {
        return xgh;
    }

    public void setXgh(String xgh) {
        this.xgh = xgh;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getBmh() {
        return bmh;
    }

    public void setBmh(String bmh) {
        this.bmh = bmh;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }

    public Date getRq() {
        return rq;
    }

    public void setRq(Date rq) {
        this.rq = rq;
    }
}
