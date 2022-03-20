package com.moxuan.controller;

import cn.hutool.core.util.StrUtil;
import com.moxuan.bo.AddressBO;
import com.moxuan.bo.AddressUpdateBO;
import com.moxuan.pojo.UserAddress;
import com.moxuan.service.UserAddressService;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {


    @Autowired
    private UserAddressService addressService;

    /**
     * 根据用户id查询收货地址列表
     */
    @PostMapping("/list")
    public BaseResp<List<UserAddress>> list(@RequestParam String userId) {
        if (StrUtil.isBlankIfStr(userId)) {
            return ResultUtil.error("");
        }
        List<UserAddress> list = addressService.lambdaQuery().eq(UserAddress::getUserId, userId).list();
        return ResultUtil.ok(list);
    }

    /**
     * 用户新增地址
     */
    @PostMapping("/add")
    public BaseResp add(@RequestBody @Validated AddressBO addressBO) {
        return addressService.add(addressBO);
    }


    /**
     * 用户修改地址
     */
    @PostMapping("/update")
    public BaseResp update(@RequestBody @Validated AddressUpdateBO addressUpdateBO) {
        return addressService.renew(addressUpdateBO);
    }

    /**
     * 用户删除地址
     */
    @PostMapping("/delete")
    public BaseResp delete(@RequestParam String userId,
                           @RequestParam String addressId) {
        return addressService.delete(userId, addressId);
    }

    /**
     * 用户设置默认地址
     */
    @PostMapping("/setDefalut")
    public BaseResp setDefalut(@RequestParam String userId,
                               @RequestParam String addressId) {
        return addressService.setDefalut(userId, addressId);
    }

}
