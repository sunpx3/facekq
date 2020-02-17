package cn.edu.bucm.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KqInfoMapper {

    public List<KqInfo> findkqinfos();
    public KqInfo findkqinfobyxgh(@Param("xgh") String xgh);
    public int count(@Param("xgh") String xgh);
}
