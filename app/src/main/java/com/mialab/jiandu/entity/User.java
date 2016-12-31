package com.mialab.jiandu.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 用户
 */
@DatabaseTable(tableName = "user")
public class User {

    @DatabaseField(id = true)
    private String phone;
    @DatabaseField
    private String username;
    @DatabaseField
    private String headPic;
    @DatabaseField
    private String blogAddress;
    @DatabaseField
    private String introduction;
    @DatabaseField
    private String sex;
    @DatabaseField
    private String job;
    @DatabaseField
    private long registerTime;
    @DatabaseField
    private int collectionNum;
    @DatabaseField
    private int readNum;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getBlogAddress() {
        return blogAddress;
    }

    public void setBlogAddress(String blogAddress) {
        this.blogAddress = blogAddress;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(int collectionNum) {
        this.collectionNum = collectionNum;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }
}
