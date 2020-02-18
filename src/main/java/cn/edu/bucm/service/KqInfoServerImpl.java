package cn.edu.bucm.service;


import cn.edu.bucm.mapper.KqInfo;
import cn.edu.bucm.mapper.KqInfoMapper;

import javax.annotation.Resource;
import java.util.List;


public class KqInfoServerImpl implements KqInfoServer {

    @Resource
    private KqInfoMapper kqInfoMapper;

    public List<KqInfo> findkqinfos(){
        return  kqInfoMapper.findkqinfos();
    }
    public KqInfo findkqinfobyxgh(String xgh){
        return kqInfoMapper.findkqinfobyxgh(xgh);
    }
    public int count(String xgh){
        return kqInfoMapper.count(xgh);
    }
}
