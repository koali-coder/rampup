package com.example.demo.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author zhouyw
 * @since 2021-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Demo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;


}
