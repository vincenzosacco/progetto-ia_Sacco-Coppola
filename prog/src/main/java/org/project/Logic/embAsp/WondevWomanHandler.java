package org.project.Logic.embAsp;

import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import org.project.Logic.embAsp.graph.atoms.value;

public class WondevWomanHandler extends MyHandler {

    public WondevWomanHandler() {
        super();
        try {
            mapToEmb(cell.class);
            mapToEmb(buildIn.class);
            mapToEmb(moveIn.class);
            mapToEmb(value.class);
        } catch (ObjectNotValidException | IllegalAnnotationException e) {
            throw new RuntimeException(e);
        }
    }

    public WondevWomanHandler(WondevWomanHandler handler) {
        super(handler);
    }

    @Override
    public void startSync() {
        super.startSync();

    }
}
