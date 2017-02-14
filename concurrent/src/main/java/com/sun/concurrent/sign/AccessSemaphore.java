package com.sun.concurrent.sign;

import java.util.concurrent.Semaphore;

/**
 * Description: <br/>
 * Date: 2017-01-03
 *
 * @author Sun
 */
public class AccessSemaphore {

	// 5个资源
	private static Semaphore sm = new Semaphore(5);

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			new Thread(new Access(sm)).start();
		}
	}
}

class Access implements Runnable {

	private Semaphore sm;

	public Access(Semaphore sm) {
		this.sm = sm;
	}

	public void run() {
		try {
			System.out.println(Thread.currentThread().getName() + " 访问");
			sm.acquire();
			System.out.println(Thread.currentThread().getName() + " 获得许可");

			Thread.sleep((long) (Math.random() * 3000));
			sm.release();
			System.out.println(Thread.currentThread().getName() + " 释放访问许可");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
