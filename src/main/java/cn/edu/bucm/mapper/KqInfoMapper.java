package cn.edu.bucm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface KqInfoMapper {

    public List<KqInfo> findkqinfos();
    public KqInfo findkqinfobyxgh(@Param("xgh") String xgh,@Param("rq") Date rq);
    public int count(@Param("xgh") String xgh);
}
