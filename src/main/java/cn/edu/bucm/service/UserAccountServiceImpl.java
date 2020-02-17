package cn.edu.bucm.service;

import cn.edu.bucm.mapper.UserAccount;
import cn.edu.bucm.mapper.UserAccountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
    public class UserAccountServiceImpl implements UserAccountService {

        @Resource
        private UserAccountMapper userAccountMapper;

        public UserAccount findUserAccountByXgh(String xgh){
            UserAccount userAccount = userAccountMapper.findUserAccountByXgh(xgh);
            return userAccount;
        }

        public List<UserAccount> findUsersAccount(){
            List<UserAccount> userAccounts = userAccountMapper.findUsersAccount();
            return userAccounts;
        }


        public void insertUsersAccount(UserAccount userAccount){
            userAccountMapper.insertUsersAccount(userAccount);
        }


        public void updateuseraccount(UserAccount userAccount){
            userAccountMapper.updateuseraccount(userAccount);
        }

        @Override
        public int count(String xgh) {
            return userAccountMapper.count(xgh);
        }

    }

