/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package Map;

import GameObjects.ACameraComponent;
import GraFX.USpriteSheet;
import Map.Tiles.UMapTileLayer;
import Map.Tiles.UTileLayerCollisionLayer;
import Map.Tiles.UTileLayerPropsLayer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Tile manager
 * <p>
 * In charge of handling all the layers of our game!
 */
public final class UMapTileManager {

    /**
     * collision layer index
     *
     * @deprecated Unused variable on this update
     */
    private static final int COLLISION_LAYER = 2;

    /**
     * Collision Layer name
     */
    private final static String CollisionLayerName = "PlayerCollision";

    /**
     * The World Camera
     * <p>
     * see one of the fallowing to understand why the camera is needed
     *
     * @see UTileLayerCollisionLayer
     * @see UTileLayerPropsLayer
     */
    private ACameraComponent CameraActor;

    /**
     * Stores the different layers
     */
    public static ArrayList<UMapTileLayer> TileManager;

    /**
     * Default constructor
     */
    private UMapTileManager() {
        TileManager = new ArrayList<UMapTileLayer>();
    }

    /**
     * Constructor
     *
     * @param MapXMLPath  The Map XML file to build from
     * @param tileWidth   The Width of each tile
     * @param tileHeight  The Height of each tile
     * @param WorldCamera the Wold camera
     */
    public UMapTileManager(String MapXMLPath, int tileWidth, int tileHeight, ACameraComponent WorldCamera) {
        TileManager = new ArrayList<UMapTileLayer>();
        CameraActor = WorldCamera;
        AddTileToMap(MapXMLPath, tileWidth, tileHeight, WorldCamera);
    }

    /**
     * Builds the map
     *
     * @param XMLMap      The Map XML file to build from
     * @param TileWidth   The Width of each tile
     * @param TileHeight  The Height of each tile
     * @param WorldCamera the Wold camera
     */
    protected final void AddTileToMap(String XMLMap, int TileWidth, int TileHeight, ACameraComponent WorldCamera) {
        String MapPath = null;

        int width = 0;
        int height = 0;
        int MapTileW;
        int MapTileH;
        int AmountOfTiles;
        int TileColumns;
        USpriteSheet Sprite;

        /**
         * We consider that our map does not have more than 10 layers!
         */
        String[] UMapLayerData = new String[10];

        try {
            //load our xml file
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            System.out.printf("Loading MAP: %s \n", XMLMap);

            File XMLMAPFILE = new File(XMLMap);
            Document document = documentBuilder.parse(XMLMAPFILE);
            document.getDocumentElement().normalize();

            //get our tileset tag
            NodeList Elements = document.getElementsByTagName("tileset");


            Node node = Elements.item(0);
            Element element = null;

            element = (Element) node;

            //Find the tileset document to extract data from
            Document TileSetXML = documentBuilder.parse(new File(XMLMAPFILE.getParentFile() + "/" + element.getAttribute("source").replace(".tsx", ".xml")));

            Elements = TileSetXML.getElementsByTagName("tileset");
            node = Elements.item(0);
            element = (Element) node;

            if (element != null) {
                //MapPath = ((Element)node.getChildNodes().item(0)).getElementsByTagName("image").item(0).

                //pull data from the tileset XML file xml
                MapPath = XMLMAPFILE.getParentFile() + "/" + ((Element) element.getElementsByTagName("image").item(0)).getAttribute("source");
                MapTileW = Integer.parseInt(element.getAttribute("tilewidth"));
                MapTileH = Integer.parseInt(element.getAttribute("tileheight"));
                TileColumns = Integer.parseInt(element.getAttribute("columns"));

                //Load the tileset Sheet
                Sprite = new USpriteSheet(MapPath, MapTileW, MapTileH, true);

                //get the layer data from the original XML
                Elements = document.getElementsByTagName("layer");

                AmountOfTiles = Elements.getLength();

                for (int LayerIndex = 0; LayerIndex < AmountOfTiles; LayerIndex++) {

                    node = Elements.item(LayerIndex);
                    element = (Element) node;

                    //get the map size, we only need to do this once as all layers belong to the same map !
                    if (LayerIndex <= 0) {
                        width = Integer.parseInt(element.getAttribute("width"));
                        height = Integer.parseInt(element.getAttribute("height"));
                    }

                    //store the current layer data
                    UMapLayerData[LayerIndex] = element.getElementsByTagName("data").item(0).getTextContent();

                    //if its a collisionlayer
                    if (element.getAttribute("name").equals(CollisionLayerName)) {
                        TileManager.add(new UTileLayerCollisionLayer(Sprite, UMapLayerData[LayerIndex], width, height, TileWidth, TileHeight, TileColumns));
                    } else {
                        TileManager.add(new UTileLayerPropsLayer(Sprite, UMapLayerData[LayerIndex], width, height, TileWidth, TileHeight, TileColumns));
                    }

                    //set the camera viewport world bounds
                    WorldCamera.setCameraLimits(TileWidth * width, height * TileHeight);

                }

            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tells each layer to render
     *
     * @param g The graphics to render on
     */
    public void PrepareDataForRender(Graphics2D g) {
        if (CameraActor == null) return;
        for (UMapTileLayer uMapTileLayer : TileManager) {
            uMapTileLayer.RenderTile(g, CameraActor.getCameraCollision());
        }
    }

}
