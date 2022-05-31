package com.tasks.invoices.reader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.tasks.invoices.Invoice;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvoiceCSVReader implements InvoiceBatchReader {

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private final CSVReader reader;
  private boolean isClosed;

  public InvoiceCSVReader(final Path path) {
    try {
      reader = new CSVReader(new FileReader(path.toFile()));
      reader.readNext();
    } catch (IOException | CsvValidationException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Invoice> read(int limit) {

    if (isClosed) {
      return Collections.emptyList();
    }
    if (limit < 1) {
      throw new RuntimeException("Limit must be a positive number");
    }

    try {
      final List<Invoice> invoices = new ArrayList<>();
      while (!isClosed && limit > 0) {
        final String[] line = reader.readNext();
        if (line != null) {
          invoices.add(parseLine(line));
          limit--;
        } else {
          isClosed = true;
          reader.close();
        }
      }
      return invoices;
    } catch (ParseException | IOException e) {
      throw new RuntimeException(e);
    } catch (CsvValidationException e) {
      throw new RuntimeException(
          "Exception occurred while processing line: " + e.getLineNumber(), e);
    }
  }

  private Invoice parseLine(final String[] fields) throws ParseException {
    return new Invoice(
        fields[0], // buyer
        fields[8], // supplier
        fields[4], // number
        new BigDecimal(fields[5]), // amount
        fields[6], // currency
        DATE_FORMAT.parse(fields[3]), // due date
        fields[7], // status
        fields[1], // image name
        fields[2]); // image content
  }
}
