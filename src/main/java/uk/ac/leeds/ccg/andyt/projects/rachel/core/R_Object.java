/*
 * Copyright 2019 Centre for Computational Geography.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.andyt.projects.rachel.core;

import java.io.Serializable;

/**
 *
 * @author geoagdt
 */
public class R_Object implements Serializable {
    
    public R_Environment env;
    
    public R_Object() {
        this(new R_Environment());
    }
    
    public R_Object(R_Environment e) {
        env = e;
    }
    
}
