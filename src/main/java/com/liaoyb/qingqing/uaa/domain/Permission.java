package com.liaoyb.qingqing.uaa.domain;



import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * 用户权限
 *
 * @author liaoyb
 */
@Data
@Entity
@Table(name = "qingqing_permission")
public class Permission extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 父级权限id：0表示一级
     */
    @NotNull
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 名称
     */
    @NotNull
    @Column(name = "name")
    private String name;


    /**
     * 描述
     */
    @NotNull
    @Column(name = "description")
    private String description;

    /**
     * 图标
     */
    @NotNull
    @Column(name = "icon")
    private String icon;

    /**
     * 权限类型：0->目录；1->菜单；2->按钮 3->接口
     */
    @NotNull
    @Column(name = "jhi_type")
    private Integer type;

    /**
     * 方法：GET、POST、PUT、DELETE、PATCH
     */
    @NotNull
    @Column(name = "method")
    private Integer method;

    /**
     * 排序
     */
    @NotNull
    @Column(name = "jhi_sort")
    private Integer sort;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Permission)) {
            return false;
        }
        return id != null && id.equals(((Permission) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
