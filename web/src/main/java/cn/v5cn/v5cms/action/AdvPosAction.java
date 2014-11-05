package cn.v5cn.v5cms.action;

import cn.v5cn.v5cms.biz.AdvPosBiz;
import cn.v5cn.v5cms.entity.AdvPos;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import static cn.v5cn.v5cms.util.MessageSourceHelper.getMessage;

/**
 * Created by ZYW on 2014/7/23.
 */
@Controller
@RequestMapping("/manager/advpos")
public class AdvPosAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdvPosAction.class);
    @Autowired
    private AdvPosBiz advPosBiz;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String advPosList(ModelMap model){
        model.addAttribute("advposs", advPosBiz.finadAll());
        return "backstage/advpos_list";
    }

    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    public String advPosaup(ModelMap model){
        model.addAttribute("advpos",new AdvPos());
        return "backstage/advpos_edit";
    }

    @RequestMapping(value = "/edit/{advPosId}",method = RequestMethod.GET)
    public String advPosaup(@PathVariable Long advPosId,ModelMap model){

        AdvPos result = advPosBiz.findOne(advPosId);
        model.addAttribute("advpos",result);
        model.addAttribute("page_title",getMessage("advpos.updatepage.title"));
        return "backstage/advpos_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public ImmutableMap<String,Object> addUpdateAdvPos(@Valid AdvPos advPos,BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            Map<String, Object> errorMessage = Maps.newHashMap();
            errorMessage.put("status", 0);
            List<FieldError> fes = bindingResult.getFieldErrors();
            for (FieldError fe : fes) {
                errorMessage.put("message",fe.getField()+","+fe.getDefaultMessage());
            }
            return ImmutableMap.<String, Object>builder().putAll(errorMessage).build();
        }
        //新增操作
        if(advPos.getAdvPosId() == null){
            AdvPos result = advPosBiz.save(advPos);
            if(result.getAdvPosId() !=null && result.getAdvPosId() != 0L){
                LOGGER.info("新增广告版位成功，{}",result);
                return ImmutableMap.<String, Object>of("status","1","message",getMessage("advpos.addsuccess.message"));
            }
            LOGGER.warn("新增广告版位失败，{}",result);
            return ImmutableMap.<String, Object>of("status","0","message",getMessage("advpos.addfailed.message"));
        }
        //修改操作
        AdvPos updateAdvPos = null;
        try {
            updateAdvPos = advPosBiz.update(advPos);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            LOGGER.error("修改广告版位失败，{},失败堆栈错误：{}",advPos,e.getMessage());
            return ImmutableMap.<String, Object>of("status","0","message",getMessage("advpos.updatefailed.message"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LOGGER.error("修改广告版位失败，{},失败堆栈错误：{}",advPos,e.getMessage());
            return ImmutableMap.<String, Object>of("status","0","message",getMessage("advpos.updatefailed.message"));
        }
        LOGGER.info("修改广告版位成功，{}",updateAdvPos);
        return ImmutableMap.<String, Object>of("status","1","message",getMessage("advpos.updatesuccess.message"));
    }

    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ImmutableMap<String,String> deleteAdvPos(Long[] advPosIds){
        LOGGER.info("删除广告版位，ID为{}",advPosIds);
        try {
            advPosBiz.deleteAdvPos(advPosIds);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("删除广告版位失败，ID为{}",advPosIds);
            return ImmutableMap.of("status","0","message",getMessage("advpos.deletefailed.message"));
        }
        return ImmutableMap.of("status","1","message",getMessage("advpos.deletesuccess.message"));
    }
}
