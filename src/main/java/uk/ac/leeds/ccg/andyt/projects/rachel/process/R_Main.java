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
package uk.ac.leeds.ccg.andyt.projects.rachel.process;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
import uk.ac.leeds.ccg.andyt.geotools.Geotools_Shapefile;
import uk.ac.leeds.ccg.andyt.geotools.demo.Geotools_CreatePointShapefile;
import uk.ac.leeds.ccg.andyt.geotools.demo.Geotools_DisplayShapefile;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_2D_ID_long;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Dimensions;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridBinary;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridBinaryFactory;
import uk.ac.leeds.ccg.andyt.grids.core.grid.chunk.Grids_GridChunkBinary;
import uk.ac.leeds.ccg.andyt.grids.core.grid.chunk.Grids_GridChunkBinaryFactory;
import uk.ac.leeds.ccg.andyt.grids.core.grid.stats.Grids_GridBinaryStats;
import uk.ac.leeds.ccg.andyt.projects.rachel.core.R_Object;

/**
 *
 * @author geoagdt
 */
public class R_Main extends R_Object {

    public R_Main() throws IOException {
        super();
    }

    public static void main(String[] args) {
        try {
            R_Main m = new R_Main();
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
        long seed;
        seed = 1569939910914L;
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
        File inF;
        double v0 = 0d;
        double v1 = 1d;
        if (runTest) {
            nv0 = 2;
            nv1 = 2;
            inF = new File(env.files.getInputDir(), "test.asc");
            //inF = new File(env.files.getInputDir(), "test2.asc");
        } else {
            nv0 = 167000;
            nv1 = 333000;
            inF = new File(env.files.getInputDir(), "col_def_0_1.asc");
        }

        // Initialise the array for the shapefiles to display.
        ArrayList<File> sfs = new ArrayList<>();

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
            Grids_GridChunkBinaryFactory cf = new Grids_GridChunkBinaryFactory();
            Grids_Dimensions d = null;
            Grids_GridBinaryFactory f = new Grids_GridBinaryFactory(ge, cf, 1000,
                    1000, d, new Grids_GridBinaryStats(ge), v);
            File genF = ge.env.io.createNewFile(ge.files.getGeneratedGridBinaryDir(), "" + (int) v, "grid");
            //File genF = new File(ge.files.getGeneratedGridBinaryDir(), "" + (int) v);
            genF.mkdirs();
            Grids_GridBinary g = f.create(genF, inF);
            long n = g.getStats().getN();
            System.out.println(g);
            /**
             * Get the selection set. v = 0 -----
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
                    Grids_GridChunkBinary chunk = (Grids_GridChunkBinary) g.getChunk(cri, cci);
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
            File sf = createShapefile(TYPE, lv, (int) v, g);
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
     * @param TYPE The SimpleFeatureType for the points that will be created.
     * @param l List of locations to turn into points.
     * @param v Effectively this is a label for naming the shapefile.
     * @param g A grid for helping to convert the locations in l into
     * coordinates.
     * @return File for storing the shapefile.
     * @throws IOException
     */
    public File createShapefile(SimpleFeatureType TYPE,
            ArrayList<Grids_2D_ID_long> l, int v, Grids_GridBinary g) throws IOException {
        String sfName = "out" + v + ".shp";
        File f = new File(env.files.getOutputDir(), sfName);
        System.out.println("creating " + f);
        List<SimpleFeature> features = new ArrayList<>();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(TYPE);
        GeometryFactory gF = JTSFactoryFinder.getGeometryFactory();
        Iterator<Grids_2D_ID_long> ite = l.iterator();
        while (ite.hasNext()) {
            Grids_2D_ID_long id = ite.next();
            //System.out.println(id);
            //double lon = getCellX(id.getRow());
            //double lat = getCellY(id.getCol());
            double lon = g.getCellXDouble(id.getCol());
            double lat = g.getCellYDouble(id.getRow());
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
        params.put("url", f.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore ds = (ShapefileDataStore) dSFactory.createNewDataStore(params);

        /*
         * TYPE is used as a template to describe the file contents
         */
        ds.createSchema(TYPE);
        File outSF = getShapefile(env.files.getOutputDir(), sfName);
        //Geotools_Shapefile.transact(outSF, TYPE, features, dSFactory);
        Geotools_Shapefile.transact(ds, TYPE, features);
        return outSF;
    }

    /**
     *
     * @param dir The directory in which the shapefile will be created.
     * @param sfName The name that will be given to the shapefile directory.
     * @return A file for storing a shapefile.
     */
    public File getShapefile(File dir, String sfName) {
        File sfDir = new File(dir, sfName);
        if (!sfDir.exists()) {
            sfDir.mkdirs();
        }
        return new File(sfDir, sfName);
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
