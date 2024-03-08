package cn.jzyunqi.common.third.netease.client;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.netease.enums.ClientType;
import cn.jzyunqi.common.third.netease.enums.MsgType;
import cn.jzyunqi.common.third.netease.enums.RoleOpt;
import cn.jzyunqi.common.third.netease.enums.UserType;
import cn.jzyunqi.common.third.netease.response.ChatRoomAddressRsp;
import cn.jzyunqi.common.third.netease.response.ChatRoomResult;
import cn.jzyunqi.common.third.netease.model.ChatRoomUserData;
import cn.jzyunqi.common.third.netease.response.CreateChatRoomRsp;
import cn.jzyunqi.common.third.netease.response.CreateUserRsp;
import cn.jzyunqi.common.third.netease.response.GetUserInfoRsp;
import cn.jzyunqi.common.third.netease.response.NeteaseBaseRsp;
import cn.jzyunqi.common.third.netease.model.UserData;
import cn.jzyunqi.common.utils.BooleanUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author wiiyaya
 * @date 2018/6/1.
 */
@Slf4j
public class NeteaseImClient {
    /**
     * 创建用户
     */
    private static final String NETEASE_IM_USER_CREATE = "https://api.netease.im/nimserver/user/create.action";

    /**
     * 获取用户信息
     */
    private static final String NETEASE_IM_USER_INFO = "https://api.netease.im/nimserver/user/getUinfos.action";

    /**
     * 更新用户信息
     */
    private static final String NETEASE_IM_USER_UPDATE = "https://api.netease.im/nimserver/user/updateUinfo.action";

    /**
     * 创建聊天室
     */
    private static final String NETEASE_IM_CHAT_ROOM_CREATE = "https://api.netease.im/nimserver/chatroom/create.action";

    /**
     * 获取聊天室地址
     */
    private static final String NETEASE_IM_CHAT_ROOM_ADDRESS = "https://api.netease.im/nimserver/chatroom/requestAddr.action";

    /**
     * 修改聊天室开/关闭状态
     */
    private static final String NETEASE_IM_CHAT_ROOM_TOGGLE_CLOSE = "https://api.netease.im/nimserver/chatroom/toggleCloseStat.action";

    /**
     * 设置聊天室内用户角色
     */
    private static final String NETEASE_IM_CHAT_ROOM_SET_MEMBER_ROLE = "https://api.netease.im/nimserver/chatroom/setMemberRole.action";

    /**
     * 发送聊天室消息
     */
    private static final String NETEASE_IM_CHAT_ROOM_SEND_MSG = "https://api.netease.im/nimserver/chatroom/sendMsg.action";

    /**
     * 聊天室用户列表
     */
    private static final String NETEASE_IM_CHAT_ROOM_USER_PAGE = "https://api.netease.im/nimserver/chatroom/membersByPage.action";

    /**
     * 更新聊天室用户信息
     */
    private static final String NETEASE_IM_CHAT_ROOM_UPDATE_CHAT_ROOM_ROLE = "https://api.netease.im/nimserver/chatroom/updateMyRoomRole.action";

    /**
     * im账号
     */
    private final String appKey;

