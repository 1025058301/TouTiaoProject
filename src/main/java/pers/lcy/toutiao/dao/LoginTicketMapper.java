package pers.lcy.toutiao.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pers.lcy.toutiao.model.LoginTicket;
import sun.security.krb5.internal.Ticket;
@Mapper
public interface LoginTicketMapper {

    int insertTicket(LoginTicket ticket);

    LoginTicket selectByTicket(@Param("ticket") String ticket);

    void updateStatus(@Param("ticket") String ticket,@Param("status") int status);

}