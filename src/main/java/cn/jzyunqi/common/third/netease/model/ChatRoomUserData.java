package cn.jzyunqi.common.third.netease.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2018/7/27.
 */
@Getter
@Setter
public class ChatRoomUserData implements Serializable {
    private static final long serialVersionUID = 4122641270432418577L;

    @JsonProperty("roomid")
    private String room;

    @JsonProperty("accid")
    private String username;

    @JsonProperty("nick")
    private String nickname;

    @JsonProperty("avator")
    private String avatar;

    private String type;

    @JsonProperty("onlineStat")
    private Boolean onlineStatus;

    @JsonProperty("enterTime")
    private Long enterTime;

}
