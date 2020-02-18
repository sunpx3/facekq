package cn.edu.bucm.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserAccountMapper {
    public List<UserAccount> findUsersAccount();
    public UserAccount findUserAccountByXgh(@Param("xgh") String xgh);
    public void insertUsersAccount(@Param("userAccount") UserAccount userAccount);
    public void updateuseraccount(@Param("userAccount") UserAccount userAccount);
    public int count(@Param("xgh") String xgh);
}
