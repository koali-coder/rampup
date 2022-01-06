package com.example.demo.service.impl;

import com.example.demo.entity.Demo;
import com.example.demo.mapper.DemoMapper;
import com.example.demo.service.IDemoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouyw
 * @since 2021-06-11
 */
@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo> implements IDemoService {

}
