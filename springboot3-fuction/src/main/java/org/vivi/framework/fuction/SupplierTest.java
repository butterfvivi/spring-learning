package org.vivi.framework.fuction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class SupplierTest {

    public static void main(String[] args) {
        Supplier<Integer> supplier = () -> new Random().nextInt(0);

        System.out.println(testSupplier(2,supplier));
        System.out.println(testSupplier(5,supplier));
    }

    public static List<Integer> testSupplier(int num, Supplier<Integer> supplier) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < num; i++){
            list.add(supplier.get());
        }
        return list;
    }
}
