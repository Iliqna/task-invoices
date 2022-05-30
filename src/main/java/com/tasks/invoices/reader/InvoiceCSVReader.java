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
import java.util.List;

public class InvoiceCSVReader implements InvoiceBatchReader {

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private final CSVReader reader;
  private boolean isOpen;

  public InvoiceCSVReader(final Path path) {
    try {
      reader = new CSVReader(new FileReader(path.toFile()));
      this.isOpen = true;
      reader.readNext();
    } catch (IOException | CsvValidationException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Invoice> read(int limit) {
    if (limit < 1) {
      throw new RuntimeException("Limit must be a positive number");
    }
    final List<Invoice> invoices = new ArrayList<>();
    if (isOpen) {
      try {
        while (isOpen && limit > 0) {
          final String[] line = reader.readNext();
          if (line != null) {
            invoices.add(parseLine(line));
            limit--;
          } else {
            isOpen = false;
            reader.close();
          }
        }
      } catch (ParseException | IOException e) {
        throw new RuntimeException(e);
      } catch (CsvValidationException e) {
        System.out.println("Exception occurred while processing line: " + e.getLineNumber());
        throw new RuntimeException(e);
      }
    }
    return invoices;
  }

  // @Override
  public List<Invoice> read2(int limit) {
    if (limit < 1) {
      throw new RuntimeException("Limit must be a positive number");
    }
    final List<Invoice> invoices = new ArrayList<>();
    String[] line;
    try {
      while ((line = reader.readNext()) != null && limit > 0) {
        invoices.add(parseLine(line));
        limit--;
      }
      reader.close();
    } catch (ParseException | IOException e) {
      throw new RuntimeException(e);
    } catch (CsvValidationException e) {
      System.out.println("Exception occurred while processing line: " + e.getLineNumber());
      throw new RuntimeException(e);
    }
    return invoices;
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
