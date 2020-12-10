package com.alonelyleaf.oauth.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * @author bijl
 * @date 2020/11/25 4:58 下午
 */
@Data
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 客户端id
     */
    //@TableField(value = "client_id")
    private String clientId;
    /**
     * 客户端密钥
     */
    private String clientSecret;
    /**
     * 资源集合
     */
    private String resourceIds;
    /**
     * 授权范围
     */
    private String scope;
    /**
     * 授权类型
     */
    private String authorizedGrantTypes;
    /**
     * 回调地址
     */
    private String webServerRedirectUri;
    /**
     * 权限
     */
    private String authorities;
    /**
     * 令牌过期秒数
     */
    private Integer accessTokenValidity;
    /**
     * 刷新令牌过期秒数
     */
    private Integer refreshTokenValidity;
    /**
     * 附加说明
     */
    private String additionalInformation;
    /**
     * 自动授权
     */
    private String autoapprove;
}
