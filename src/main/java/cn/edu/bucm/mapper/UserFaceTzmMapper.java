package cn.edu.bucm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFaceTzmMapper {

    public int selectImgFeature(@Param("xgh") String xgh);

    public void insertImgFeature(@Param("userFaceTzm") UserFaceTzm userFaceTzm);

    public void updateImgFeature(@Param("userFaceTzm") UserFaceTzm userFaceTzm);

    public List<UserFaceTzm> findAllUserFaceTzmList();

}
