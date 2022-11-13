package com.yida.config;

import com.yida.utils.ParameterMapping;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Auther: yida
 * @Date: 2022/11/13 00:46
 * @Description:
 */
@Data
@AllArgsConstructor
public class BoundSql {
	private String finalSql;
	private List<ParameterMapping> parameterMappingList;
}
