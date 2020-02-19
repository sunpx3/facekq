package cn.edu.bucm.controller;

import cn.edu.bucm.exceptions.ThePicHasNotFaceException;
import cn.edu.bucm.mapper.KqInfo;
import cn.edu.bucm.mapper.UserAccount;
import cn.edu.bucm.mapper.UserFace;
import cn.edu.bucm.mapper.UserFaceTzm;
import cn.edu.bucm.service.KqInfoServer;
import cn.edu.bucm.service.UserAccountService;
import cn.edu.bucm.service.UserFaceService;
import cn.edu.bucm.service.UserFaceTzmService;
import cn.edu.bucm.utils.FileUtils;
import cn.edu.bucm.utils.LdapCheck;
import cn.edu.bucm.utils.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/kqface")
public class FaceController {


    @Autowired
    private UserFaceService userFaceService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserFaceTzmService userFaceTzmService;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private KqInfoServer kqInfoServer;

    private static final Logger log = LoggerFactory.getLogger(FaceController.class);


    @RequestMapping(value = "/finduser", method = RequestMethod.GET)
    @ResponseBody
    public void findUserByParam(@RequestParam(value = "xgh") String xgh, HttpServletResponse response) throws IOException {

        byte[] imgBytes;
        if(redisUtil.hasKey(xgh)){
            String base64 = redisUtil.get(xgh);
            imgBytes = Base64.getDecoder().decode(base64);
        }else{
            UserFace newuser = userFaceService.findUserByXgh(xgh);
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
        List<UserFace> userFaceList = userFaceService.findUsersByIndex(beginNums, endNums, xgh);
        int total = userFaceService.count(xgh);
        System.out.println(total);

        Iterator iterator = userFaceList.iterator();
        List<String> stringList = new ArrayList<>();

        while (iterator.hasNext()) {
            UserFace userFace = (UserFace) iterator.next();
            stringList.add(userFace.getXgh());
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
        List<UserFace> userFaceList = userFaceService.findUsersByIndex(beginNums, endNums, "");
        int total = userFaceService.count("");
        Iterator iterator = userFaceList.iterator();
        List<String> stringList = new ArrayList<>();

        while (iterator.hasNext()) {
            UserFace userFace = (UserFace) iterator.next();
            stringList.add(userFace.getXgh());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imgList", stringList);
        jsonObject.put("total", total);
        return jsonObject;
    }

    @RequestMapping(value = "/insertuser", method = RequestMethod.POST)
    @ResponseBody
    public String insertUsers(@RequestParam(value = "newuser") UserFace newuser) {
        userFaceService.insertUsers(newuser);
        return "susscess";
    }

    //@RequestMapping(value = "/success",method=RequestMethod.POST)
    @PostMapping("/updateOrAddPersonnelPic")
    @ResponseBody
    public JSONObject showSuccess(@RequestParam(required = false, value = "persName") String personName, @RequestParam("picBase64") MultipartFile multipartFile, HttpSession session, HttpServletRequest request) throws IOException {

        String persname = personName;
        MultipartFile multipartfile = multipartFile;
        InputStream inputStream = multipartfile.getInputStream();


        try {
            multipartFile.getOriginalFilename();
            String filePath = request.getSession().getServletContext().getRealPath("/") + String.valueOf(Math.random() * 11)+ ".jpg";
            multipartFile.transferTo(new File(filePath));
            String imgFeature = userFaceTzmService.getPersonFeatureByImgFile(new File(filePath));

            UserFaceTzm userFaceTzm=new UserFaceTzm();
            userFaceTzm.setXgh(persname);
            userFaceTzm.setTzm(imgFeature);
            userFaceTzmService.savePersonImgFeature(userFaceTzm);

        } catch (Exception e) {
            log.error(e.getMessage() + "|" + "生成特征码出错，请检查!");
        }


        boolean stat = false;
        JSONObject resMap = new JSONObject();

        List<UserFace> userFaceList = userFaceService.findUsers();
        Iterator iterator = userFaceList.iterator();

        while (iterator.hasNext()) {
            UserFace userFace = (UserFace) iterator.next();
            if (userFace.getXgh().equalsIgnoreCase(persname)) {
                stat = true;
                break;
            } else {
                stat = false;
            }
        }


        //  BASE64Encoder encode=new BASE64Encoder();

        UserFace newuser = new UserFace();
        try {
            inputStream = multipartfile.getInputStream();
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
                    userFaceService.updateUsers(newuser);
                    resMap.put("persName", persname);
                    resMap.put("status", "success");
                } else {
                    if (!"undefined".equals(persname)) {
                        userFaceService.insertUsers(newuser);
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


    @PostMapping(value = "/uploadDiffPersonnelPic",  produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public JSONObject uploadDiffPic(@RequestParam("picBase64") MultipartFile multipartFile, HttpServletRequest request) {



        JSONObject resultMsg = new JSONObject();
        System.out.println("识别开始...");
        try {
            //String filePath = request.getSession().getServletContext().getRealPath("/")  + String.valueOf(Math.random() * 10) + multipartFile.getOriginalFilename();
           // multipartFile.transferTo(new File(filePath));
            //String feature = userFaceTzmService.getPersonFeatureByImgFile(new File(filePath));
            File file=FileUtils.multipartFileToFile(multipartFile);
            String feature = userFaceTzmService.getPersonFeatureByImgFile(file);
            userFaceTzmService.getUserFaceTzmList().forEach( (UserFaceTzm userFaceTzm) -> {
                try {
                    String zbrgh;
                    KqInfo kqInfo;
                    if(userFaceTzmService.diffPicFeature(feature, userFaceTzm.getTzm())){
                        // 比对成功
                        System.out.println("比对成功:" + userFaceTzm.getXgh());
                        zbrgh=userFaceTzm.getXgh();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                        Date rq=new Date();
                        df.format(rq);
                        kqInfo=kqInfoServer.findkqinfobyxgh(zbrgh,rq);
                        if(kqInfo.getXgh()!=null) {
                            System.out.println("今天您值班，能通行！");
                            resultMsg.put("status", "success");
                        }else
                        {
                            resultMsg.put("status","fail");
                            System.out.println("今天您不值班，不能通行！");
                        }
                    }
                } catch (Exception e) {
                    String errorMsg = e.getMessage() + "|" + "特征比对出错，不是本校人员！";
                    log.error(errorMsg);
                    resultMsg.put("errorMsg", errorMsg);
                }
            });

        }catch (ThePicHasNotFaceException e){
            log.error(e.getMessage());
            resultMsg.put("errorMsg", e.getMessage());
        }
        catch (Exception e) {
            resultMsg.put("errorMsg", e.getMessage());
        }


        return resultMsg;
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


    @PostMapping("/ldapLogin")
    @ResponseBody
    public JSONObject casLogin(@RequestBody JSONObject jsonpObject) {

        String baseDN="ou=bks,ou=bucmuser,dc=bucm,dc=edu,dc=cn";
        String username = jsonpObject.getString("username");
        String password = jsonpObject.getString("password");
        System.out.println(username+"+"+password);

        JSONObject resMap = new JSONObject();

        Map<String, Object> login = LdapCheck.authenticate(baseDN, username, password);
        boolean result = ((Boolean)login.get("loginResult")).booleanValue();
        if (!result) {
            login = LdapCheck.authenticate("ou=sss,ou=bucmuser,dc=bucm,dc=edu,dc=cn", username, password);
            result = ((Boolean)login.get("loginResult")).booleanValue();
        } else {
            result = true;
        }
        if (!result) {
            login = LdapCheck.authenticate("ou=bss,ou=bucmuser,dc=bucm,dc=edu,dc=cn", username, password);
            result = ((Boolean)login.get("loginResult")).booleanValue();
        } else {
            result = true;
        }
        if (!result) {
            login = LdapCheck.authenticate("ou=fsyyzg,ou=bucmuser,dc=bucm,dc=edu,dc=cn", username, password);
            result = ((Boolean)login.get("loginResult")).booleanValue();
        } else {
            result = true;
        }
        if (!result) {
            login = LdapCheck.authenticate("ou=jzg,ou=bucmuser,dc=bucm,dc=edu,dc=cn", username, password);
            result = ((Boolean)login.get("loginResult")).booleanValue();
        } else {
            result = true;
        }
        if (!result) {
            login = LdapCheck.authenticate("ou=ltxzg,ou=bucmuser,dc=bucm,dc=edu,dc=cn", username, password);
            result = ((Boolean)login.get("loginResult")).booleanValue();
        } else {
            result = true;
        }
        if (result) {
            resMap.put("username", username);
            resMap.put("status", "success");
        }else{
            resMap.put("status", "error");
        }

        return resMap;

    }



    @GetMapping("/getOndutyPersonInfo")
    @ResponseBody
    public JSONObject getOndutyPersonInfo(@RequestParam("xgh") String xgh){


        if(StringUtils.isEmpty(xgh)) {

        }
        UserFace userFace = userFaceService.findUserByXgh(xgh);
        // ????β????????????????
        if(userFace == null){

        }else{
            //
        }

        return null;
    }


}





