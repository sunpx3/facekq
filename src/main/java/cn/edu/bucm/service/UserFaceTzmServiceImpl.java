package cn.edu.bucm.service;

import cn.edu.bucm.mapper.UserFaceTzm;
import cn.edu.bucm.mapper.UserFaceTzmMapper;

import javax.annotation.Resource;
import java.util.List;

public class UserFaceTzmServiceImpl implements UserFaceTzmServer {
    @Resource
    private UserFaceTzmMapper userFaceTzmMapper;

    public List<UserFaceTzm> findUsersTzm(){
        return userFaceTzmMapper.findUsersTzm();
    }
    public UserFaceTzm findUsersTzmByXgh(String xgh)
    {
        return userFaceTzmMapper.findUsersTzmByXgh(xgh);
    }
    public void insertUsersTzm(UserFaceTzm newuser){
        userFaceTzmMapper.insertUsersTzm(newuser);
    }
    public void updateUsersTzm(UserFaceTzm olduser){
        userFaceTzmMapper.updateUsersTzm(olduser);
    }
}
