package com.example.demo.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author zhouyw
 * @since 2021-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GoldenTestLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String code;

    private String goldenTestCode;

    /**
     * 测试结果
     */
    private Integer status;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求参数
     */
    private String param;

    /**
     * 预期结果
     */
    private String expectedData;

    /**
     * 实际结果
     */
    private String realityData;

    private Integer createUserId;

    private LocalDateTime createTime;

    private Integer updateUserId;

    private LocalDateTime updateTime;


}
