package com.looseboxes.webform.thym;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes={
            com.looseboxes.webform.thym.WebformApplication.class, 
            com.looseboxes.webform.WebformBasePackageClass.class})
class WebformApplicationSmokeIT {
	@Test
	void contextLoads() {
	}
}
