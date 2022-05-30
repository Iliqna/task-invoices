package com.tasks.invoices;

import com.tasks.invoices.reader.InvoiceBatchReader;
import com.tasks.invoices.reader.InvoiceCSVReader;
import com.tasks.invoices.writer.InvoiceCSVWriter;
import com.tasks.invoices.writer.InvoiceImageWriter;
import com.tasks.invoices.writer.InvoiceWriter;
import com.tasks.invoices.writer.InvoiceXMLWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {

  static final int BATCH_SIZE = 100;

  public static void main(String[] args) {

    if (args.length != 4) {
      printExpectedOutput();
      throw new RuntimeException("Unexpected argument length");
    }

    final String srcPathArg = args[0];
    final Path srcPath = Paths.get(srcPathArg);
    validatePath(srcPath);

    final String destPathArg = args[1];
    final Path destPath = Paths.get(destPathArg);
    validatePath(destPath);

    final String destImagePathArg = args[2];
    final Path destImagePath = Paths.get(destImagePathArg);
    validatePath(destImagePath);

    final String outputFormat = args[3];
    final InvoiceWriter writer;
    switch (outputFormat) {
      case "CSV":
        writer = new InvoiceCSVWriter(destPath);
        break;
      case "XML":
        writer = new InvoiceXMLWriter(destPath);
        break;
      default:
        printExpectedOutput();
        throw new RuntimeException("Unsupported format: " + outputFormat);
    }

    final InvoiceBatchReader reader = new InvoiceCSVReader(srcPath);
    final List<InvoiceWriter> writers =
        Arrays.asList(writer, new InvoiceImageWriter(destImagePath));
    final ProcessStarter starter = new ProcessStarter(reader, BATCH_SIZE, writers);
    starter.start();
  }

  private static void validatePath(final Path path) {
    if (!Files.exists(path)) {
      throw new RuntimeException("Invalid path: " + path);
    }
  }

  private static void printExpectedOutput() {

    System.out.println("Expected format: src dst imageDst format");
    System.out.println("- src - path to the src file");
    System.out.println("- dst - path to the destination directory");
    System.out.println("- imageDst - path to the destination image directory");
    System.out.println("- format - format of the result files: {CSV|XML}");
    System.out.println(
        "EXAMPLE: /home/iliyana/invoices.scv /home/iliyana/output /home/iliyana/output/images CSV");
  }
}
