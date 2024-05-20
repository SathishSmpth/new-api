package com.kamatchibotique.application.model.customer;

import com.kamatchibotique.application.model.common.EntityList;

import java.util.ArrayList;
import java.util.List;

public class CustomerList extends EntityList {
    private static final long serialVersionUID = -3108842276158069739L;

    private List<Customer> customers = new ArrayList<>();

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}
