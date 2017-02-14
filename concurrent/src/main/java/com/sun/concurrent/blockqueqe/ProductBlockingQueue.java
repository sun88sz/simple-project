package com.sun.concurrent.blockqueqe;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Description: <br/>
 * Date: 2017-01-03
 *
 * @author Sun
 */
public class ProductBlockingQueue {

	/**
	 * add & remove <br/>
	 * offer & pull <br/>
	 * put & take
	 *
	 */
	public static void main(String[] args) {
		// 容量,是否是公平竞争
		BlockingQueue x = new ArrayBlockingQueue<ProductBlockingQueue>(3, false);
		new Thread(new Producer(x)).start();
		new Thread(new Producer(x)).start();
		new Thread(new Producer(x)).start();
		new Thread(new Consumer(x)).start();
		new Thread(new Consumer(x)).start();
		new Thread(new Consumer(x)).start();
	}
}

class Producer implements Runnable {

	private BlockingQueue<ProductBlockingQueue> p;

	Producer(BlockingQueue p) {
		super();
		this.p = p;
	}

	public void run() {
		while (true) {
			try {
				// 成功返回true,失败返回false
				boolean offer = p.offer(new ProductBlockingQueue());
				System.out.println("Producer " + Thread.currentThread().getName() + " " + (offer ? "成功" : "失败"));
				Thread.currentThread().sleep((int) (Math.random() * 200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Consumer implements Runnable {

	private BlockingQueue<ProductBlockingQueue> p;

	Consumer(BlockingQueue p) {
		super();
		this.p = p;
	}

	public void run() {
		while (true) {
			try {
				// 成功返回取出的对象,失败返回null
				ProductBlockingQueue product = p.poll();
				System.out.println(product);
				Thread.currentThread().sleep((int) (Math.random() * 100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}