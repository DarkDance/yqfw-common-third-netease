package cn.jzyunqi.common.third.netease.response;

import cn.jzyunqi.common.third.netease.model.ChatRoomUserData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @date 2018/7/27.
 */
@Getter
@Setter
public class ChatRoomResult implements Serializable {
    private static final long serialVersionUID = 4122641270432418577L;

    private List<ChatRoomUserData> data;
}
