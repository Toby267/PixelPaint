package org.scc200g15.action;

import org.scc200g15.image.Image;

/**
 * Action - Represents an action that has happened such as layers moving or pixels being changed
 */
public interface Action {
    public void undo(Image image);
    public void redo(Image image);
}
