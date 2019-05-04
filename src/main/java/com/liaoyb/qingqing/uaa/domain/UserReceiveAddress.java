package com.liaoyb.qingqing.uaa.domain;


import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户收货地址
 *
 * @author liaoyb
 */
@Data
@Entity
@Table(name = "qingqing_user_receive_address")
public class UserReceiveAddress extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    @NotNull
    @Column(name = "user_id")
    private Long userId;

    /**
     * 收货人名称
     */
    @NotNull
    @Column(name = "name")
    private String name;

    /**
     * 收货人电话
     */
    @NotNull
    @Column(name = "phone")
    private String phone;

    /**
     * 是否为默认(0-否，1-是)
     */
    @NotNull
    @Column(name = "default_status")
    private Integer defaultStatus;

    /**
     * 邮政编码
     */
    @NotNull
    @Column(name = "post_code")
    private String postCode;

    /**
     * 省份/直辖市
     */
    @NotNull
    @Column(name = "province")
    private String province;

    /**
     * 城市
     */
    @NotNull
    @Column(name = "city")
    private String city;

    /**
     * 区
     */
    @NotNull
    @Column(name = "region")
    private String region;

    /**
     * 详细地址(街道)
     */
    @NotNull
    @Column(name = "detail_address")
    private String detailAddress;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserReceiveAddress)) {
            return false;
        }
        return id != null && id.equals(((UserReceiveAddress) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
