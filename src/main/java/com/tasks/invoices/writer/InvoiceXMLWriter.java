package com.tasks.invoices.writer;

import com.tasks.invoices.Buyer;
import com.tasks.invoices.Invoice;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class InvoiceXMLWriter implements InvoiceWriter {

  private final Path path;

  public InvoiceXMLWriter(final Path path) {
    this.path = path;
  }

  @Override
  public void write(final List<Invoice> invoiceList) {

    final Map<String, List<Invoice>> invoicesByBuyer =
        invoiceList.stream().collect(groupingBy(Invoice::getBuyer));

    for (final Map.Entry<String, List<Invoice>> entry : invoicesByBuyer.entrySet()) {
      final File file = new File(path.toString() + "/" + entry.getKey() + ".xml");

      final Buyer buyer;
      if (file.exists()) {
        try {
          final JAXBContext jaxbContext = JAXBContext.newInstance(Buyer.class);
          final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
          buyer = (Buyer) unmarshaller.unmarshal(file);
        } catch (final JAXBException e) {
          throw new RuntimeException(e);
        }
      } else {
        buyer = new Buyer(entry.getKey());
      }

      buyer.getInvoices().addAll(entry.getValue());

      try {
        final JAXBContext jaxbContext = JAXBContext.newInstance(Buyer.class);
        final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(buyer, file);
      } catch (final JAXBException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
