package cn.v5cn.v5cms.action;

import cn.v5cn.v5cms.biz.ContentBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ZYW on 2014/11/17.
 */
@Controller
@RequestMapping("/manager/content")
public class ContentAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentAction.class);

    @Autowired
    private ContentBiz contentBiz;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String contentList(){
        return "content_list";
    }

    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    public String contentEdit(){
        return "content_edit";
    }
}