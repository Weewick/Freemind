package newChanges;

import freemind.controller.MapModuleManager;
import freemind.main.FreeMind;
import freemind.modes.MindMapNode;
import freemind.modes.NodeAdapter;
import freemind.modes.attributes.Attribute;
import freemind.modes.mindmapmode.MindMapController;

public class NodeConverter {

    /*
        translates between root attributes and nodes
     */


    public static void updateNodesFromRootData(){
        // get current selected node
        NodeWrapper current = ANSManager.getLastNodeSelected();
        if(current != null){
            // get the root node of that tree
            MindMapNode root = current.getNodeAdapter().getMap().getRootNode();
            System.out.println("Creating nodes from root "+root.hashCode());
            // read attributes
            for(Object s : root.getAttributeKeyList()) {
                // we assume strings are included
                try {
                    String title = (String) s;
                    // check if we have a node named this in our wrapper, then ignore. if not, create a new node
                    NodeWrapper nodeWrapper = NodeWrapper.getByTitle(title);
                    if(nodeWrapper == null){
                        // does not exist
                        System.out.println("Node does not exist, will be created now with title: "+title);
                        // create new node @ root // get controller from current
                        MindMapNode newNode = current.getController().newNode(title, current.getNodeAdapter().getMap());
                        // move to root
                        newNode.setParent(root);
                        // set resource flag to true
                        NodeWrapper.get(current.getController().getNodeFromID(current.getController().getNodeID(newNode))).setResourceFlag(true);
                    }else{
                        // already exists
                        System.out.println("Node already exists with title: "+title);
                        // set resource flag to true, to make sure everything is fine
                        nodeWrapper.setResourceFlag(true);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
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

}