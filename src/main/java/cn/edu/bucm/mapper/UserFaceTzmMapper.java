package cn.edu.bucm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserFaceTzmMapper {

    public int selectImgFeature(@Param("xgh") String xgh);

    public void insertImgFeature(@Param("UserFaceTzm") UserFaceTzm userFaceTzm);

    public void updateImgFeature(@Param("UserFaceTzm") UserFaceTzm userFaceTzm);

}
