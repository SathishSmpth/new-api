package com.kamatchibotique.application.model.tax;

import com.kamatchibotique.application.model.order.OrderTotalItem;
import com.kamatchibotique.application.model.tax.taxrate.TaxRate;

public class TaxItem extends OrderTotalItem {
    private static final long serialVersionUID = 1L;
    private String label;
    private TaxRate taxRate = null;

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setTaxRate(TaxRate taxRate) {
        this.taxRate = taxRate;
    }

    public TaxRate getTaxRate() {
        return taxRate;
    }
}
