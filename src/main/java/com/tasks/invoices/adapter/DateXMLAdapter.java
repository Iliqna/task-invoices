package com.tasks.invoices.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateXMLAdapter extends XmlAdapter<String, Date> {

  private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  @Override
  public Date unmarshal(String date) throws Exception {
    if (date != null && !date.isEmpty()) {
      return FORMAT.parse(date);
    }
    return null;
  }

  @Override
  public String marshal(Date date) {
    if (date != null) {
      return FORMAT.format(date);
    }
    return null;
  }
}
