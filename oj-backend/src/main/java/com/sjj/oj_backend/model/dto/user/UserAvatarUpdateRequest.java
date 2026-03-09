package com.sjj.oj_backend.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 更新用户头像请求
 */
@Data
public class UserAvatarUpdateRequest implements Serializable {

    /**
     * 头像访问地址
     */
    private String userAvatar;

    private static final long serialVersionUID = 1L;
}