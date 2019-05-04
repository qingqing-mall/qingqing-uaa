package com.liaoyb.qingqing.uaa.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.liaoyb.qingqing.uaa.domain.Permission} entity.
 */
@Data
public class PermissionDTO implements Serializable {

    private Long id;

    /**
     * 父级权限id：0表示一级
     */
    @NotNull
    @ApiModelProperty(value = "父级权限id：0表示一级", required = true)
    private Long parentId;

    /**
     * 名称
     */
    @NotNull
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * 描述
     */
    @NotNull
    @ApiModelProperty(value = "描述", required = true)
    private String description;

    /**
     * 图标
     */
    @NotNull
    @ApiModelProperty(value = "图标", required = true)
    private String icon;

    /**
     * 权限类型：0->目录；1->菜单；2->按钮 3->接口
     */
    @NotNull
    @ApiModelProperty(value = "权限类型：0->目录；1->菜单；2->按钮 3->接口", required = true)
    private Integer type;

    /**
     * 方法：GET、POST、PUT、DELETE、PATCH
     */
    @NotNull
    @ApiModelProperty(value = "方法：GET、POST、PUT、DELETE、PATCH", required = true)
    private Integer method;

    /**
     * 排序
     */
    @NotNull
    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

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

        PermissionDTO permissionDTO = (PermissionDTO) o;
        if (permissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), permissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
