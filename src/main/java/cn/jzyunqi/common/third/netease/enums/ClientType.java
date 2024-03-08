package cn.jzyunqi.common.third.netease.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @date 2018/9/21.
 */
@Getter
@AllArgsConstructor
public enum ClientType {

    /**
     * 客户端为web端时使用
     */
    weblink(1),

    /**
     * 客户端为非web端时使用
     */
    commonlink(2),

    /**
     * 微信小程序使用
     */
    wechatlink(3),
    ;

    /**
     * 类型
     */
    private Integer type;
}
