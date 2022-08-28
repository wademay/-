package com.minos.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.minos.constant.MessageConstant;
import com.minos.entity.PageResult;
import com.minos.entity.QueryPageBean;
import com.minos.entity.Result;
import com.minos.pojo.Order;
import com.minos.service.OrderSettingListService;
import com.minos.utils.SMSUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 预约列表控制层
 */
@RestController
@RequestMapping("/ordersettinglist")
public class OrderSettingListController {
    @Reference
    private OrderSettingListService orderSettingListService;

    @Reference
//    private OrderService orderService;

    // 日期格式化
    public static String formatDateStr(String dateString){
        String[] ts = dateString.split("T")[0].split("-");
        int number = Integer.parseInt(ts[2]);
        ++number;
        ts[2] = String.valueOf(number);
        return ts[0] + "-" + ts[1] + "-" + ts[2];
    }

    // 预约列表分页查询
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return orderSettingListService.pageQuery(queryPageBean);
    }


    // 添加一条新的预约记录
    @PostMapping("/add")
    public Result add(String setmealId, @RequestBody Map map){

        Result result = new Result(false, MessageConstant.ORDER_FAIL);

        try {
            map.put("setmealId",setmealId);

            // 格式化预约日期与生日的格式
            String str = (String) map.get("orderDate");
            map.put("orderDate", formatDateStr(str));

            str = (String) map.get("birthday");
            map.put("birthday", formatDateStr(str));

            // 进行预约的业务处理
            map.put("orderType", Order.ORDERTYPE_TELEPHONE);
//            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }

        if (result.isFlag()) {      // 预约成功  发送短信进行通知
            String orderDate = (String) map.get("orderDate");
            try {
                // 无法发送通知   只能用验证码代替
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, map.get("telephone").toString(), orderDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    // 确定预约 修改预约的状态
    @PutMapping("/updateStatus")
    public Result updateStatus(int orderId){
        try{
            orderSettingListService.updateStatusByOrderId(orderId);

            return new Result(true, MessageConstant.ORDER_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }

    // 删除一条预约信息
    @PostMapping("/delete")
    public Result delete(@RequestBody Map map){
        try{
            orderSettingListService.delete(map);

            return new Result(true, MessageConstant.ORDER_CANCEL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_CANCEL_FAIL);
        }
    }
}
