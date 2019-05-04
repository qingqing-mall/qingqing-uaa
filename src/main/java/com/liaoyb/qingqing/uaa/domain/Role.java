package com.liaoyb.qingqing.uaa.domain;



import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * 角色
 *
 * @author liaoyb
 */
@Data
@Entity
@Table(name = "qingqing_role")
public class Role extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
     * 启用状态：0->禁用；1->启用
     */
    @NotNull
    @Column(name = "active_status")
    private Integer activeStatus;

    @ManyToMany
    @JoinTable(name = "qingqing_role_permission_relation",
               joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Set<Permission> permissions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return id != null && id.equals(((Role) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", activeStatus=" + activeStatus +
                '}';
    }
}
