package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.UserMapper;
import com.example.springboot_vue_back.Utils.CopyUtils;
import com.example.springboot_vue_back.Utils.SnowFlake;
import com.example.springboot_vue_back.domain.User;
import com.example.springboot_vue_back.domain.UserExample;
import com.example.springboot_vue_back.exception.BusinessException;
import com.example.springboot_vue_back.exception.BusinessExceptionCode;
import com.example.springboot_vue_back.req.UserQueryReq;
import com.example.springboot_vue_back.req.UserSaveReq;
import com.example.springboot_vue_back.resp.UserQueryResp;

import com.example.springboot_vue_back.resp.PageResp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.conf.ConnectionUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req) {
//        分页查询=》查询总数+查当前页数据
        PageHelper.startPage(req.getPage(), req.getSize());//第一个参数是页码，第二个参数是每页显示的条数 只对第一个查询语句起作用


        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
     

        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        pageInfo.getTotal();//返回总条数  配合前端可以通过知道总条数查询分页

//        //将User类型转为UserResp类型
//        List<UserResp> userRespList =new ArrayList<>();
//        for (User user : userList) {
//            UserResp userResp = new UserResp();
//            BeanUtils.copyProperties(user,userResp);
//            userRespList.add(userResp);
//        }

        //将User类型转为UserResp类型 使用copyutils工具类
        List<UserQueryResp> userRespList = CopyUtils.copyList(userList, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(userRespList);

        return pageResp;
    }




    public void save(UserSaveReq req){
        User user= CopyUtils.copy(req,User.class);//将请求参数更新为实体
        if (ObjectUtils.isEmpty(req.getId())){//判断是否存在
            //不存在 新增
            User userDB=selectByLoginName(req.getLoginName());//根据登录名查询用户
            if (ObjectUtils.isEmpty(userDB)) {
                //新增
                user.setId(snowFlake.nextId());//将生成的雪花算法赋给id
                userMapper.insert(user);
            }else {
                //已存在  //使用自定义异常类
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        }else {
            //更新
            userMapper.updateByPrimaryKey(user);
        }

    }

    /***
     * 根据登录名查询用户
     * @param loginName
     * @return
     */
    public User selectByLoginName(String loginName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(userExample);
      if (CollectionUtils.isEmpty(userList)){
          return null;
    }else {
          return userList.get(0);
      }
    }

    public void delete(long id){
        userMapper.deleteByPrimaryKey(id);

    }

}
