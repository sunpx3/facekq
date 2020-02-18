package cn.edu.bucm.service;

import cn.edu.bucm.entity.TongueTzm;

import java.io.File;

public interface FaceService {

    // 生成照片特征码
    public String getPersonFeatureByImgFile(File file) throws Exception;

    // 根据工号获取特征
    public String getPersonFeatureByXgh(String xgh);

    // 根据工号获取saas库图片
    public String getRemotePersonFeatureByXgh(String xgh);

    // 保存特征码
    public void  savePersonImgFeature(TongueTzm tongueTzm);

    public void  updatePersonImgFeature(TongueTzm tongueTzm);




}
