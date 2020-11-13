package pers.lcy.toutiao.controller;

import com.sun.org.apache.bcel.internal.generic.DDIV;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.lcy.toutiao.model.Message;
import pers.lcy.toutiao.model.ViewObject;
import pers.lcy.toutiao.service.MessageService;
import pers.lcy.toutiao.service.ViewObjectService;
import pers.lcy.toutiao.util.CommonUtil;
import pers.lcy.toutiao.util.HostHolder;

import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    public static Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;

    @Autowired
    ViewObjectService viewObjectService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/addMessage", method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content){
        try{
            Message message=new Message();
            message.setContent(content);
            message.setFromId(fromId);
            message.setToId(toId);
            message.setCreatedDate(new Date());
            message.setConversationId(fromId > toId ? toId + "-" + fromId : fromId + "-" + toId);
            message.setHasRead(0);
            messageService.addMessage(message);
            return CommonUtil.getJsonString(0);
        }catch (Exception e){
            return CommonUtil.getJsonString(1,"添加消息出错");
        }
    }
    @RequestMapping(value="/msg/detail")
    public String getConversationDetail(@RequestParam("conversationId") String conversationId,
                                        Model model){
        try {
            int localUserId=hostHolder.get().getId();
            List<ViewObject> messages=viewObjectService.getConversationDetailView(conversationId);
            model.addAttribute("messages",messages);
            messageService.setMessageReadStatus(localUserId,conversationId);
            return "letterDetail";
        }catch (Exception e){
            logger.error("展示对话细节出错"+e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(value = "/msg/list",method = {RequestMethod.GET})
    public String getConversationList(Model model){
        try {
            int localUserId=hostHolder.get().getId();
            List<ViewObject> conversations=viewObjectService.getConversationListView(localUserId);
            model.addAttribute("conversations",conversations);
        }catch (Exception e){
            logger.error("展示对话列表错误"+e.getMessage());
        }
        return "letter";
    }

}
