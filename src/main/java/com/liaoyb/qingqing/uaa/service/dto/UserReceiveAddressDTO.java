package com.liaoyb.qingqing.uaa.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.liaoyb.qingqing.uaa.domain.UserReceiveAddress} entity.
 */
@Data
public class UserReceiveAddressDTO implements Serializable {

    private Long id;

    /**
     * 用户id
     */
    @NotNull
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    /**
     * 收货人名称
     */
    @NotNull
    @ApiModelProperty(value = "收货人名称", required = true)
    private String name;

    /**
     * 收货人电话
     */
    @NotNull
    @ApiModelProperty(value = "收货人电话", required = true)
    private String phone;

    /**
     * 是否为默认(0-否，1-是)
     */
    @NotNull
    @ApiModelProperty(value = "是否为默认(0-否，1-是)", required = true)
    private Integer defaultStatus;

    /**
     * 邮政编码
     */
    @NotNull
    @ApiModelProperty(value = "邮政编码", required = true)
    private String postCode;

    /**
     * 省份/直辖市
     */
    @NotNull
    @ApiModelProperty(value = "省份/直辖市", required = true)
    private String province;

    /**
     * 城市
     */
    @NotNull
    @ApiModelProperty(value = "城市", required = true)
    private String city;

    /**
     * 区
     */
    @NotNull
    @ApiModelProperty(value = "区", required = true)
    private String region;

    /**
     * 详细地址(街道)
     */
    @NotNull
    @ApiModelProperty(value = "详细地址(街道)", required = true)
    private String detailAddress;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserReceiveAddressDTO userReceiveAddressDTO = (UserReceiveAddressDTO) o;
        if (userReceiveAddressDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userReceiveAddressDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
