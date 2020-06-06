/*
 * Copyright 2020 looseBoxes.com
 *
 * Licensed under the looseBoxes Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.looseboxes.webform.thym;

import com.looseboxes.webform.thym.controllers.FormControllerHtmlImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author USER
 */
@SpringBootTest(
        classes={
            com.looseboxes.webform.thym.WebformApplication.class, 
            com.looseboxes.webform.WebformBasePackageClass.class})

public class ControllersSmokeIT {

    @Autowired private FormControllerHtmlImpl formController;
    
    @Test
    public void application_WhenRun_ShouldLoadFormController() throws Exception {
        System.out.println("application_WhenRun_ShouldLoadFormController");
        assertThat(formController).isNotNull();
    }
}