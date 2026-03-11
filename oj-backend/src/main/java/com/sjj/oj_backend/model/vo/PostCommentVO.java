package com.sjj.oj_backend.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class PostCommentVO implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long postId;

    private String content;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private Date createTime;

    private Date updateTime;

    private UserVO user;

    private static final long serialVersionUID = 1L;
}
