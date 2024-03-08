package cn.jzyunqi.common.third.netease.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @date 2018/7/27.
 */
@Getter
@AllArgsConstructor
public enum UserType {

    /**
     * 固定成员
     */
    FIXED(0),

    /**
     * 临时成员
     */
    TEMP(1),

    /**
     * 在线固定成员
     */
    FIXED_ONLINE(2),
    ;

    /**
     * 类型
     */
    private Integer type;
}
