package com.tasks.invoices.reader;

import com.tasks.invoices.Invoice;

import java.util.List;

public interface InvoiceBatchReader {

  List<Invoice> read(int limit);
}
