package cn.jzyunqi.common.third.netease.response;

import cn.jzyunqi.common.third.netease.model.UserData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wiiyaya
 * @date 2018/7/27.
 */
@Getter
@Setter
public class GetUserInfoRsp extends NeteaseBaseRsp<Object> {
    private static final long serialVersionUID = 6972161432624468099L;

    @JsonProperty("uinfos")
    private List<UserData> userList;
}
