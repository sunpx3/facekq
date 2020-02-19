package cn.edu.bucm.service;


import cn.edu.bucm.mapper.KqInfo;
import cn.edu.bucm.mapper.KqInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class KqInfoServerImpl implements KqInfoServer {

    @Resource
    private KqInfoMapper kqInfoMapper;

    public List<KqInfo> findkqinfos(){
        return  kqInfoMapper.findkqinfos();
    }
    public KqInfo findkqinfobyxgh(String xgh,Date rq){
        return kqInfoMapper.findkqinfobyxgh(xgh,rq);
    }
    public int count(String xgh){
        return kqInfoMapper.count(xgh);
    }
}
