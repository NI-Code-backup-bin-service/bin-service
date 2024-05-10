/*
 * Copyright (c) 2023 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.mada;

import ae.network.gateway.bin.domain.BinInfo;
import ae.network.gateway.bin.domain.FileRowParser;
import ae.network.gateway.bin.domain.LogEventBuilder;
import ae.network.logging.loggers.ApplicationLogger;

import javax.inject.Named;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Named
public abstract class FileLoader {

    private static final ApplicationLogger LOGGER = ApplicationLogger.getLogger(FileLoader.class);

    protected abstract FileRowParser getRowParser();

    public Map<String, BinInfo> load(String... files) {
        Map<String, BinInfo> binInfo = new TreeMap<>();

        for (String file : files) {
            URL resource = getClass().getClassLoader().getResource(file);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.openStream()))) {
                Map<String, BinInfo> fileBinInfo = br.lines()
                    .map(line -> getRowParser().parse(line))
                    .collect(Collectors.toMap(BinInfo::getBin, Function.identity(), (prev, current) -> current));
                binInfo.putAll(fileBinInfo);
                LOGGER.info(LogEventBuilder.build(String.format("Loaded %s rows from %s", fileBinInfo.size(), file)));
            } catch (IOException ex) {
                LOGGER.error(LogEventBuilder.build("Exception thrown loading mada files", ex));
                throw new RuntimeException(ex);
            }
        }

        LOGGER.info(LogEventBuilder.build(String.format("%s total rows loaded from mada file", binInfo.size())));
        return binInfo;
    }
}
