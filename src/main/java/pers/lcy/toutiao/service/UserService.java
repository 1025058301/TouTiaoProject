package pers.lcy.toutiao.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.lcy.toutiao.dao.LoginTicketMapper;
import pers.lcy.toutiao.dao.UserMapper;
import pers.lcy.toutiao.model.LoginTicket;
import pers.lcy.toutiao.model.User;
import pers.lcy.toutiao.util.CommonUtil;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    public User getUser(int id){
        return userMapper.selectUserById(id);
    }

    public Map<String,Object> register(String username,String password){
        Map<String, Object> resMsg = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            resMsg.put("msgname", "用户名不得为空");
            return resMsg;
        }
        if (StringUtils.isBlank(password)) {
            resMsg.put("msgpassword", "密码不得为空");
            return resMsg;
        }
        if (userMapper.selectUserByName(username) != null) {
            resMsg.put("msgname", "用户名重复!!!");
            return resMsg;
        }
        User newUser=new User();
        newUser.setName(username);
        newUser.setSalt(UUID.randomUUID().toString().substring(0,5));
        newUser.setPassword(CommonUtil.MD5(password + newUser.getSalt()));
        newUser.setHeadUrl("/images/img/1.jpg");
        userMapper.addUser(newUser);
        String ticket=addLoginTicket(userMapper.selectUserIdByName(username));
        resMsg.put("ticket",ticket);
        return resMsg;
    }

    public Map<String,Object> login(String username,String password){
        Map<String, Object> resMsg = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            resMsg.put("msgname", "用户名不得为空");
            return resMsg;
        }
        if (StringUtils.isBlank(password)) {
            resMsg.put("msgpassword", "密码不得为空");
            return resMsg;
        }
        User user=userMapper.selectUserByName(username);
        if (user == null) {
            resMsg.put("msgname", "用户名不存在");
            return resMsg;
        }
        if(!CommonUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            resMsg.put("msgpwd","密码不正确");
        }
        String ticket=addLoginTicket(user.getId());
        resMsg.put("ticket",ticket);

        return resMsg;
    }

    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }

    private String addLoginTicket(int userId){
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        ticket.setStatus(0);
        loginTicketMapper.insertTicket(ticket);
        return ticket.getTicket();
    }
}
