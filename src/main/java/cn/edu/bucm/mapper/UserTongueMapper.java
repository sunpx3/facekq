package cn.edu.bucm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface UserTongueMapper {
    public List<UserTongue> findUsers();
    public List<UserTongue> findUsersByIndex(@Param("beginNums") int beginNums, @Param("endNums") int endNums, @Param("xgh") String xgh);
    public UserTongue findUserByXgh(@Param("xgh") String xgh);
    public void insertUsers(@Param("newuser") UserTongue userTongue);
    public void updateUsers(@Param("newuser") UserTongue userTongue);
    public int count(@Param("xgh") String xgh);
}
