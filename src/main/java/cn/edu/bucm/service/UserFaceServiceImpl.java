package cn.edu.bucm.service;

import cn.edu.bucm.mapper.UserFace;
import cn.edu.bucm.mapper.UserTongueMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserFaceServiceImpl implements UserFaceService {

    @Resource
    private UserTongueMapper userFaceMapper;

    public UserFace findUserByXgh(String xgh){
        UserFace userFace = userFaceMapper.findUserByXgh(xgh);
        return userFace;
    }

    public List<UserFace> findUsers(){
        List<UserFace> userFaces = userFaceMapper.findUsers();
        return userFaces;
    }

    public List<UserFace> findUsersByIndex(int beginNums, int endNums, String xgh){

        List<UserFace> userFaces = userFaceMapper.findUsersByIndex(beginNums,endNums,xgh);
        return userFaces;
    }

    public void insertUsers(UserFace userFace){
        userFaceMapper.insertUsers(userFace);
    }


    public void updateUsers(UserFace userFace){
        userFaceMapper.updateUsers(userFace);
    }

    @Override
    public int count(String xgh) {
        return userFaceMapper.count(xgh);
    }

}
