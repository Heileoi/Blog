package com.xilei.blog.dto;

import lombok.Data;

/**
 * 用户信息DTO
 * 功能：封装用户个人信息更新请求
 */
@Data
public class UserDTO {

    /** 昵称 */
    private String nickname;

    /** 邮箱 */
    private String email;

    /** 头像URL */
    private String avatar;

    /** 个人简介 */
    private String bio;

    /** 个人网站 */
    private String website;

    /** GitHub地址 */
    private String github;
}
