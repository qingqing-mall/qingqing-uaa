package com.liaoyb.qingqing.uaa.enums;

import lombok.Getter;

/**
 * 用户激活状态
 *
 * @author liaoyb
 */
@Getter
public enum UserActivationStatus {
    NOT_ACTIVATED(0, "未激活"),
    ACTIVATED(1, "已激活");

    private Integer value;
    private String description;

    UserActivationStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
