package org.scc200g15.action;

import org.scc200g15.gui.GUI;
import org.scc200g15.image.Image;

public class LayerMoveAction implements Action{
    int oldIndex, newIndex;

    public LayerMoveAction(int oldIndex, int newIndex) {
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
    }

    @Override
    public void undo(Image image) {
            GUI.getInstance().getActiveImage().moveLayer(newIndex, oldIndex );


        

        GUI.getInstance().getLayerSelector().redrawMenuUI();
        GUI.getInstance().getCanvas().recalculateAllPixels();
        GUI.getInstance().getCanvas().repaint();
    }

    @Override
    public void redo(Image image) {
        GUI.getInstance().getActiveImage().moveLayer(oldIndex, newIndex);

        GUI.getInstance().getLayerSelector().redrawMenuUI();
        GUI.getInstance().getCanvas().recalculateAllPixels();
        GUI.getInstance().getCanvas().repaint();
    }
    
}
