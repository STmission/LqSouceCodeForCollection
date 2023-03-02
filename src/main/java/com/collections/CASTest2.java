package com.collections;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: TestProject
 * @description: CAS第二个测试  并发情况下使用AtomicInteger底层是CAS累加到20
 * @author: liuqi
 * @create: 2023-03-02 16:36
 **/
public class CASTest2 {
    //三个线程操作让一个值进行累加到1000

   // static Integer num = 0;
    static AtomicInteger num = new AtomicInteger(0);
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                        while (num.get()<20){
                            System.out.println("当前线程名称："+Thread.currentThread().getName()+"，此时num值为："+num.incrementAndGet());
                        }
                }
            });
            t.start();
        }

    }


}
