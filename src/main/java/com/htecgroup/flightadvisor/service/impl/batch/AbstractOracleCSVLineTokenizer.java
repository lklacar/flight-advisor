package com.htecgroup.flightadvisor.service.impl.batch;

import com.htecgroup.flightadvisor.exception.CSVParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;

import javax.annotation.Nonnull;
import java.io.StringReader;
import java.util.stream.IntStream;


public abstract class AbstractOracleCSVLineTokenizer implements LineTokenizer {

    public abstract String[] getFieldNames();

    @Override
    @Nonnull
    public FieldSet tokenize(String s) {
        String[] fieldsNames = getFieldNames();
        CSVRecord record = parseLine(s);
        return new DefaultFieldSetFactory().create(IntStream.range(0, fieldsNames.length)
                .boxed()
                .map(record::get)
                .toArray(String[]::new), fieldsNames);

    }

    private CSVRecord parseLine(String s) {
        try {
            StringReader stringReader = new StringReader(s);
            CSVParser csvParser = new CSVParser(stringReader, CSVFormat.ORACLE);
            return csvParser.getRecords()
                    .get(0);
        } catch (Exception ex) {
            throw new CSVParseException(ex);
        }
    }
}
