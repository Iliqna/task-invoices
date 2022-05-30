package com.tasks.invoices.writer;

import com.tasks.invoices.Invoice;

import java.util.List;

public interface InvoiceWriter {


  void write(final List<Invoice> invoiceList);
}
