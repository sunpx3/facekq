package cn.edu.bucm.service;

import cn.edu.bucm.mapper.UserAccount;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface UserAccountService {
    public List<UserAccount> findUsersAccount();
    public UserAccount findUserAccountByXgh(String xgh);
    public void insertUsersAccount(UserAccount userAccount);
    public void updateuseraccount(UserAccount userAccount);
    public int count(String xgh);

}
