package newChanges;

import freemind.modes.MindMapNode;
import freemind.modes.NodeAdapter;
import freemind.modes.attributes.Attribute;
import freemind.modes.mindmapmode.MindMapController;

import java.awt.*;
import java.awt.event.InputEvent;

public class NodeConverter {

    /*
        translates between root attributes and nodes
     */

    public static boolean NCSOverride = false;
    public static MindMapNode NCSOverride_Node;
    public static String NCSOverride_Title = "";

    public static void updateNodesFromRootData(){
        // get current selected node
        NodeWrapper current = ANSManager.getLastNodeSelected();
        if(current != null){
            // get the root node of that tree
            MindMapNode root = current.getNodeAdapter().getMap().getRootNode();
            System.out.println("Creating nodes from root "+root.hashCode());
            // set selection override
            NCSOverride = true;
            NCSOverride_Node = root;
            // get controller
            MindMapController controller = current.getController();
            // read attributes
            for(Object s : root.getAttributeKeyList()) {
                // we assume strings are included
                try {
                    String title = (String) s;
                    // check if we have a node named this in our wrapper, then ignore. if not, create a new node
                    NodeWrapper nodeWrapper = NodeWrapper.getByTitle(title);
                    if(nodeWrapper == null){
                        // does not exist
                        System.out.println("Node does not already exist");
                        // create new node @ root // get controller from current

                        //addNew
                        NCSOverride_Title = title;
                        controller.getNewChildAction().actionPerformed(null);
                        //update properties
                        NodeWrapper newNodeWrapper = ANSManager.getLastNodeCreated();
                        // newNodeWrapper.getNodeAdapter().setText(title); // why u no wörk what wronk wit u ?!!??!
                        newNodeWrapper.getNodeAdapter().setXmlNoteText(root.getAttribute(title));
                        newNodeWrapper.setResourceFlag(true);

                    }else{
                        // already exists
                        System.out.println("Node already exists, updating description");
                        // set resource flag to true, to make sure everything is fine
                        nodeWrapper.setResourceFlag(true);
                        nodeWrapper.getNodeAdapter().setXmlNoteText(root.getAttribute(title));
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
        // disable selection override
        NCSOverride = false;
        // simulate keypress to close edit dialog
        try{
            Robot robot = new Robot();
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(100);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void addToRootData(String title){
        // get current selected node
        NodeAdapter current = ANSManager.getLastNodeSelected().getNodeAdapter();
        // get the root node of that tree
        MindMapNode root = current.getMap().getRootNode();
        // add to attribute list if not already contained
        if(!root.getAttributeKeyList().contains(title)){
            root.addAttribute(new Attribute(title));
        }
    }

    public static void removeFromRootData(String title){
        // get current selected node
        NodeAdapter current = ANSManager.getLastNodeSelected().getNodeAdapter();
        // get the root node of that tree
        MindMapNode root = current.getMap().getRootNode();
        // add to attribute list if not already contained
        if(root.getAttributeKeyList().contains(title)){
            root.removeAttribute(root.getAttributePosition(title));
        }
    }

    public static void updateRootData(NodeWrapper nodeWrapper){
        // get current selected node
        NodeAdapter current = nodeWrapper.getNodeAdapter();
        // get the root node of that tree
        MindMapNode root = current.getMap().getRootNode();
        // edit attribute
        String title = current.getText();
        if(root.getAttributeKeyList().contains(title)){
            root.removeAttribute(root.getAttributePosition(title));
            root.addAttribute(new Attribute(title, current.getXmlNoteText()));
        }
    }

}
