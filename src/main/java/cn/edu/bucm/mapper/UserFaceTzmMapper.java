package cn.edu.bucm.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFaceTzmMapper {

    public List<UserFaceTzm> findUsersTzm();
    public UserFaceTzm findUsersTzmByXgh(@Param("xgh") String xgh);
    public void insertUsersTzm(@Param("newuser") UserFaceTzm UserFaceTzm);
    public void updateUsersTzm(@Param("olduser") UserFaceTzm UserFaceTzm);
}
