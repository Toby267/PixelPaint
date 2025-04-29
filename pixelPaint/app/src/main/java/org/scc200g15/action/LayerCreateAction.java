package org.scc200g15.action;

import org.scc200g15.gui.GUI;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;

public class LayerCreateAction implements Action{

    Layer layer;
    int index;

    public LayerCreateAction(Layer layer, int index) {
        this.layer = layer;
        this.index = index;
    }

    @Override
    public void undo(Image image) {
        image.removeLayer(layer);

        GUI.getInstance().getLayerSelector().redrawMenuUI();
        GUI.getInstance().getCanvas().recalculateAllPixels();
        GUI.getInstance().getCanvas().repaint();
    }

    @Override
    public void redo(Image image) {
        image.addLayer(layer, index);
        
        GUI.getInstance().getLayerSelector().redrawMenuUI();
        GUI.getInstance().getCanvas().recalculateAllPixels();
        GUI.getInstance().getCanvas().repaint();
    }
    
}
