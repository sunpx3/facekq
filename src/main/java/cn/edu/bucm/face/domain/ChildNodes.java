package cn.edu.bucm.face.domain; /**
  * Copyright 2019 bejson.com 
  */
import java.util.List;

/**
 * Auto-generated: 2019-04-12 15:22:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ChildNodes {

    private String orgType;
    private long createTime;
    private List<String> childNodes;
    private String name;
    private boolean disabled;
    private long updateTime;
    private int id;
    private int parentId;
    public void setOrgType(String orgType) {
         this.orgType = orgType;
     }
     public String getOrgType() {
         return orgType;
     }

    public void setCreateTime(long createTime) {
         this.createTime = createTime;
     }
     public long getCreateTime() {
         return createTime;
     }

    public void setChildNodes(List<String> childNodes) {
         this.childNodes = childNodes;
     }
     public List<String> getChildNodes() {
         return childNodes;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setDisabled(boolean disabled) {
         this.disabled = disabled;
     }
     public boolean getDisabled() {
         return disabled;
     }

    public void setUpdateTime(long updateTime) {
         this.updateTime = updateTime;
     }
     public long getUpdateTime() {
         return updateTime;
     }

    public void setId(int id) {
         this.id = id;
     }
     public int getId() {
         return id;
     }

    public void setParentId(int parentId) {
         this.parentId = parentId;
     }
     public int getParentId() {
         return parentId;
     }

}