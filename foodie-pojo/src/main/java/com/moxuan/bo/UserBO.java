package com.moxuan.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserBO {
    /**
     * 用户名称
     */
    @NotBlank(message = "用户名称不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;


}
