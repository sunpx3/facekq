package cn.edu.bucm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface UserTongueMapper {
    public List<UserFace> findUsers();
    public List<UserFace> findUsersByIndex(@Param("beginNums") int beginNums, @Param("endNums") int endNums, @Param("xgh") String xgh);
    public UserFace findUserByXgh(@Param("xgh") String xgh);
    public void insertUsers(@Param("newuser") UserFace userFace);
    public void updateUsers(@Param("newuser") UserFace userFace);
    public int count(@Param("xgh") String xgh);
}
