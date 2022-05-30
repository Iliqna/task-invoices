package com.tasks.invoices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Buyer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Buyer {

  @XmlElement(name = "name")
  private final String name;

  @XmlElementWrapper(name = "invoices")
  @XmlElement(name = "invoice")
  private final List<Invoice> invoices = new ArrayList<>();

  public Buyer() {
    this.name = null;
  }

  public Buyer(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<Invoice> getInvoices() {
    return invoices;
  }
}
