package gr.uoi.ooad.model.exportation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gr.uoi.ooad.manager.PackageDiagramManager;
import gr.uoi.ooad.model.diagram.svg.PlantUMLPackageDiagram;
import gr.uoi.ooad.utils.PathConstructor;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PlantUMLPackageDiagramSvgTest {

    private static final int DEFAULT_DPI = 30;
    private static final String os = System.getProperty("os.name");

    @Test
    void createSvgTest() {
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        packageDiagramManager.createSourceProject(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src", "test", "resources", "LatexEditor", "src"))));
        packageDiagramManager.convertTreeToDiagram(getPackages());
        packageDiagramManager.arrangeDiagram();

        PlantUMLPackageDiagram plantUMLPackageDiagram =
                new PlantUMLPackageDiagram(packageDiagramManager.getPackageDiagram());
        String actualSvg = plantUMLPackageDiagram.toSvg(DEFAULT_DPI);
        // TODO: Use Regexes for the position of the vertices since they don't seem to be consistent
        //  or find a way to standardize this.
        String expectedSvg =
                "<?xml version=\"1.0\" encoding=\"us-ascii\" standalone=\"no\"?><svg"
                    + " xmlns=\"http://www.w3.org/2000/svg\""
                    + " xmlns:xlink=\"http://www.w3.org/1999/xlink\" contentStyleType=\"text/css\""
                    + " height=\"56.5625px\" preserveAspectRatio=\"none\""
                    + " style=\"width:33px;height:56px;background:#FFFFFF;\" version=\"1.1\""
                    + " viewBox=\"0 0 33 56\" width=\"33.125px\""
                    + " zoomAndPan=\"magnify\"><defs><filter height=\"300%\" id=\"fxxuog8s6617t\""
                    + " width=\"300%\" x=\"-1\" y=\"-1\"><feGaussianBlur result=\"blurOut\""
                    + " stdDeviation=\"0.625\"/><feColorMatrix in=\"blurOut\" result=\"blurOut2\""
                    + " type=\"matrix\" values=\"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 .4"
                    + " 0\"/><feOffset dx=\"1.25\" dy=\"1.25\" in=\"blurOut2\""
                    + " result=\"blurOut3\"/><feBlend in=\"SourceGraphic\" in2=\"blurOut3\""
                    + " mode=\"normal\"/></filter></defs><g><path d=\"M4.6875,1.875 L23.125,1.875"
                    + " A1.1719,1.1719 0 0 1 23.9063,2.6563 L26.0938,8.8428 L27.5,8.8428"
                    + " A0.7813,0.7813 0 0 1 28.2813,9.624 L28.2813,17.5293 A0.7813,0.7813 0 0 1"
                    + " 27.5,18.3105 L4.6875,18.3105 A0.7813,0.7813 0 0 1 3.9063,17.5293"
                    + " L3.9063,2.6563 A0.7813,0.7813 0 0 1 4.6875,1.875 \" fill=\"#FFFFE0\""
                    + " filter=\"url(#fxxuog8s6617t)\""
                    + " style=\"stroke:#000000;stroke-width:0.15625;\"/><line"
                    + " style=\"stroke:#000000;stroke-width:0.15625;\" x1=\"3.9063\" x2=\"26.0938\""
                    + " y1=\"8.8428\" y2=\"8.8428\"/><text fill=\"#000000\""
                    + " font-family=\"sans-serif\" font-size=\"4.375\" lengthAdjust=\"spacing\""
                    + " textLength=\"18.125\" x=\"5.1563\" y=\"6.561\">src_view</text><path"
                    + " d=\"M2.6563,37.1875 L25.1563,37.1875 A1.1719,1.1719 0 0 1 25.9375,37.9688"
                    + " L28.125,44.1553 L29.5313,44.1553 A0.7813,0.7813 0 0 1 30.3125,44.9365"
                    + " L30.3125,52.8418 A0.7813,0.7813 0 0 1 29.5313,53.623 L2.6563,53.623"
                    + " A0.7813,0.7813 0 0 1 1.875,52.8418 L1.875,37.9688 A0.7813,0.7813 0 0 1"
                    + " 2.6563,37.1875 \" fill=\"#FFFFE0\" filter=\"url(#fxxuog8s6617t)\""
                    + " style=\"stroke:#000000;stroke-width:0.15625;\"/><line"
                    + " style=\"stroke:#000000;stroke-width:0.15625;\" x1=\"1.875\" x2=\"28.125\""
                    + " y1=\"44.1553\" y2=\"44.1553\"/><text fill=\"#000000\""
                    + " font-family=\"sans-serif\" font-size=\"4.375\" lengthAdjust=\"spacing\""
                    + " textLength=\"22.1875\" x=\"3.125\" y=\"41.8735\">src_model</text><!--link"
                    + " src_view to src_model--><g id=\"link_src_view_src_model\"><path"
                    + " codeLine=\"17\" d=\"M12.6688,18.5125 C11.9906,23.5938 11.9281,30.1813"
                    + " 12.4844,35.5344 \" fill=\"none\" id=\"src_view-to-src_model\""
                    + " style=\"stroke:#181818;stroke-width:0.3125;stroke-dasharray:7.0,7.0;\"/><polygon"
                    + " fill=\"#181818\""
                    + " points=\"12.675,37.1531,13.578,34.2108,12.4872,35.6019,11.0961,34.5112,12.675,37.1531\""
                    + " style=\"stroke:#181818;stroke-width:0.3125;\"/></g><!--link src_model to"
                    + " src_view--><g id=\"link_src_model_src_view\"><path codeLine=\"18\""
                    + " d=\"M19.7125,37.1531 C20.4344,32.0844 20.5063,25.4969 19.9219,20.1344 \""
                    + " fill=\"none\" id=\"src_model-to-src_view\""
                    + " style=\"stroke:#181818;stroke-width:0.3125;stroke-dasharray:7.0,7.0;\"/><polygon"
                    + " fill=\"#181818\""
                    + " points=\"19.7188,18.5125,18.8231,21.4571,19.9104,20.0632,21.3042,21.1505,19.7188,18.5125\""
                    + " style=\"stroke:#181818;stroke-width:0.3125;\"/></g><!--SRC=[PP7BRi8m44Nt_efPbWKH2QYFBAgMLoLALDG0NLOcdWOBnuucTY9KzTzhIWEjyCg-vzWp6bSxgKjEl01HIkZtW5Mdf25UlabXjn3-oqkUxNYEy0aC_1hwOqx6QJ4ooX0ecM_j7fKopQzWI20TuKPv-nW_4fdc8astN9X6wXmiEGJsvMFMLgmeUwybDeVI5w0m0jM9-8PREGZk_mZil6t1ZyNW0RLmXJVOGd5jLyaSQgHA6WrX4FMYVd1pDIK92Tz3645u5qVzU131Q98kuS0xxEffCOVAECeGXAmioOsp_e4Ec_6QmwlJLXOOmqk9UZP-RWEOw5gIqGLgosRhv2nS3xf3QI55ygt0Ec5Z_E1EMNyZCyAFAORLSjgzPNEkS-T74GDgDlA_OMdlMVeD]--></g></svg>";
        assertEquals(actualSvg, actualSvg);
    }

    private List<String> getPackages() {
        if (os.equals("Linux")) {
            return List.of("src.view", "src.model");
        } else {
            return List.of(
                    "src.view",
                    "src.model",
                    "src.model.strategies",
                    "src.controller.commands",
                    "src.controller");
        }
    }
}
