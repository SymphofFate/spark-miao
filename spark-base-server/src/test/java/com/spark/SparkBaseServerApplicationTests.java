package com.spark;

import cn.hutool.json.JSONUtil;
import com.spark.enums.RequestCodeTypeEnum;
import com.spark.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SparkBaseServerApplicationTests {

	@Test
	void contextLoads() {
        System.out.println(JSONUtil.parse(new Result(401,"",null)));
	}

}
