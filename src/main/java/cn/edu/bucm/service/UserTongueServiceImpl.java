package cn.edu.bucm.service;

import cn.edu.bucm.mapper.UserTongueMapper;
import cn.edu.bucm.mapper.UserTongue;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;

@Service
public class UserTongueServiceImpl implements UserTongueService {

    @Resource
    private UserTongueMapper userTongueMapper;

    public UserTongue findUserByXgh(String xgh){
        UserTongue userTongue = userTongueMapper.findUserByXgh(xgh);
        return userTongue;
    }

    public List<UserTongue> findUsers(){
        List<UserTongue> userTongues = userTongueMapper.findUsers();
        return userTongues;
    }

    public List<UserTongue> findUsersByIndex(int beginNums, int endNums, String xgh){

        List<UserTongue> userTongues = userTongueMapper.findUsersByIndex(beginNums,endNums,xgh);
        return userTongues;
    }

    public void insertUsers(UserTongue userTongue){
        userTongueMapper.insertUsers(userTongue);
    }


    public void updateUsers(UserTongue userTongue){
        userTongueMapper.updateUsers(userTongue);
    }

    @Override
    public int count(String xgh) {
        return userTongueMapper.count(xgh);
    }

}
