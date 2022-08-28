package com.minos.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.minos.constant.MessageConstant;
import com.minos.entity.PageResult;
import com.minos.entity.QueryPageBean;
import com.minos.entity.Result;
import com.minos.pojo.Setmeal;
import com.minos.service.SetmealService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 体检套餐管理
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    //使用JedisPool操作Redis服务
//    @Autowired
//    private JedisPool jedisPool;

    //文件上传
//    @RequestMapping("/upload")
//    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
//        System.out.println(imgFile);
//        String originalFilename = imgFile.getOriginalFilename();//原始文件名 3bd90d2c-4e82-42a1-a401-882c88b06a1a2.jpg
//        int index = originalFilename.lastIndexOf(".");
//        String extention = originalFilename.substring(index - 1);//.jpg
//        String fileName = UUID.randomUUID().toString() + extention;//	FuM1Sa5TtL_ekLsdkYWcf5pyjKGu.jpg
//        try {
//            //将文件上传到七牛云服务器
//            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
//            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
//        }
//        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
//    }

    @Reference
    private SetmealService setmealService;

    //新增套餐
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try{
            setmealService.add(setmeal,checkgroupIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.pageQuery(queryPageBean);
    }
}
