package com.example.ggxiaozhi.factory.model.db;

import com.example.ggxiaozhi.factory.model.Author;
import com.example.ggxiaozhi.factory.model.db.base.BaseDbModel;
import com.example.ggxiaozhi.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;
import java.util.Objects;

/**
 * 工程名 ： ITalker
 * 包名   ： com.example.ggxiaozhi.factory.model.db
 * 作者名 ： 志先生_
 * 日期   ： 2017/11
 * 功能   ：服务器返回的用户基本信息Model
 */
@Table(database = AppDatabase.class)
public class User extends BaseDbModel<User> implements Author {
    public static final int SEX_MAN = 1;//男人
    public static final int SEX_WOMAN = 2;//女人
    @PrimaryKey
    private String id;

    //用户名
    @Column
    private String name;

    //电话号
    @Column
    private String phone;

    //头像
    @Column
    private String portrait;

    //描述(相当于个性签名)
    @Column
    private String desc;

    //性别
    @Column
    private int sex = 0;

    //用户关注人的数量
    @Column
    private int follows;

    //用户粉丝的数量
    @Column
    private int following;

    //我对某人的备注信息 也应该存储到数据库
    @Column
    private String alias;

    //我与当前User的关系状态，是否关注了这个人
    @Column
    private boolean isFollow;

    //用户信息最后的更新时间
    @Column
    private Date modifyAt;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", portrait='" + portrait + '\'' +
                ", desc='" + desc + '\'' +
                ", sex=" + sex +
                ", follows=" + follows +
                ", following=" + following +
                ", alias='" + alias + '\'' +
                ", isFollow=" + isFollow +
                ", modifyAt=" + modifyAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return sex == user.sex
                && follows == user.follows
                && following == user.following
                && isFollow == user.isFollow
                && Objects.equals(id, user.id)
                && Objects.equals(name, user.name)
                && Objects.equals(phone, user.phone)
                && Objects.equals(portrait, user.portrait)
                && Objects.equals(desc, user.desc)
                && Objects.equals(alias, user.alias)
                && Objects.equals(modifyAt, user.modifyAt);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean isSame(User old) {
        //主要关注Id是否相等
        return this == old || Objects.equals(id, old.id);
    }

    @Override
    public boolean isUiContentsSame(User old) {

        //显示内容是否一样的条件  主要判断 名字 头像 xiingbie 是否已经关注
        return this == old || (
                Objects.equals(this.name, old.name)
                        && Objects.equals(this.portrait, old.portrait)
                        && Objects.equals(this.sex, old.sex)
                        && Objects.equals(this.isFollow, old.isFollow)
        );
    }
}
