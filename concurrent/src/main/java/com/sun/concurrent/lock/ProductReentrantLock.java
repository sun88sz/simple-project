package com.sun.concurrent.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description: <br/>
 * Date: 2016-12-29
 *
 * @author Sun
 */
public class ProductReentrantLock {

	private int size = 0;
	private final int maxSize = 10;
	private final int minSize = 0;

	Lock mylock = new ReentrantLock();

	Condition full = mylock.newCondition();
	Condition empty = mylock.newCondition();

	public void consume() {
		try {
			System.out.println("Consumer " + Thread.currentThread().getName() + " comsume");
			mylock.lock();
			if (size <= minSize) {
				System.out.println("Consumer " + Thread.currentThread().getName() + " wait");
				empty.await();
			}
			size = size - 1;
			full.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mylock.unlock();
		}
	}

	public void produce() {
		try {
			System.out.println("Producer " + Thread.currentThread().getName() + " produce");
			mylock.lock();
			if (size >= maxSize) {
				System.out.println("Producer " + Thread.currentThread().getName() + " wait");
				full.await();
			}
			size = size + 1;
			empty.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mylock.unlock();
		}
	}

	public static void main(String[] args) {
		ProductReentrantLock x = new ProductReentrantLock();
		new Thread(new Producer(x)).start();
		new Thread(new Producer(x)).start();
		new Thread(new Producer(x)).start();
		new Thread(new Consumer(x)).start();
		new Thread(new Consumer(x)).start();
		new Thread(new Consumer(x)).start();

	}
}

class Producer implements Runnable {

	private ProductReentrantLock p;

	Producer(ProductReentrantLock p) {
		super();
		this.p = p;
	}

	public void run() {
		try {
			while (true) {
				p.produce();
				Thread.currentThread().sleep((int) (Math.random() * 200));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Consumer implements Runnable {

	private ProductReentrantLock p;

	Consumer(ProductReentrantLock p) {
		super();
		this.p = p;
	}

	public void run() {
		try {
			while (true) {
				p.consume();
				Thread.currentThread().sleep((int) (Math.random() * 200));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}