package cn.edu.bucm.service;

import cn.edu.bucm.entity.TongueTzm;
import cn.edu.bucm.face.VisitSAASAuthorize;
import cn.edu.bucm.mapper.FaceMapper;
import cn.edu.bucm.utils.Contants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 处理图片特征
 */
@Service
public class FaceServiceImpl implements FaceService {

    private static VisitSAASAuthorize visitSAASAuthorize = new VisitSAASAuthorize();

    @Autowired
    private FaceMapper faceMapper;


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
    public void savePersonImgFeature(TongueTzm tongueTzm) {
        int resCount = faceMapper.selectImgFeature(tongueTzm.getXgh());
        if(resCount > 0){
            faceMapper.updateImgFeature(tongueTzm);
        }else{
            faceMapper.insertImgFeature(tongueTzm);
        }

    }

    @Override
    public void updatePersonImgFeature(TongueTzm tongueTzm) {
        faceMapper.updateImgFeature(tongueTzm);
    }
}
