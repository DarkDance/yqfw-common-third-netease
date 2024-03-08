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
public class UserData implements Serializable {
    private static final long serialVersionUID = -6395407168178327439L;

    @JsonProperty("accid")
    private String username;

    @JsonProperty("name")
    private String nickname;

    @JsonProperty("icon")
    private String avatar;
}
