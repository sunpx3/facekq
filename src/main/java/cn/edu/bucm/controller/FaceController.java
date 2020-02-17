package cn.edu.bucm.controller;

import cn.edu.bucm.mapper.UserAccount;
import cn.edu.bucm.mapper.UserTongue;
import cn.edu.bucm.service.UserAccountService;
import cn.edu.bucm.service.UserTongueService;
import cn.edu.bucm.utils.RedisUtil;
import com.alibaba.fastjson.JSONObject;

import io.lettuce.core.RedisConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.util.Base64;
import java.util.*;

@Controller
@RequestMapping(value = "/tongue")
public class FaceController {


    @Autowired
    private UserTongueService userTongueService;

    @Autowired
    private UserAccountService userAccountService;

    @Resource
    private RedisUtil redisUtil;

    @RequestMapping(value = "/finduser", method = RequestMethod.GET)
    @ResponseBody
    public void findUserByParam(@RequestParam(value = "xgh") String xgh, HttpServletResponse response) throws IOException {

        byte[] imgBytes;
        if(redisUtil.hasKey(xgh)){
            String base64 = redisUtil.get(xgh);
            imgBytes = Base64.getDecoder().decode(base64);
        }else{
            UserTongue newuser = userTongueService.findUserByXgh(xgh);
            imgBytes = newuser.getTzm();
        }
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(imgBytes);
        outputStream.flush();
    }


    @GetMapping("/searchuser")
    @ResponseBody
    public JSONObject searchuserByIndex(@RequestParam(value = "xgh") String xgh, @RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize) {

        System.out.println(pageIndex + "---" + pageSize + "---" + xgh);
        JSONObject jsonObject = new JSONObject();
        int beginNums = (pageIndex - 1) * pageSize;
        int endNums = pageIndex * pageSize;
        List<UserTongue> userTongueList = userTongueService.findUsersByIndex(beginNums, endNums, xgh);
        int total = userTongueService.count(xgh);
        System.out.println(total);

        Iterator iterator = userTongueList.iterator();
        List<String> stringList = new ArrayList<>();

        while (iterator.hasNext()) {
            UserTongue userTongue = (UserTongue) iterator.next();
            stringList.add(userTongue.getXgh());
        }
        jsonObject.put("imgList", stringList);
        jsonObject.put("total", total);

        return jsonObject;
    }


    @GetMapping("/findallusers")
    @ResponseBody
    public JSONObject findAllUserByPage(@RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize) {
        System.out.println(pageIndex + "..." + pageSize);
        int beginNums = (pageIndex - 1) * pageSize;
        int endNums = pageIndex * pageSize;
        List<UserTongue> userTongueList = userTongueService.findUsersByIndex(beginNums, endNums, "");
        int total = userTongueService.count("");
        Iterator iterator = userTongueList.iterator();
        List<String> stringList = new ArrayList<>();

        while (iterator.hasNext()) {
            UserTongue userTongue = (UserTongue) iterator.next();
            stringList.add(userTongue.getXgh());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imgList", stringList);
        jsonObject.put("total", total);
        return jsonObject;
    }

    @RequestMapping(value = "/insertuser", method = RequestMethod.POST)
    @ResponseBody
    public String insertUsers(@RequestParam(value = "newuser") UserTongue newuser) {
        userTongueService.insertUsers(newuser);
        return "susscess";
    }

    //@RequestMapping(value = "/success",method=RequestMethod.POST)
    @PostMapping("/updateOrAddPersonnelPic")
    @ResponseBody
    public JSONObject showSuccess(@RequestParam(required = false, value = "persName") String personName, @RequestParam("picBase64") MultipartFile multipartFile, HttpSession session) {

        String persname = personName;
        MultipartFile multipartfile = multipartFile;

        boolean stat = false;
        JSONObject resMap = new JSONObject();

        List<UserTongue> userTongueList = userTongueService.findUsers();
        Iterator iterator = userTongueList.iterator();

        while (iterator.hasNext()) {
            UserTongue userTongue = (UserTongue) iterator.next();
            if (userTongue.getXgh().equalsIgnoreCase(persname)) {
                stat = true;
                break;
            } else {
                stat = false;
            }
        }


        //  BASE64Encoder encode=new BASE64Encoder();

        UserTongue newuser = new UserTongue();
        try {
            InputStream inputStream = multipartfile.getInputStream();
            if (inputStream != null && !"undefined".equals(persname)) {
                byte[] data = new byte[inputStream.available()];
                inputStream.read(data);
                inputStream.close();
                // String base64Str=encode.encode(data);
                //  System.out.println(base64Str.length());
                // System.out.println(base64Str);
                newuser.setXgh(persname);
                System.out.println(persname + ":" + !"undefined".equals(persname));
                newuser.setTzm(data);
                if (stat) {
                    userTongueService.updateUsers(newuser);
                    resMap.put("persName", persname);
                    resMap.put("status", "success");
                } else {
                    if (!"undefined".equals(persname)) {
                        userTongueService.insertUsers(newuser);
                        resMap.put("persName", persname);
                        resMap.put("status", "success");
                    } else {
                        resMap.put("status", "error");
                    }
                }
            } else {
                resMap.put("status", "error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resMap;
    }

    //@RequestMapping(value = "/login",method=RequestMethod.POST)
    @PostMapping("/login")
    @ResponseBody
    public JSONObject Login(@RequestBody JSONObject jsonpObject) {

        String username = jsonpObject.getString("username");
        String password = jsonpObject.getString("password");

        JSONObject resMap = new JSONObject();


        List<UserAccount> userAccounts = userAccountService.findUsersAccount();
        Iterator iterator = userAccounts.iterator();

        while (iterator.hasNext()) {
            UserAccount userAccount = (UserAccount) iterator.next();
            if (userAccount.getXgh().equalsIgnoreCase(username) && userAccount.getPassword().equalsIgnoreCase(password)) {
                resMap.put("username", username);
                resMap.put("status", "success");
                break;
            } else {
                resMap.put("status", "error");
            }
        }
        return resMap;
    }


    @PostMapping("/casLogin")
    @ResponseBody
    public JSONObject casLogin() {
        System.out.println("it is here");
        JSONObject resMap = new JSONObject();
        resMap.put("status", "success");
        return resMap;

    }


}





