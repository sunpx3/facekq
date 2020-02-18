package cn.edu.bucm.service;

import cn.edu.bucm.mapper.UserFaceTzm;

import java.util.List;

public interface UserFaceTzmServer {
    public List<UserFaceTzm> findUsersTzm();
    public UserFaceTzm findUsersTzmByXgh(String xgh);
    public void insertUsersTzm(UserFaceTzm newuser);
    public void updateUsersTzm(UserFaceTzm olduser);
}
