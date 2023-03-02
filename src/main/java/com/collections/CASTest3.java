package com.collections;


import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @program: LqSouceCodeForCollection 并发情况下通过CAS无限累加值
 * @description:
 * @author: liuqi
 * @create: 2023-03-02 17:47
 **/
public class CASTest3 {

    private  int i = 0;
    private static sun.misc.Unsafe UNSAFE;
    private static long I_OFFSET;

    static {
        try{
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe)field.get(null);

            I_OFFSET = UNSAFE.objectFieldOffset(CASTest3.class.getDeclaredField("i"));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        CASTest3 casTest3 = new CASTest3();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    //参数：对象实例，
                    if(UNSAFE.compareAndSwapInt(casTest3, I_OFFSET, casTest3.i, casTest3.i + 1)){
                        System.out.println("当前线程为："+Thread.currentThread().getName()+"值为："+UNSAFE.getIntVolatile(casTest3,I_OFFSET));
                    }
                    try {
                        Thread.sleep(300);//sleep方法不释放锁，wait等待会释放锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(UNSAFE.compareAndSwapInt(casTest3, I_OFFSET, casTest3.i, casTest3.i + 1)){
                        System.out.println("当前线程为："+Thread.currentThread().getName()+"值为："+UNSAFE.getIntVolatile(casTest3,I_OFFSET));
                    }
                    try {
                        Thread.sleep(300);//sleep方法不释放锁，wait等待会释放锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

}
