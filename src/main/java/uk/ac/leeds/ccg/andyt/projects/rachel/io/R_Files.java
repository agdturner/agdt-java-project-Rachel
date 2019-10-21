/*
 * Copyright 2019 Centre for Computational Geography, University of Leeds.
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
package uk.ac.leeds.ccg.andyt.projects.rachel.io;

import java.io.File;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_Files;
import uk.ac.leeds.ccg.andyt.projects.rachel.core.R_Strings;

/**
 *
 * @author geoagdt
 */
public class R_Files extends Generic_Files {
    
    public R_Files(){
        super(Generic_Defaults.getDefaultDir());        
    }
    
    /**
     * @param dataDir
     * @return A directory called {@link R_Strings#String_s_Rachel} 
     * in {@code dataDir}.
     */
    public static File getDir(File dataDir) {
        File r = new File(dataDir, R_Strings.s_Rachel);
        r.mkdir();
        return r;
    }

    public R_Files(File dataDir) {
        super(getDir(dataDir));
    }
}
