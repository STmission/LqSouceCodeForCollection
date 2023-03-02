package com.collections;


import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: LqSouceCodeForCollection 并发情况下通过CAS无限累加值
 * @description:
 * @author: liuqi
 * @create: 2023-03-02 17:47
 **/
public class CASTest1 {


    //这里一定要可见性(volatile) 参照AtomicInteger中的value就知道了
    private volatile String value = "A";
    private static sun.misc.Unsafe UNSAFE;
    private static long valueOffset;


   
    
    static {
        try{
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe)field.get(null);


            valueOffset = UNSAFE.objectFieldOffset(CASTest1.class.getDeclaredField("value"));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        CASTest1 casTest1 = new CASTest1();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while("A".equals(UNSAFE.getObjectVolatile(casTest1,valueOffset))){
                    //参数：对象实例，
                    if(UNSAFE.compareAndSwapObject(casTest1, valueOffset, "A", "B")){
                        System.out.println("当前线程为："+Thread.currentThread().getName()+"，"+"原来值为：A"+"，"+"修改值为："+UNSAFE.getObjectVolatile(casTest1,valueOffset));
                    }
                    try {
                        Thread.sleep(100);//sleep方法不释放锁，wait等待会释放锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while("B".equals(UNSAFE.getObjectVolatile(casTest1,valueOffset))){
                    if(UNSAFE.compareAndSwapObject(casTest1, valueOffset, "B", "A")){
                        System.out.println("当前线程为："+Thread.currentThread().getName()+"，"+"原来值为：B"+"，"+"修改值为："+UNSAFE.getObjectVolatile(casTest1,valueOffset));
                    }
                    try {
                        Thread.sleep(100);//sleep方法不释放锁，wait等待会释放锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();



        new Thread(new Runnable() {
            @Override
            public void run() {
                while("A".equals(UNSAFE.getObjectVolatile(casTest1,valueOffset))){
                    //参数：对象实例，
                    if(UNSAFE.compareAndSwapObject(casTest1, valueOffset, "A", "B")){
                        System.out.println("当前线程为："+Thread.currentThread().getName()+"，"+"原来值为：A"+"，"+"修改值为："+UNSAFE.getObjectVolatile(casTest1,valueOffset));
                    }
                    try {
                        Thread.sleep(100);//sleep方法不释放锁，wait等待会释放锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

}
