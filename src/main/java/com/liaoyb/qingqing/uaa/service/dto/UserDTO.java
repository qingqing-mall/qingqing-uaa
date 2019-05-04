package com.liaoyb.qingqing.uaa.service.dto;
import com.liaoyb.qingqing.uaa.domain.Role;
import com.liaoyb.qingqing.uaa.domain.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link com.liaoyb.qingqing.uaa.domain.User} entity.
 */
@Data
public class UserDTO implements Serializable {

    private Long id;

    /**
     * 用户名
     */
    @NotNull
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    /**
     * 昵称
     */
    @NotNull
    @ApiModelProperty(value = "昵称", required = true)
    private String nickname;

    /**
     * 邮箱
     */
    @NotNull
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;

    /**
     * 密码
     */
    @NotNull
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    /**
     * 手机号码
     */
    @NotNull
    @ApiModelProperty(value = "手机号码", required = true)
    private String phone;

    /**
     * 头像
     */
    @NotNull
    @ApiModelProperty(value = "头像", required = true)
    private String imageUrl;

    /**
     * 性别：0->未知；1->男；2->女
     */
    @NotNull
    @ApiModelProperty(value = "性别：0->未知；1->男；2->女", required = true)
    private Integer gender;

    /**
     * 生日
     */
    @NotNull
    @ApiModelProperty(value = "生日", required = true)
    private Instant birthday;

    /**
     * 所在城市
     */
    @NotNull
    @ApiModelProperty(value = "所在城市", required = true)
    private String city;

    /**
     * 语言
     */
    @Size(min = 2, max = 20)
    @ApiModelProperty(value = "语言")
    private String langKey;

    /**
     * 帐号启用状态:0->禁用；1->启用
     */
    @NotNull
    @ApiModelProperty(value = "帐号启用状态:0->禁用；1->启用", required = true)
    private Integer activeStatus;

    /**
     * 帐号激活状态:0-未激活；1-已激活
     */
    @NotNull
    @ApiModelProperty(value = "帐号激活状态:0-未激活；1-已激活", required = true)
    private Integer activationStatus;

    /**
     * 激活key
     */
    @Size(max = 20)
    @ApiModelProperty(value = "激活key")
    private String activationKey;

    /**
     * 重置密码key
     */
    @Size(max = 20)
    @ApiModelProperty(value = "重置密码key")
    private String resetKey;

    /**
     * 重置密码时间(有效期24小时)
     */
    @ApiModelProperty(value = "重置密码时间(有效期24小时)")
    private Instant resetDate;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private Long createdBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Instant createdDate;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private Long lastModifiedBy;

    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "最后更新时间")
    private Instant lastModifiedDate;

    private Set<String> roles;


    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.imageUrl = user.getImageUrl();
        this.gender = user.getGender();
        this.birthday = user.getBirthday();
        this.city = user.getCity();
        this.langKey = user.getLangKey();
        this.activeStatus = user.getActiveStatus();

        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserDTO userDTO = (UserDTO) o;
        if (userDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
