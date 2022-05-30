package com.tasks.invoices;

import com.tasks.invoices.writer.InvoiceWriter;
import com.tasks.invoices.reader.InvoiceBatchReader;

import java.util.List;

public class ProcessStarter {

  private final InvoiceBatchReader reader;
  private final int batchSize;
  private final List<InvoiceWriter> writers;

  public ProcessStarter(
      final InvoiceBatchReader reader, int batchSize, final List<InvoiceWriter> writers) {
    this.reader = reader;
    this.batchSize = batchSize;
    this.writers = writers;
  }

  public void start() {
    while (true) {
      final List<Invoice> invoices = reader.read(batchSize);
      if (!invoices.isEmpty()) {
        writers.forEach(w -> w.write(invoices));
      } else {
        break;
      }
    }
  }
}
