package jpabook.jpashop;

import jpabook.jpashop.domain.Address;

public class ValueMain {
    public static void main(String[] args) {
        Address a = new Address();
        Address b = a;
        //a.setCity("sibal");
        System.out.println("b.getCity() = " + b.getCity());
    }
}
