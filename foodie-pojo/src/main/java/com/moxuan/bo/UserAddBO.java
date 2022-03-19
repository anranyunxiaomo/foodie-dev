package com.moxuan.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserAddBO extends UserBO {

    /**
     * 二次密码
     */
    @NotBlank(message = "密码不能为空")
    private String confirmPassword;

}
