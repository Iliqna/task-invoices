package com.tasks.invoices.writer;

import com.tasks.invoices.Invoice;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class InvoiceCSVWriter implements InvoiceWriter {

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private final String[] columnNames =
      new String[] {
        "buyer",
        "image_name",
        "invoice_image",
        "invoice_due_date",
        "invoice_number",
        "invoice_amount",
        "invoice_currency",
        "invoice_status",
        "supplier"
      };

  private final Path path;

  public InvoiceCSVWriter(final Path path) {
    this.path = path;
  }

  public void write(final List<Invoice> invoiceList) {

    final Map<String, List<Invoice>> invoicesByBuyer =
        invoiceList.stream().collect(groupingBy(Invoice::getBuyer));
    invoicesByBuyer.forEach(
        (key, value) -> {
          final Path resultPath = Path.of(path + "/" + key + ".csv");
          final boolean isNewFile = !Files.exists(resultPath);
          try (final com.opencsv.CSVWriter writer =
              new com.opencsv.CSVWriter(new FileWriter(resultPath.toFile()))) {
            if (isNewFile) {
              writer.writeNext(columnNames);
            }
            value.forEach(invoice -> writer.writeNext(toLine(invoice)));
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }

  private String[] toLine(final Invoice invoice) {
    return new String[] {
      invoice.getBuyer(),
      invoice.getImageName(),
      invoice.getImage(),
      DATE_FORMAT.format(invoice.getDueDate()),
      invoice.getNumber(),
      invoice.getAmount().toPlainString(),
      invoice.getCurrency(),
      invoice.getStatus(),
      invoice.getSupplier()
    };
  }
}
