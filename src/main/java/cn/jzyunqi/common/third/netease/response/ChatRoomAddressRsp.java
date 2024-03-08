package cn.jzyunqi.common.third.netease.response;

import cn.jzyunqi.common.third.netease.response.NeteaseBaseRsp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wiiyaya
 * @date 2018/9/21.
 */
@Getter
@Setter
public class ChatRoomAddressRsp extends NeteaseBaseRsp<Object> {
    private static final long serialVersionUID = 7197459204868837814L;

    @JsonProperty("addr")
    private List<String> addressList;

}
