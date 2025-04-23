package org.scc200g15.action;

import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;

public class LayerRenameAction implements Action {

    String oldName, newName;
    Layer layer;

    public LayerRenameAction(Layer layer, String oldName, String newName) {
        this.layer = layer;
        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    public void undo(Image image) {
        layer.setLayerName(oldName);
    }

    @Override
    public void redo(Image image) {
        layer.setLayerName(newName);
    }
    
}
