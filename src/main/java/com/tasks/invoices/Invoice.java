package com.tasks.invoices;

import com.tasks.invoices.adapter.DateXMLAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Invoice")
public class Invoice {

  @XmlTransient private final String buyer;

  @XmlElement(name = "imageName")
  private final String imageName;

  @XmlTransient private final String image;

  @XmlElement(name = "dueDate")
  @XmlJavaTypeAdapter(DateXMLAdapter.class)
  private final Date dueDate;

  @XmlElement(name = "number")
  private final String number;

  @XmlElement(name = "amount")
  private final BigDecimal amount;

  @XmlElement(name = "currency")
  private final String currency;

  @XmlElement(name = "status")
  private final String status;

  @XmlElement(name = "supplier")
  private final String supplier;

  public Invoice() {
    this.buyer = null;
    this.imageName = null;
    this.image = null;
    this.dueDate = null;
    this.number = null;
    this.amount = null;
    this.currency = null;
    this.status = null;
    this.supplier = null;
  }

  public Invoice(
      final String bayer,
      final String supplier,
      final String number,
      final BigDecimal amount,
      final String currency,
      final Date dueDate,
      final String status,
      final String imageName,
      final String image) {
    this.buyer = bayer;
    this.supplier = supplier;
    this.number = number;
    this.amount = amount;
    this.currency = currency;
    this.dueDate = dueDate;
    this.status = status;
    this.imageName = imageName;
    this.image = image;
  }

  public String getBuyer() {
    return buyer;
  }

  public String getImageName() {
    return imageName;
  }

  public String getImage() {
    return image;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public String getNumber() {
    return number;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getCurrency() {
    return currency;
  }

  public String getStatus() {
    return status;
  }

  public String getSupplier() {
    return supplier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Invoice invoice = (Invoice) o;
    return Objects.equals(number, invoice.number) && Objects.equals(supplier, invoice.supplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, supplier);
  }
}
