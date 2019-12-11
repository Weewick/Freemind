package newChanges.newNodes;

import freemind.modes.MindMapNode;
import newChanges.nodeData.ANSManager;
import newChanges.nodeWrapper.NodeWrapper;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NewNodeCreator implements Runnable {

    /*
        creates new nodes from requests
     */

    private static BlockingQueue<NewNodeRequest> queue;
    public static MindMapNode NNC_SelectionOverride;
    public static String NNC_NewNodeTitleOverride = "";
    public static boolean NNC_ActiveOverride = false;

    public NewNodeCreator(){
        queue = new LinkedBlockingQueue<>();
    }

    public static void add(NewNodeRequest newNodeRequest){
        queue.add(newNodeRequest);
    }

    @Override
    public void run() {
        System.out.println("Started NewNodeCreator");
        while(true){
            try{
                // take
                NewNodeRequest nnr = queue.take();
                System.out.println("Creating node from request "+nnr.hashCode());
                // set override
                NNC_ActiveOverride = true;
                // set title
                NNC_NewNodeTitleOverride = nnr.getTitle();
                // set parent
                NNC_SelectionOverride = nnr.getParent();
                // create node
                nnr.getController().getNewChildAction().actionPerformed(null);
                // get last node
                NodeWrapper newNodeWrapper = ANSManager.getLastNodeCreated();
                newNodeWrapper.getNodeAdapter().setXmlNoteText(nnr.getDescription());
                newNodeWrapper.setResourceFlag(nnr.getResourceFlag());
                // simulate keypress to close edit dialog
                Robot robot = new Robot();
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.delay(25);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }catch (Exception e){
                e.printStackTrace();
            }
            // remove override
            NNC_ActiveOverride = false;
        }
    }
}
