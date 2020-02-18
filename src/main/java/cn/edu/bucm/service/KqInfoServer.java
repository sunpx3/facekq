package cn.edu.bucm.service;

import cn.edu.bucm.mapper.KqInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KqInfoServer {
    public List<KqInfo> findkqinfos();
    public KqInfo findkqinfobyxgh(String xgh);
    public int count(String xgh);
}

