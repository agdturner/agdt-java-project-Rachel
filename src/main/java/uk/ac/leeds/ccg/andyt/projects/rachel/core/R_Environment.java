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

import java.io.IOException;
import java.io.Serializable;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.projects.rachel.io.R_Files;

/**
 *
 * @author geoagdt
 */
public class R_Environment implements Serializable {

    public transient final Generic_Environment env;

    public transient final Grids_Environment ge;
    
    public transient final R_Files files;
    
    public R_Environment() throws IOException {
        this(new Grids_Environment());
    }

    public R_Environment(Grids_Environment ge) {
        this(new R_Files(), ge);
    }
    
    public R_Environment(R_Files files, Grids_Environment ge) {
        this.ge = ge;
        this.env = ge.env;
        this.files = files;
    }
}