    /**
     * im账号密码
     */
    private final String appSecret;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public NeteaseImClient(String appKey, String appSecret, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 创建IM用户
     *
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称
     * @param avatar   头像
     */
    public void createUser(String username, String password, String nickname, String avatar) throws BusinessException {
        CreateUserRsp body;
        try {
            URI createUserUri = new URIBuilder(NETEASE_IM_USER_CREATE)
                    .addParameter("accid", username) //用户名
                    .addParameter("token", password) //密码
                    .addParameter("name", nickname) //昵称
                    .addParameter("icon", avatar) //头像
                    .build();

            RequestEntity<Object> requestEntity = new RequestEntity<>(this.getNeteaseHeader(), HttpMethod.POST, createUserUri);
            ResponseEntity<CreateUserRsp> createUserRsp = restTemplate.exchange(requestEntity, CreateUserRsp.class);
            body = Optional.ofNullable(createUserRsp.getBody()).orElseGet(CreateUserRsp::new);
        } catch (Exception e) {
            log.error("======NeteaseImHelper createUser [{}] other error:", username, e);
            throw new BusinessException("common_error_ne_im_create_user_error");
        }

        if (!StringUtilPlus.equals(body.getCode(), "200")) {
            log.error("======NeteaseImHelper createUser [{}] 200 error, code[{}][{}]:", username, body.getCode(), body.getDesc());
            throw new BusinessException("common_error_ne_im_create_user_failed");
        }
    }

    /**
     * 获取用户信息
     *
     * @param usernameList 用户名列表
     */
    public List<UserData> getUserInfo(List<String> usernameList) throws BusinessException {
        GetUserInfoRsp body;
        try {
            URI sendMsgUri = new URIBuilder(NETEASE_IM_USER_INFO)
                    .addParameter("accids", objectMapper.writeValueAsString(usernameList))
                    .build();

            RequestEntity<Object> requestEntity = new RequestEntity<>(getNeteaseHeader(), HttpMethod.POST, sendMsgUri);
            ResponseEntity<GetUserInfoRsp> getUserInfoRsp = restTemplate.exchange(requestEntity, GetUserInfoRsp.class);
            body = Optional.ofNullable(getUserInfoRsp.getBody()).orElseGet(GetUserInfoRsp::new);
        } catch (Exception e) {
            log.error("======NeteaseImHelper getUinfos [{}] other error:", usernameList, e);
            throw new BusinessException("common_error_ne_im_get_uinfos_error");
        }

        if (StringUtilPlus.equals(body.getCode(), "200")) {
            return body.getUserList();
        } else {
            log.error("======NeteaseImHelper getUinfos [{}] error, code[{}][{}]:", usernameList, body.getCode(), body.getDesc());
            throw new BusinessException("common_error_ne_im_get_uinfos_failed");
        }
    }

    /**
     * 更新用户信息
     *
     * @param username 用户名
     * @param nickname 昵称
     * @param avatar   头像
     */
    public void updateUser(String username, String nickname, String avatar) throws BusinessException {
        NeteaseBaseRsp<Object> body;
        try {
            URI createUserUri = new URIBuilder(NETEASE_IM_USER_UPDATE)
                    .addParameter("accid", username) //) //用户名
                    .addParameter("name", nickname) //昵称
                    .addParameter("icon", avatar) //头像
                    .build();

            RequestEntity<Object> requestEntity = new RequestEntity<>(getNeteaseHeader(), HttpMethod.POST, createUserUri);
            ParameterizedTypeReference<NeteaseBaseRsp<Object>> responseType = new ParameterizedTypeReference<NeteaseBaseRsp<Object>>() {};
            ResponseEntity<NeteaseBaseRsp<Object>> updateUserRsp = restTemplate.exchange(requestEntity, responseType);
            body = Optional.ofNullable(updateUserRsp.getBody()).orElseGet(NeteaseBaseRsp::new);
        } catch (Exception e) {
            log.error("======NeteaseImHelper updateUser [{}] other error:", username, e);
            throw new BusinessException("common_error_ne_im_update_user_info_error");
        }

        if (!StringUtilPlus.equals(body.getCode(), "200")) {
            log.error("======NeteaseImHelper updateUser [{}] 200 error, code[{}][{}]:", username, body.getCode(), body.getDesc());
            throw new BusinessException("common_error_ne_im_update_user_info_failed");
        }
    }

    /**
     * 创建聊天室
     *
     * @param creator      创建者用户名
     * @param chatRoomName 聊天室名称
     * @return 聊天室id
     */
    public Long createChatRoom(String creator, String chatRoomName) throws BusinessException {
        CreateChatRoomRsp body;
        try {
            URI createUserUri = new URIBuilder(NETEASE_IM_CHAT_ROOM_CREATE)
                    .addParameter("creator", creator) //聊天室所有者
                    .addParameter("name", StringUtilPlus.substring(chatRoomName, 0, 127)) //聊天室名称，长度限制128个字符
                    //.addParameter("announcement", "聊天室公告，不知道什么时候出来")
                    .addParameter("queuelevel", "1")//队列管理权限：0:所有人都有权限变更队列，1:只有主播管理员才能操作变更。默认0
                    .build();

            RequestEntity<Object> requestEntity = new RequestEntity<>(getNeteaseHeader(), HttpMethod.POST, createUserUri);
            ResponseEntity<CreateChatRoomRsp> createUserRsp = restTemplate.exchange(requestEntity, CreateChatRoomRsp.class);
            body = Optional.ofNullable(createUserRsp.getBody()).orElseGet(CreateChatRoomRsp::new);
        } catch (Exception e) {
            log.error("======NeteaseImHelper createChatRoom [{}] other error:", creator, e);
            throw new BusinessException("common_error_ne_im_create_chat_room_error");
        }

        if (StringUtilPlus.equals(body.getCode(), "200")) {
            return body.getChatRoom().getRoomId();
        } else {
            log.error("======NeteaseImHelper createChatRoom [{}] error, code[{}][{}]:", creator, body.getCode(), body.getDesc());
            throw new BusinessException("common_error_ne_im_create_chat_room_failed");
        }
    }

    /**
     * 获取聊天室地址
     *
     * @param chatRoomId    聊天室id
     * @param loginUsername 登录用户名
     * @param clientType    客户端类型
     * @param clientIp      客户端id
     * @return 聊天室地址
     */
    public List<String> getChatRoomAddress(String chatRoomId, String loginUsername, ClientType clientType, String clientIp) throws BusinessException {
        ChatRoomAddressRsp body;
        try {
            URI createUserUri = new URIBuilder(NETEASE_IM_CHAT_ROOM_ADDRESS)
                    .addParameter("roomid", chatRoomId) //聊天室id
                    .addParameter("accid", loginUsername) //进入聊天室的账号
                    .addParameter("clienttype", clientType.getType().toString()) //客户端类型
                    .addParameter("clientip", clientIp)//客户端ip
                    .build();

            RequestEntity<Object> requestEntity = new RequestEntity<>(getNeteaseHeader(), HttpMethod.POST, createUserUri);
            ResponseEntity<ChatRoomAddressRsp> chatRoomAddressRsp = restTemplate.exchange(requestEntity, ChatRoomAddressRsp.class);
            body = Optional.ofNullable(chatRoomAddressRsp.getBody()).orElseGet(ChatRoomAddressRsp::new);
        } catch (Exception e) {
            log.error("======NeteaseImHelper getChatRoomAddress [{}] other error:", chatRoomId, e);
            throw new BusinessException("common_error_ne_im_get_chat_room_address_error");
        }

        if (StringUtilPlus.equals(body.getCode(), "200")) {
            return body.getAddressList();
        } else {
            log.error("======NeteaseImHelper getChatRoomAddress [{}] error, code[{}][{}]:", chatRoomId, body.getCode(), body.getDesc());
            throw new BusinessException("common_error_ne_im_get_chat_room_address_failed");
        }
    }

    /**
     * 关闭聊天室
     *
     * @param creator    聊天室所有者
     * @param chatRoomId 聊天室编号
     */
    public void closeChatRoom(String creator, String chatRoomId) throws BusinessException {
        NeteaseBaseRsp<Object> body;
        try {
            URI createUserUri = new URIBuilder(NETEASE_IM_CHAT_ROOM_TOGGLE_CLOSE)
                    .addParameter("operator", creator) //聊天室所有者
                    .addParameter("roomid", chatRoomId) //聊天室id
                    .addParameter("valid", "false") //true或false，false:关闭聊天室；true:打开聊天室
                    .build();

            RequestEntity<Object> requestEntity = new RequestEntity<>(getNeteaseHeader(), HttpMethod.POST, createUserUri);
            ParameterizedTypeReference<NeteaseBaseRsp<Object>> responseType = new ParameterizedTypeReference<NeteaseBaseRsp<Object>>() {};
            ResponseEntity<NeteaseBaseRsp<Object>> createUserRsp = restTemplate.exchange(requestEntity, responseType);
            body = Optional.ofNullable(createUserRsp.getBody()).orElseGet(NeteaseBaseRsp::new);
        } catch (Exception e) {
            log.error("======NeteaseImHelper closeChatRoom [{}] other error:", creator, e);
            throw new BusinessException("common_error_ne_im_toggle_chat_room_close_error");
        }

        if (!StringUtilPlus.equals(body.getCode(), "200")) {
            log.error("======NeteaseImHelper closeChatRoom [{}] 200 error, code[{}][{}]:", creator, body.getCode(), body.getDesc());
            throw new BusinessException("common_error_ne_im_toggle_chat_room_close_failed");
        }
    }

    /**
     * 设置聊天室内用户角色
     *
     * @param chatRoomId 聊天室id
     * @param operator   操作者用户名
     * @param target     操作对象用户名
     * @param opt        操作类型
     * @param cancel     是否取消
     */
    public void setMemberRole(String chatRoomId, String operator, String target, RoleOpt opt, Boolean cancel) throws BusinessException {
        NeteaseBaseRsp<Object> body;
        try {
            URI createUserUri = new URIBuilder(NETEASE_IM_CHAT_ROOM_SET_MEMBER_ROLE)
                    .addParameter("roomid", chatRoomId) //聊天室id
                    .addParameter("operator", operator) //操作者账号accid
                    .addParameter("target", target) //被操作者账号accid
                    .addParameter("opt", opt.getOpt().toString()) //操作
                    .addParameter("optvalue", BooleanUtilPlus.toStringTrueFalse(!cancel)) //true或false，true:设置；false:取消设置
                    .build();

            RequestEntity<Object> requestEntity = new RequestEntity<>(getNeteaseHeader(), HttpMethod.POST, createUserUri);
            ParameterizedTypeReference<NeteaseBaseRsp<Object>> responseType = new ParameterizedTypeReference<NeteaseBaseRsp<Object>>() {};
            ResponseEntity<NeteaseBaseRsp<Object>> createUserRsp = restTemplate.exchange(requestEntity, responseType);
            body = Optional.ofNullable(createUserRsp.getBody()).orElseGet(NeteaseBaseRsp::new);
        } catch (Exception e) {
            log.error("======NeteaseImHelper setMemberRole [{}][{}] other error:", chatRoomId, target, e);
            throw new BusinessException("common_error_ne_im_set_member_role_error");
        }

        if (!StringUtilPlus.equals(body.getCode(), "200")) {
            log.error("======NeteaseImHelper setMemberRole [{}][{}] 200 error, code[{}][{}]:", chatRoomId, target, body.getCode(), body.getDesc());
            throw new BusinessException("common_error_ne_im_set_member_role_failed");
        }
    }

    /**
     * 向聊天室发送消息
     *
     * @param chatRoomId   聊天室id
     * @param fromUsername 发送人名称
     * @param msgType      消息类型
     * @param msg          消息对象
     * @param ext          扩展消息
     */
    public void sendChatRoomMessage(String chatRoomId, String fromUsername, MsgType msgType, String msg, Object ext) throws BusinessException {
        NeteaseBaseRsp<Object> body;
        try {
            String msgId = RandomUtilPlus.String.randomAlphanumeric(30);
            URIBuilder sendMsgUri = new URIBuilder(NETEASE_IM_CHAT_ROOM_SEND_MSG)
                    .addParameter("roomid", chatRoomId) //聊天室id
                    .addParameter("msgId", msgId) //客户端消息id，使用uuid等随机串，msgId相同的消息会被客户端去重
                    .addParameter("fromAccid", fromUsername) //消息发出者的账号accid
                    .addParameter("msgType", msgType.getType().toString()) //消息类型
                    .addParameter("attach", msg); //消息内容

            if (ext != null) {
                sendMsgUri.addParameter("ext", objectMapper.writeValueAsString(ext)); //扩展消息
            }

            RequestEntity<Object> requestEntity = new RequestEntity<>(getNeteaseHeader(), HttpMethod.POST, sendMsgUri.build());
            ParameterizedTypeReference<NeteaseBaseRsp<Object>> responseType = new ParameterizedTypeReference<NeteaseBaseRsp<Object>>() {};
            ResponseEntity<NeteaseBaseRsp<Object>> createUserRsp = restTemplate.exchange(requestEntity, responseType);
            body = Optional.ofNullable(createUserRsp.getBody()).orElseGet(NeteaseBaseRsp::new);
        } catch (Exception e) {
            log.error("======NeteaseImHelper sendChatRoomMessage [{}] other error:", chatRoomId, e);
            throw new BusinessException("common_error_ne_im_send_chat_room_msg_error");
        }

        if (!StringUtilPlus.equals(body.getCode(), "200")) {
            log.error("======NeteaseImHelper sendChatRoomMessage [{}] 200 error, code[{}][{}]:", chatRoomId, body.getCode(), body.getDesc());
            throw new BusinessException("common_error_ne_im_send_chat_room_msg_failed");
        }
    }

    /**
     * 聊天室内成员列表
     *
     * @param chatRoomId 聊天室id
     * @param userType   用户类型
     * @param endTime    最后一个用户进入时间
     * @param limit      获取数量
     * @return 成员列表
     */
    public List<ChatRoomUserData> chatRoomUserPage(String chatRoomId, UserType userType, Long endTime, Integer limit) throws BusinessException {
        NeteaseBaseRsp<ChatRoomResult> body;
        try {
            URI sendMsgUri = new URIBuilder(NETEASE_IM_CHAT_ROOM_USER_PAGE)
                    .addParameter("roomid", chatRoomId)
                    .addParameter("type", userType.getType().toString())
                    .addParameter("endtime", endTime.toString()) //查询时间，最后结果倒序
                    .addParameter("limit", limit.toString()) //最大值100
                    .build();

            RequestEntity<Object> requestEntity = new RequestEntity<>(getNeteaseHeader(), HttpMethod.POST, sendMsgUri);
            ParameterizedTypeReference<NeteaseBaseRsp<ChatRoomResult>> responseType = new ParameterizedTypeReference<NeteaseBaseRsp<ChatRoomResult>>() {};
            ResponseEntity<NeteaseBaseRsp<ChatRoomResult>> chartRoomMemberPageRsp = restTemplate.exchange(requestEntity, responseType);
            body = Optional.ofNullable(chartRoomMemberPageRsp.getBody()).orElseGet(NeteaseBaseRsp::new);
        } catch (Exception e) {
            log.error("======NeteaseImHelper chatRoomUserPage [{}] other error:", chatRoomId, e);
            throw new BusinessException("common_error_ne_im_user_by_page_error");
        }

        if (StringUtilPlus.equals(body.getCode(), "200")) {
            ChatRoomResult chatRoomResult = body.getDesc();
            return chatRoomResult.getData();
        } else {
            log.error("======NeteaseImHelper chatRoomUserPage [{}] error, code[{}][{}]:", chatRoomId, body.getCode(), body.getDesc());
            throw new BusinessException("common_error_ne_im_user_by_page_failed");
        }
    }

    /**
     * 更新聊天室用户信息
     *
     * @param chatRoomId 聊天室id
     * @param username   用户名
     * @param nickname   昵称
     * @param avatar     头像
     */
    public void updateChatRoomRole(String chatRoomId, String username, String nickname, String avatar) throws BusinessException {
        NeteaseBaseRsp<Object> body;
        try {
            URI createUserUri = new URIBuilder(NETEASE_IM_CHAT_ROOM_UPDATE_CHAT_ROOM_ROLE)
                    .addParameter("roomid", chatRoomId)
                    .addParameter("accid", username)
                    .addParameter("nick", nickname)
                    .addParameter("avator", avatar)
                    .build();

            RequestEntity<Object> requestEntity = new RequestEntity<>(getNeteaseHeader(), HttpMethod.POST, createUserUri);
            ParameterizedTypeReference<NeteaseBaseRsp<Object>> responseType = new ParameterizedTypeReference<NeteaseBaseRsp<Object>>() {};
            ResponseEntity<NeteaseBaseRsp<Object>> createUserRsp = restTemplate.exchange(requestEntity, responseType);
            body = Optional.ofNullable(createUserRsp.getBody()).orElseGet(NeteaseBaseRsp::new);
        } catch (Exception e) {
            log.error("======NeteaseImHelper updateChatRoomRole [{}][{}] other error:", chatRoomId, username, e);
            throw new BusinessException("common_error_ne_im_update_chat_room_role_error");
        }

        if (!"200".equals(body.getCode())) {
            log.error("======NeteaseImHelper updateChatRoomRole [{}][{}] 200 error, code[{}][{}]:", chatRoomId, username, body.getCode(), body.getDesc());
            throw new BusinessException("common_error_ne_im_update_chat_room_role_failed");
        }
    }

    /**
     * 查询聊天历史
     *
     * @param chatRoomId   聊天室id
     * @param fromUsername 用户名
     */
    public void queryHist(String chatRoomId, String fromUsername) throws BusinessException {
        try {
            URI sendMsgUri = new URIBuilder("https://api.netease.im/nimserver/history/queryChatroomMsg.action")
                    .addParameter("roomid", chatRoomId)
                    .addParameter("accid", fromUsername)
                    .addParameter("timetag", String.valueOf(System.currentTimeMillis()))
                    .addParameter("limit", "10")
                    .addParameter("type", "0")
                    .build();

            RequestEntity<Object> requestEntity = new RequestEntity<>(getNeteaseHeader(), HttpMethod.POST, sendMsgUri);
            ResponseEntity<Object> createUserRsp = restTemplate.exchange(requestEntity, Object.class);
            createUserRsp.getBody();
        } catch (Exception e) {
            log.error("======NeteaseImHelper queryHist [{}] other error:", chatRoomId, e);
            throw new BusinessException("common_error_ne_im_query_hist_error");
        }
    }

    /**
     * 组装请求Header
     *
     * @return HttpHeaders
     */
    private HttpHeaders getNeteaseHeader() {
        HttpHeaders neteaseHeader = new HttpHeaders();

        String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
        String curTime = String.valueOf(System.currentTimeMillis() / 1000);
        String checkSum = DigestUtilPlus.SHA.sign(appSecret + nonceStr + curTime, DigestUtilPlus.SHAAlgo._1, Boolean.FALSE);

        // 设置请求的header
        neteaseHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        neteaseHeader.set("AppKey", appKey); //im账号appKey
        neteaseHeader.set("Nonce", nonceStr); //随机数（最大长度128个字符）
        neteaseHeader.set("CurTime", curTime); //当前UTC时间戳，从1970年1月1日0点0 分0 秒开始到现在的秒数(String)
        neteaseHeader.set("CheckSum", checkSum); //SHA1(AppSecret + Nonce + CurTime),三个参数拼接的字符串，进行SHA1哈希计算，转化成16进制字符(String，小写)
        return neteaseHeader;
    }
}
