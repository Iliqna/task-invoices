import com.tasks.invoices.Invoice;
import com.tasks.invoices.reader.InvoiceBatchReader;
import com.tasks.invoices.reader.InvoiceCSVReader;
import com.tasks.invoices.writer.InvoiceCSVWriter;
import com.tasks.invoices.writer.InvoiceWriter;
import org.junit.Assert;
import org.junit.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

public class InvoicesTest {

  @Test
  public void csvTest() {

    final Path srcPath = Paths.get("src/test/resources/invoices.csv");
    final InvoiceBatchReader sourceReader = new InvoiceCSVReader(srcPath);

    final Path destPath = Paths.get("src/test");
    final InvoiceWriter writer = new InvoiceCSVWriter(destPath);

    // read src file in two batches and assert
    final List<Invoice> invoiceBatch1 = sourceReader.read(3);
    final List<Invoice> invoicesBatch2 = sourceReader.read(10);
    final List<Invoice> invoicesBatch3 = sourceReader.read(10);
    Assert.assertTrue(invoicesBatch3.isEmpty());

    final List<Invoice> invoiceList = new ArrayList<>(invoiceBatch1);
    invoiceList.addAll(invoicesBatch2);
    Assert.assertEquals(10, invoiceList.size());
    // TODO: assert invoice properties

    // write
    writer.write(invoiceList);

    // assert write result
    invoiceList.stream()
        .collect(groupingBy(Invoice::getBuyer))
        .forEach(
            (buyer, buyerInvoices) -> {
              final Path resultPath = Path.of(destPath + "/" + buyer + ".csv");
              Assert.assertTrue(Files.exists(resultPath));
              final InvoiceBatchReader resultReader = new InvoiceCSVReader(resultPath);
              final List<Invoice> invoiceResult = resultReader.read(10);
              Assert.assertEquals(buyerInvoices.size(), invoiceResult.size());
              // TODO: assert invoice result properties
              // delete the result file
              Assert.assertTrue(resultPath.toFile().delete());
            });
  }

  // TODO: similar integration tests for xml and image writers
  // TODO: unit tests
}
