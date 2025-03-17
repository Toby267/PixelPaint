package org.scc200g15.action;

import org.scc200g15.image.Image;

public interface Action {
    public void undo(Image image);
    public void redo(Image image);
}
