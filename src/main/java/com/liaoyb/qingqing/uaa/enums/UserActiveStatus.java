package com.liaoyb.qingqing.uaa.enums;

import lombok.Getter;

/**
 * 用户状态
 *
 * @author liaoyb
 */
@Getter
public enum UserActiveStatus {
    DISABLED(0, "禁用"),
    ENABLED(1, "启用");

    private Integer value;
    private String description;

    UserActiveStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
