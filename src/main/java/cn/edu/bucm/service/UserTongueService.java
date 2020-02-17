package cn.edu.bucm.service;

import cn.edu.bucm.mapper.UserTongue;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserTongueService {
    public List<UserTongue> findUsers();
    public List<UserTongue> findUsersByIndex(int beginNums, int endNums, String xgh);
    public UserTongue findUserByXgh(String xgh);
    public void insertUsers(UserTongue userTongue);
    public void updateUsers(UserTongue userTongue);
    public int count(String xgh);

}
