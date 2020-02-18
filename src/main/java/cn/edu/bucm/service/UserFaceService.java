package cn.edu.bucm.service;

import cn.edu.bucm.mapper.UserFace;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserFaceService {
    public List<UserFace> findUsers();
    public List<UserFace> findUsersByIndex(int beginNums, int endNums, String xgh);
    public UserFace findUserByXgh(String xgh);
    public void insertUsers(UserFace userFace);
    public void updateUsers(UserFace userFace);
    public int count(String xgh);

}
