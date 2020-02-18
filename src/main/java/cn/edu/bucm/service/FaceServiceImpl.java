package cn.edu.bucm.service;

import cn.edu.bucm.face.VisitSAASAuthorize;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FaceServiceImpl implements FaceService {

    private static VisitSAASAuthorize visitSAASAuthorize = new VisitSAASAuthorize();


    @Override
    public String getPersonFeatureByImgFile(File file) {


        try {
            String token = VisitSAASAuthorize.getToken (visitSAASAuthorize.basic);
            // VisitSAASAuthorize.faceTest(token, )
        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }
}
