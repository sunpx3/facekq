package cn.edu.bucm.mapper;

import cn.edu.bucm.entity.TongueTzm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FaceMapper {

    public int selectImgFeature(@Param("xgh") String xgh);

    public void insertImgFeature(@Param("tongueTzm") TongueTzm tongueTzm);

    public void updateImgFeature(@Param("tongueTzm") TongueTzm tongueTzm);

}
