package cn.edu.bucm.service;

import cn.edu.bucm.face.VisitSAASAuthorize;
import cn.edu.bucm.mapper.UserFaceTzm;
import cn.edu.bucm.mapper.UserFaceTzmMapper;
import cn.edu.bucm.utils.Contants;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 处理图片特征
 */
@Service
public class UserFaceTzmServiceImpl implements UserFaceTzmService {

    private static VisitSAASAuthorize visitSAASAuthorize = new VisitSAASAuthorize();

    @Resource
    private UserFaceTzmMapper userFaceTzmMapper;


    @Override
    public String getPersonFeatureByImgFile(File file) throws Exception {

        String token = VisitSAASAuthorize.getToken (visitSAASAuthorize.basic);
        String resJson = VisitSAASAuthorize.faceTest(token, file, Integer.valueOf(Contants.TENANT_ID));
        String feature = VisitSAASAuthorize.getFeature(resJson);

        return feature;
    }

    @Override
    public String getPersonFeatureByXgh(String xgh) {
        return null;
    }

    @Override
    public String getRemotePersonFeatureByXgh(String xgh) {
        return null;
    }

    @Override
    public boolean diffPicFeature(String feature1, String feature2) throws Exception {
        String token = VisitSAASAuthorize.getToken (visitSAASAuthorize.basic);
        String resJson = VisitSAASAuthorize.faceFeatureCompare(token, feature1, feature2, Integer.valueOf(Contants.TENANT_ID));
        double scoreNums = Double.valueOf(JSONObject.parseObject(resJson).getString("entity"));

        if(scoreNums >= Contants.DIFF_PICRATENUMS){
            return true;
        }
        return false;
    }


    @Override
    public void savePersonImgFeature(UserFaceTzm userFaceTzm) {
        int resCount = userFaceTzmMapper.selectImgFeature(userFaceTzm.getXgh());
        if(resCount > 0){
            userFaceTzmMapper.updateImgFeature(userFaceTzm);
        }else{
            userFaceTzmMapper.insertImgFeature(userFaceTzm);
        }

    }

    @Override
    public void updatePersonImgFeature(UserFaceTzm userFaceTzm) {
        userFaceTzmMapper.updateImgFeature(userFaceTzm);
    }

    @Override
    public List<UserFaceTzm> getUserFaceTzmList() {
        return userFaceTzmMapper.findAllUserFaceTzmList();
    }
}
