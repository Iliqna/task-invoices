package com.tasks.invoices.writer;

import com.tasks.invoices.Invoice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

public class InvoiceImageWriter implements InvoiceWriter {

  private final Path path;

  public InvoiceImageWriter(final Path path) {
    this.path = path;
  }

  @Override
  public void write(final List<Invoice> invoices) {

    for (final Invoice invoice : invoices) {
      final String image = invoice.getImage();
      final String imageName = invoice.getImageName();
      if (image != null && !image.isEmpty() && imageName != null && !imageName.isEmpty()) {
        writeBase64ToFile(
            invoice.getImage(),
            Paths.get(path + "/" + invoice.getBuyer() + "_" + invoice.getImageName()).toFile());
      }
    }
  }

  private void writeBase64ToFile(final String base64content, final File file) {
    byte[] data = Base64.getDecoder().decode(base64content);
    try (final OutputStream stream = new FileOutputStream(file)) {
      stream.write(data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
