/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.binbase;

import ae.network.gateway.bin.domain.AccountRangeInfo;
import ae.network.gateway.bin.domain.BinInfo;
import ae.network.gateway.bin.domain.LogEventBuilder;
import ae.network.logging.loggers.ApplicationLogger;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import javax.inject.Inject;
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
public class BinBaseFileLoader {

    private static final ApplicationLogger LOGGER = ApplicationLogger.getLogger(BinBaseFileLoader.class);

    BinBaseFileRowParser rowParser;

    @Inject
    public BinBaseFileLoader(BinBaseFileRowParser rowParser) {
        this.rowParser = rowParser;
    }

    public Map<String, BinInfo> load(String... files) {
        Map<String, BinInfo> binInfo = new TreeMap<>();

        for (String file : files) {
            URL resource = getClass().getClassLoader().getResource(file);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.openStream()))) {
                Map<String, BinInfo> fileBinInfo = br.lines()
                    .map(line -> rowParser.parse(line))
                    .collect(Collectors.toMap(BinInfo::getBin, Function.identity(), (prev, current) -> current));
                binInfo.putAll(fileBinInfo);
                LOGGER.info(LogEventBuilder.build(String.format("Loaded %s rows from %s", fileBinInfo.size(), file)));
            } catch (IOException ex) {
                LOGGER.error(LogEventBuilder.build("Exception thrown loading bin base files", ex));
                throw new RuntimeException(ex);
            }
        }

        LOGGER.info(LogEventBuilder.build(String.format("%s total rows loaded", binInfo.size())));
        return binInfo;
    }

    public RangeMap<String, AccountRangeInfo> loadVisaAccountRangeDefinition(String file) {
        RangeMap<String, AccountRangeInfo> visaAccountRangeDefinition = TreeRangeMap.create();

        URL resource = getClass().getClassLoader().getResource(file);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.openStream()))) {
            br.lines().forEach(line -> {
                final String rangeLowerBound = line.substring(12, 21);
                final String rangeUpperBound = line.substring(0, 9);

                final String issuingCountry = line.substring(43, 45);
                final boolean isTokenAccountRange = "Y".equals(line.substring(33, 34));

                final AccountRangeInfo accountRangeInfo = new AccountRangeInfo(isTokenAccountRange, issuingCountry);
                visaAccountRangeDefinition.put(Range.closed(rangeLowerBound, rangeUpperBound), accountRangeInfo);
            });
            return visaAccountRangeDefinition;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
