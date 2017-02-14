package com.sun.concurrent.sign;

import java.util.concurrent.Semaphore;

/**
 * Description: <br/>
 * Date: 2017-01-03
 *
 * @author Sun
 */
public class ProductSemaphore {

	static Semaphore empty = new Semaphore(10); // 信号量：记录仓库空的位置
	static Semaphore full = new Semaphore(0); // 信号量：记录仓库满的位置
	static Semaphore mutex = new Semaphore(1); // 临界区互斥访问信号量(二进制信号量)，相当于互斥锁。

	public static void main(String[] args) {
		new Thread(new Producer()).start();
		new Thread(new Consumer()).start();
	}
}

class Producer implements Runnable {

	public void run() {
		try {
			while (true) {
				ProductSemaphore.empty.acquire(); // 递减仓库空信号量
				ProductSemaphore.mutex.acquire(); // 进入临界区
				System.out.println("生成一个产品放入仓库");
				ProductSemaphore.mutex.release(); // 离开临界区
				ProductSemaphore.full.release(); // 递增仓库满信号量
				Thread.currentThread().sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Consumer implements Runnable {
	public void run() {
		try {
			while (true) {
				ProductSemaphore.full.acquire(); // 递减仓库满信号量
				ProductSemaphore.mutex.acquire();
				System.out.println("从仓库拿出一个产品消费");
				ProductSemaphore.mutex.release();
				ProductSemaphore.empty.release(); // 递增仓库空信号量
				Thread.currentThread().sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}