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
package uk.ac.leeds.ccg.projects.rachel.process;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.generic.io.Generic_Path;
import uk.ac.leeds.ccg.geotools.Geotools_Shapefile;
import uk.ac.leeds.ccg.geotools.demo.Geotools_CreatePointShapefile;
import uk.ac.leeds.ccg.geotools.demo.Geotools_DisplayShapefile;
import uk.ac.leeds.ccg.grids.d2.Grids_2D_ID_long;
import uk.ac.leeds.ccg.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.grids.d2.grid.b.Grids_GridBinary;
import uk.ac.leeds.ccg.grids.d2.chunk.b.Grids_ChunkBinaryArray;
import uk.ac.leeds.ccg.grids.process.Grids_Processor;
import uk.ac.leeds.ccg.projects.rachel.core.R_Environment;
import uk.ac.leeds.ccg.projects.rachel.core.R_Object;

/**
 * R_Main
 * 
 * @author Andy Turner
 * @version 1.0.0
 */
public class R_Main extends R_Object {

    public R_Main(R_Environment e) throws IOException {
        super(e);
    }

    public static void main(String[] args) {
        try {
            Path d = Paths.get(System.getProperty("user.home"), "data", 
                    "project", "Rachel");
            R_Environment e = new R_Environment(new Grids_Environment(
                new Generic_Environment(new Generic_Defaults(d))));
            R_Main m = new R_Main(e);
            m.run();
        } catch (Error | Exception e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * The main runner.
     * @throws Exception 
     */
    public void run() throws Exception {
        long t0 = System.currentTimeMillis();
        long seed = 1569939910914L;
        //seed = t0;
        System.out.println("seed " + seed);
        Random rand = new Random(t0);

        // Main switches
        boolean displayResult;
        displayResult = false;
        //displayResult = true;
        boolean runTest;
        //runTest = false;
        runTest = true;
        
        // Parameters
        int nv0;
        int nv1;
        Path inF;
        double v0 = 0d;
        double v1 = 1d;
        if (runTest) {
            nv0 = 2;
            nv1 = 2;
            inF = Paths.get(env.files.getInputDir().toString(), "test.asc");
            //inF = Paths.get(env.files.getInputDir(), "test2.asc");
        } else {
            nv0 = 167000;
            nv1 = 333000;
            inF = Paths.get(env.files.getInputDir().toString(), "col_def_0_1.asc");
        }

        // Initialise the array for the shapefiles to display.
        ArrayList<Path> sfs = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            // Initialise parameters
            int nv;
            double v;
            if (i == 0) {
                nv = nv0;
                v = 0d;
            } else {
                nv = nv1;
                v = 1d;
            }
            // Initialise grid
            Grids_Environment ge = new Grids_Environment();
            Grids_Processor gp = new Grids_Processor(ge); 
//            Grids_ChunkFactoryBinary cf = new Grids_GridChunkBinaryFactory();
//            Grids_Dimensions d = null;
//            Grids_GridBinaryFactory f = new Grids_GridBinaryFactory(ge, cf, 1000,
//                    1000, d, new Grids_GridBinaryStats(ge), v);
//            Path genF = ge.env.io.createNewFile(ge.files.getGeneratedGridBinaryDir(), "" + (int) v, "grid");
//            //Path genF = Paths.get(ge.files.getGeneratedGridBinaryDir(), "" + (int) v);
//            genF.mkdirs();
//            Grids_GridBinary g = f.create(genF, inF);
            Grids_GridBinary g = gp.gridFactoryBinary.create(new Generic_Path(inF));
            long n = g.getStats().getN().longValueExact();
            System.out.println(g);
            /**
             * Get the selection set.
             * v = 0 -----
             * Grids_GridBinary[ChunkNcols=512, ChunkNrows=512, NChunkCols=118,
             * NChunkRows=133, NCols=60197, NRows=67711,
             * Directory=C:\Users\geoagdt\src\agdt\java\projects\agdt-java-project-Rachel\data\generated\data\generated\Grids\GridBinary\0,
             * Name=0, Dimensions=Grids_Dimensions[XMin=-81.722556798426,
             * XMax=-66.673450067286, YMin=-4.253559568628,
             * YMax=12.674029279192, Cellsize=0.00024999762],
             * ChunkIDChunkMap.size()=5091, Grids.size()=1,
             * Grids_GridBinaryStatsNotUpdated[N=1176394687]]
             *
             * v = 1
             * ----- Grids_GridBinary[ChunkNcols=512, ChunkNrows=512,
             * NChunkCols=118, NChunkRows=133, NCols=60197, NRows=67711,
             * Directory=C:\Users\geoagdt\src\agdt\java\projects\agdt-java-project-Rachel\data\generated\data\generated\Grids\GridBinary\1,
             * Name=1, Dimensions=Grids_Dimensions[XMin=-81.722556798426,
             * XMax=-66.673450067286, YMin=-4.253559568628,
             * YMax=12.674029279192, Cellsize=0.00024999762],
             * ChunkIDChunkMap.size()=5091, Grids.size()=1,
             * Grids_GridBinaryStatsNotUpdated[N=38857924]]
             */
            TreeSet<Integer> s = null;
            if (n < Integer.MAX_VALUE) {
                s = getSelection(nv, (int) n, rand);
                System.out.println("s.size() " + s.size());
            } else {
                String m = "n >= " + Integer.MAX_VALUE;
                System.out.println(m);
                throw new Exception(m);
            }
            /**
             * Go through the grid and find locations of the selection set lv.
             */
            ArrayList<Grids_2D_ID_long> lv = new ArrayList<>();
            int nChunkCols = g.getNChunkCols();
            int nChunkRows = g.getNChunkRows();
            Iterator<Integer> ite = s.iterator();
            int pos = ite.next();
            int index = 0;
            for (int cri = 0; cri < nChunkRows; cri++) {
                System.out.println("cri " + cri);
                for (int cci = 0; cci < nChunkCols; cci++) {
                    //System.out.println("cci " + cci);
                    Grids_ChunkBinaryArray chunk = (Grids_ChunkBinaryArray) g.getChunk(cri, cci);
                    if (chunk != null) {
                        int chunkNCols = g.getChunkNCols(cci);
                        int chunkNRows = g.getChunkNRows(cri);
                        for (int r = 0; r < chunkNRows; r++) {
                            for (int c = 0; c < chunkNCols; c++) {
                                if (chunk.getCell(r, c)) {
                                    if (index == pos) {
                                        long row = (cri * g.getChunkNRows()) + r;
                                        long col = (cci * g.getChunkNCols()) + c;
                                        lv.add(g.getCellID(row, col));
                                        if (ite.hasNext()) {
                                            pos = ite.next();
                                        }
                                    }
                                    index++;
                                }
                            }
                        }
                    }
                    chunk = null; // To free memory
                    g.env.checkAndMaybeFreeMemory();
                }
            }
            System.out.println("lv.size() " + lv.size());
            /**
             * convert the location set into a point shapefile
             */
            SimpleFeatureType TYPE = getSimpleFeatureType();
            Path sf = createShapefile(TYPE, lv, (int) v, g);
            sfs.add(sf);
        }
        /**
         * Display the resulting shapefiles.
         */
        if (displayResult) {
            int displayWidth = 600;
            int displayHeight = 500;
            try {
                Geotools_DisplayShapefile ds = new Geotools_DisplayShapefile();
                ds.displayShapefiles(sfs, displayWidth, displayHeight, null);
            } catch (Exception ex) {
                Logger.getLogger(R_Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Creates the point shapefile.
     *
     * @param sft The SimpleFeatureType for the points that will be created.
     * @param l List of locations to turn into points.
     * @param v Effectively this is a label for naming the shapefile.
     * @param g A grid for helping to convert the locations in l into
     * coordinates.
     * @return Path for storing the shapefile.
     * @throws IOException
     */
    public Path createShapefile(SimpleFeatureType sft,
            ArrayList<Grids_2D_ID_long> l, int v, Grids_GridBinary g) throws IOException {
        String sfName = "out" + v + ".shp";
        Path f = Paths.get(env.files.getOutputDir().toString(), sfName);
        System.out.println("creating " + f);
        List<SimpleFeature> features = new ArrayList<>();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(sft);
        GeometryFactory gF = JTSFactoryFinder.getGeometryFactory();
        Iterator<Grids_2D_ID_long> ite = l.iterator();
        while (ite.hasNext()) {
            Grids_2D_ID_long id = ite.next();
            //System.out.println(id);
            //double lon = getCellX(id.getRow());
            //double lat = getCellY(id.getCol());
            double lon = g.getCellX(id.getCol()).doubleValue();
            double lat = g.getCellY(id.getRow()).doubleValue();
            Point point = gF.createPoint(new Coordinate(lon, lat));
            fb.add(point);
            //System.out.println("lat " + lat + ", lon " + lon);
            String name = id.getRow() + "_" + id.getCol();
            //System.out.println(name);
            fb.add(name);
            SimpleFeature feature = fb.buildFeature(null);
            features.add(feature);
        }
        /*
         * Get an output file name and create the new shapefile
         */
        ShapefileDataStoreFactory dSFactory = new ShapefileDataStoreFactory();
        Map<String, Serializable> params = new HashMap<>();
        params.put("url", f.toUri().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore ds = (ShapefileDataStore) dSFactory.createNewDataStore(params);

        /*
         * TYPE is used as a template to describe the file contents
         */
        ds.createSchema(sft);
        Path outSF = getShapefile(env.files.getOutputDir(), sfName);
        //Geotools_Shapefile.transact(outSF, TYPE, features, dSFactory);
        Geotools_Shapefile.transact(ds, sft, features);
        return outSF;
    }

    /**
     *
     * @param dir The directory in which the shapefile will be created.
     * @param sfName The name that will be given to the shapefile directory.
     * @return A file for storing a shapefile.
     * @throws java.io.IOException If encountered.
     */
    public Path getShapefile(Path dir, String sfName) throws IOException {
        Path sfDir = Paths.get(dir.toString(), sfName);
        if (!Files.exists(sfDir)) {
            Files.createDirectories(sfDir);
        }
        return Paths.get(sfDir.toString(), sfName);
    }

    /**
     * @return A SimpleFeatureType for a point with a srid of 4326 for a WGS84
     * geographical projection. The points have a string attribute called name.
     */
    public SimpleFeatureType getSimpleFeatureType() {
        SimpleFeatureType r = null;
        try {
            r = DataUtilities.createType("POINT",
                    "the_geom:Point:srid=4326," + "name:String");
        } catch (SchemaException ex) {
            Logger.getLogger(Geotools_CreatePointShapefile.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return r;
    }

    /**
     * Creates and returns a pseudo randomly selected set of n integers in the
     * range [0,N). The largest integer returned would be N - 1.
     *
     * @param n The n to be selected.
     * @param N The total.
     * @param rand The Random for pseudo-random selection.
     * @return A set with length n.
     */
    public TreeSet<Integer> getSelection(int n, int N, Random rand) {
        TreeSet<Integer> r = new TreeSet<>();
        int[] i = rand.ints(0, N).distinct().limit(n).toArray();
        for (int j = 0; j < i.length; j++) {
            r.add(i[j]);
        }
        return r;
    }
}
