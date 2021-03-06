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
package uk.ac.leeds.ccg.projects.rachel.core;

import java.io.IOException;
import java.io.Serializable;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.projects.rachel.io.R_Files;

/**
 * R_Environment
 * 
 * @author Andy Turner
 * @version 1.0.0
 */
public class R_Environment implements Serializable {

    private static final long serialVersionUID = 1L;

    public transient final Generic_Environment env;

    public transient final Grids_Environment ge;
    
    public transient final R_Files files;
    
    public R_Environment(Grids_Environment ge) throws IOException {
        this.ge = ge;
        this.env = ge.env;
        this.files = new R_Files(ge.files.getDir().getParent());
    }
}
