package cn.edu.bucm.service;


import cn.edu.bucm.mapper.UserFaceTzm;

import java.io.File;
import java.util.List;

public interface UserFaceTzmService {

    // 生成照片特征码
    public String getPersonFeatureByImgFile(File file) throws Exception;

    // 根据工号获取特征
    public String getPersonFeatureByXgh(String xgh);

    // 根据工号获取saas库图片
    public String getRemotePersonFeatureByXgh(String xgh);

    public boolean diffPicFeature(String feature1, String feature2) throws Exception;

    // 保存特征码
    public void  savePersonImgFeature(UserFaceTzm userFaceTzm);

    public void  updatePersonImgFeature(UserFaceTzm userFaceTzm);

    public List<UserFaceTzm> getUserFaceTzmList();




}
