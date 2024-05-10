/*
 * Copyright (c) 2023 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.mada;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class MadaFileLoader extends FileLoader {

    private final MadaFileRowParser rowParser;

    @Inject
    public MadaFileLoader(MadaFileRowParser rowParser) {
        this.rowParser = rowParser;
    }

    @Override
    protected MadaFileRowParser getRowParser() {
        return rowParser;
    }
}
