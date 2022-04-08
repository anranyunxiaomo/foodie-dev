package com.moxuan.service.center;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.common.CenterUserBO;
import com.moxuan.config.UserFaceProperties;
import com.moxuan.mapper.UsersMapper;
import com.moxuan.pojo.Users;
import com.moxuan.pojo.mapper.center.CenterUserBeanMapper;
import com.moxuan.service.UsersService;
import com.moxuan.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
public class CenterUserService extends ServiceImpl<UsersMapper, Users> {

    @Autowired
    private BindingResultUtil bindingResultUtil;
    @Resource
    private CenterUserBeanMapper centerUserBeanMapper;
    @Resource
    private FileUtils fileUtils;
    @Resource
    private UserFaceProperties userFaceProperties;
    @Autowired
    private UsersService usersService;


    /**
     * 用户中心-用户信息
     */
    public BaseResp userInfo(String userId) {
        Users users = this.lambdaQuery().setEntity(new Users())
                // 查询时，排除密码
                .select(c -> !Objects.equals(c.getProperty(), "password"))
                .eq(Users::getId, userId)
                .one();
        return ResultUtil.ok(users);
    }

    /**
     * 修改用户信息
     */
    public BaseResp updateUserInfo(String userId,
                                   CenterUserBO centerUserBO,
                                   BindingResult result,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        // 校验字段是否错误
        if (result.hasErrors()) {
            return ResultUtil.error(JSONUtil.toJsonStr(bindingResultUtil.getErrors(result)));
        }
//        // 更新用户信息
        Users users = centerUserBeanMapper.combination(centerUserBO, userId);
        updateSelect(userId, request, response, users);
        return ResultUtil.ok();
    }

    /**
     * 更新用户的头像
     */
    public BaseResp uploadFace(String userId, MultipartFile multipartFile, HttpServletRequest request, HttpServletResponse response) {
        String finalFacePath = null;
        try {
            // 校验用户ID是否存在
            if (StrUtil.isBlankIfStr(userId)) {
                throw new CustomException(ResultUtil.error("用户ID不能为空"));
            }
            // 校验文件是否存在
            if (ObjectUtil.isNull(multipartFile)) {
                throw new CustomException(ResultUtil.error("头像文件不存在"));
            }
            // 校验文件的格式是否正确
            String fileType = fileUtils.getFileType(multipartFile);
            if (!(fileType.equalsIgnoreCase("jpg") ||
                    fileType.equalsIgnoreCase("png") ||
                    fileType.equalsIgnoreCase("jpeg"))) {
                return ResultUtil.error("图片格式不正确！");
            }
            finalFacePath = fileUtils.fileNameSplicingPath(userId, userFaceProperties.getPathPrefix(), fileType);
            File file = new File(userFaceProperties.getDriveLetter() + finalFacePath);
            if (file.getParentFile() != null) {
                // 创建文件夹
                file.getParentFile().mkdirs();
            }
            multipartFile.transferTo(file);
            multipartFile=null;
        } catch (IOException e) {
            throw new CustomException(ResultUtil.error("存储文件失败"));
        }
        // 生成对应的文件的名称
        Users users = Users.builder()
                .id(userId)
                .face(userFaceProperties.getImageServerUrl() + finalFacePath + "?+" + DateUtil.currentSeconds())
                .build();
        updateSelect(userId, request, response, users);
        return ResultUtil.ok();

    }

    /**
     * 相同方法提取
     */

    private void updateSelect(String userId, HttpServletRequest request, HttpServletResponse response, Users users) {
        this.updateById(users);
        users = this.lambdaQuery().eq(Users::getId, userId)
                .select(Users::getUsername, Users::getId, Users::getFace, Users::getSex)
                .one();
        // 解决分布式用户会话同步的问题。
        usersService.generateUserInfo(request,response,users);
    }

}
