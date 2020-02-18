package cn.edu.bucm.face.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 人员信息
 *
 * @author js
 */
public class Person {

    /**
     * 人员id
     */

    private String id;
    /**
     * 人员名称
     */
    private String name;
    /**
     * 人员编号
     */
    private String no;
    private String icNumber;
    private String mail;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * 密码
     */
    private String password;
    /**
     * 组织id
     */
    private Integer organizationId;
    private String idCard;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getWgNumber() {
        return wgNumber;
    }

    public void setWgNumber(String wgNumber) {
        this.wgNumber = wgNumber;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", no='" + no + '\'' +
                ", icNumber='" + icNumber + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", organizationId=" + organizationId +
                ", wgNumber='" + wgNumber + '\'' +
                ", syncVersion=" + syncVersion +
                ", sex='" + sex + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", tenantId=" + tenantId +
                ", operatorId=" + operatorId +
                ", operatorName='" + operatorName + '\'' +
                ", appId='" + appId + '\'' +
                ", organization='" + organization + '\'' +
                ", variable=" + variable +
                ", avatars=" + avatars +
                ", type='" + type + '\'' +
                '}';
    }

    /**
     * 同步版本号
     */



    private String wgNumber;
    private Long syncVersion;
    private String sex;


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    private Date createTime;
    private Date updateTime;
    private Long tenantId;
    /**
     * 操作者id
     */
    private Long operatorId;
    /**
     * 操作账号
     */
    private String operatorName;
    private String appId;
    private String organization;
    /**
     * 扩展字段map
     */
    private Map<String, String> variable;
    /**
     * 照片Map包含可见光，近红外
     */
    private Map<ResourceType, List<String>> avatars;
    /**
     * 人员类型
     */
    private String type;

    public static enum ResourceType {
        VISIBLE_LIGHT, NEAR_INFRARED
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Long getSyncVersion() {
        return syncVersion;
    }

    public void setSyncVersion(Long syncVersion) {
        this.syncVersion = syncVersion;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Map<String, String> getVariable() {
        return variable;
    }

    public void setVariable(Map<String, String> variable) {
        this.variable = variable;
    }

    public Map<ResourceType, List<String>> getAvatars() {
        return avatars;
    }

    public void setAvatars(Map<ResourceType, List<String>> avatars) {
        this.avatars = avatars;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
