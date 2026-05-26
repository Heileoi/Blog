package com.xilei.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 友情链接实体类
 * 对应数据库表：tb_friend_link
 * 功能：管理博客的友情链接，支持排序和状态控制
 */
@Data
@TableName("tb_friend_link")
public class FriendLink {

    /** 友链ID，自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 网站名称 */
    private String name;

    /** 网站链接 */
    private String url;

    /** 网站Logo */
    private String logo;

    /** 网站描述 */
    private String description;

    /** 联系邮箱 */
    private String email;

    /** 排序值 */
    private Integer sortOrder;

    /** 状态：0-待审核，1-已通过，2-已拒绝 */
    private Integer status;

    /** 逻辑删除标记 */
    @TableLogic
    private Integer isDeleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
