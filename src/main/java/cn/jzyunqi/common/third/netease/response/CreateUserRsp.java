package cn.jzyunqi.common.third.netease.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @date 2018/6/1.
 */
@Getter
@Setter
public class CreateUserRsp extends NeteaseBaseRsp<Object> {
    private static final long serialVersionUID = -7286768083377141607L;

    private Info info;

    @Getter
    @Setter
    public static class Info {

        @JsonProperty("accid")
        private String username;

        @JsonProperty("token")
        private String password;

        @JsonProperty("name")
        private String nickname;
    }
}
