package com.collections;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicStampedReference;

//ABA问题解决，使用AtomicStampedReference(初始值reference，初始印章stamped)
public class AabProResolve {

    private static final Random RANDOM = new Random();
    private static final String B = "B";
    private static final String A = "A";

    //初始值，初始印章
    private static final AtomicStampedReference<String> ATOMIC_STAMPED_REFERENCE = new AtomicStampedReference<>(A,0);

    public static void main(String[] args) throws InterruptedException {

        final CountDownLatch startLatch = new CountDownLatch(1);
        Thread[] threads = new Thread[20];

        for (int i=0; i < 5; i++){
            threads[i] = new Thread(){

                @Override
                public void run() {
                    String oldValue = ATOMIC_STAMPED_REFERENCE.getReference();
                    int stamp = ATOMIC_STAMPED_REFERENCE.getStamp();

                    try {
                        startLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(RANDOM.nextInt() & 500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (ATOMIC_STAMPED_REFERENCE.compareAndSet(oldValue, B, stamp, stamp+1)){
                        System.out.println(Thread.currentThread().getName()+ " 已经对原始值: "+oldValue+" 进行了修改,此时值为: "+ ATOMIC_STAMPED_REFERENCE.getReference()+"此时stamp为："+ATOMIC_STAMPED_REFERENCE.getStamp());
                    }

                }
            };
            threads[i].start();
        }
        Thread.sleep(200);
        startLatch.countDown();

        new Thread(){
            @Override
            public void run() {

                try {
                    Thread.sleep(RANDOM.nextInt() & 200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int stamp = ATOMIC_STAMPED_REFERENCE.getStamp();
                String oldVal = ATOMIC_STAMPED_REFERENCE.getReference();
                while (!ATOMIC_STAMPED_REFERENCE.compareAndSet(
                        B,
                        A,stamp, stamp+1)){
                    stamp = ATOMIC_STAMPED_REFERENCE.getStamp();
                }
                System.out.println(Thread.currentThread().getName() +" 已经将值 "+oldVal+" 修改成原始值: A"+"，此时stamp为："+ATOMIC_STAMPED_REFERENCE.getStamp());


            }
        }.start();

    }

}