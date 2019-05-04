package com.liaoyb.qingqing.uaa.service.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link com.liaoyb.qingqing.uaa.domain.Role} entity.
 */
@Data
public class RoleDTO implements Serializable {

    private Long id;

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
     * 启用状态：0->禁用；1->启用
     */
    @NotNull
    @ApiModelProperty(value = "启用状态：0->禁用；1->启用", required = true)
    private Integer activeStatus;

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


    private Set<PermissionDTO> permissions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoleDTO roleDTO = (RoleDTO) o;
        if (roleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", activeStatus=" + getActiveStatus() +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
