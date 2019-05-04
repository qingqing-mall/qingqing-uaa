package com.liaoyb.qingqing.uaa.domain;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liaoyb.qingqing.uaa.enums.UserActiveStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 用户
 *
 * @author liaoyb
 */
@Data
@Entity
@Table(name = "qingqing_user")
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @NotNull
    @Column(name = "username")
    private String username;

    /**
     * 昵称
     */
    @NotNull
    @Column(name = "nickname")
    private String nickname;

    /**
     * 邮箱
     */
    @NotNull
    @Column(name = "email")
    private String email;

    /**
     * 密码
     */
    @NotNull
    @Column(name = "jhi_password")
    private String password;

    /**
     * 手机号码
     */
    @NotNull
    @Column(name = "phone")
    private String phone;

    /**
     * 头像
     */
    @NotNull
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * 性别：0->未知；1->男；2->女
     */
    @NotNull
    @Column(name = "gender")
    private Integer gender;

    /**
     * 生日
     */
    @NotNull
    @Column(name = "birthday")
    private Instant birthday;

    /**
     * 所在城市
     */
    @NotNull
    @Column(name = "city")
    private String city;

    /**
     * 语言
     */
    @Size(min = 2, max = 20)
    @Column(name = "lang_key")
    private String langKey;

    /**
     * 帐号启用状态:0->禁用；1->启用
     */
    @NotNull
    @Column(name = "active_status")
    private Integer activeStatus;

    /**
     * 帐号激活状态:0-未激活；1-已激活
     */
    @NotNull
    @Column(name = "activation_status")
    private Integer activationStatus;

    /**
     * 激活key
     */
    @Size(max = 20)
    @Column(name = "activation_key")
    private String activationKey;

    /**
     * 重置密码key
     */
    @Size(max = 20)
    @Column(name = "reset_key")
    private String resetKey;

    /**
     * 重置密码时间(有效期24小时)
     */
    @Column(name = "reset_date")
    private Instant resetDate;

    /**
     * 角色
     */
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "qingqing_user_role_relation",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    /**
     * 是否激活状态
     *
     * @return
     */
    public boolean activated(){
        return Objects.equals(UserActiveStatus.ENABLED.getValue(), activeStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return !(user.getId() == null || getId() == null) && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", city='" + city + '\'' +
                ", langKey='" + langKey + '\'' +
                ", activeStatus=" + activeStatus +
                ", activationStatus=" + activationStatus +
                ", activationKey='" + activationKey + '\'' +
                ", resetKey='" + resetKey + '\'' +
                ", resetDate=" + resetDate +
                '}';
    }
}
