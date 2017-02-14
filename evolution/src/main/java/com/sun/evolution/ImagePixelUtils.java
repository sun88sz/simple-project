package com.sun.evolution;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePixelUtils {
	/**
	 * 读取一张图片的RGB值
	 * 
	 */
	public static int[][] getImagePixel(File file) {

		BufferedImage bi = null;
		try {
			bi = ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int width = bi.getWidth();
		int height = bi.getHeight();
		int minx = bi.getMinX();
		int miny = bi.getMinY();

		int[][] picArray = new int[width][height];

		for (int i = minx; i < width; i++) {
			for (int j = miny; j < height; j++) {
				// fffefefe
				int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
				picArray[i][j] = pixel & 0xffffff;
			}
		}
		return picArray;
	}

	/**
	 * 根据像素打印图片
	 * 
	 * @param width
	 * @param height
	 * @param rpgArr
	 * @param suffix
	 *            .jpg
	 * @param file
	 */
	public static void printImageByPixel(int width, int height, int[][] rpgArr, String suffix, File file) {
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				bi.setRGB(i, j, rpgArr[i][j]);
			}
		}
		try {
			ImageIO.write(bi, suffix, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回屏幕色彩值
	 *
	 * @param x
	 * @param y
	 * @return
	 * @throws AWTException
	 */
	public static int getScreenPixel(int x, int y) throws AWTException { // 函数返回值为颜色的RGB值。
		Robot rb = null; // java.awt.image包中的类，可以用来抓取屏幕，即截屏。
		rb = new Robot();
		Toolkit tk = Toolkit.getDefaultToolkit(); // 获取缺省工具包
		Dimension di = tk.getScreenSize(); // 屏幕尺寸规格
		System.out.println(di.width);
		System.out.println(di.height);
		Rectangle rec = new Rectangle(0, 0, di.width, di.height);
		BufferedImage bi = rb.createScreenCapture(rec);
		int pixelColor = bi.getRGB(x, y);

		return 16777216 + pixelColor; // pixelColor的值为负，经过实践得出：加上颜色最大值就是实际颜色值。
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ImagePixelUtils rc = new ImagePixelUtils();
		File file = new File("evolution/src/resources/firefox64.jpg");

		System.out.println(file.getAbsoluteFile() + " - " + file.length());

		int[][] imagePixel = rc.getImagePixel(file);

		File fileW = new File("evolution/src/resources/firefoxTest.jpg");
		printImageByPixel(64, 64, imagePixel, "jpg", fileW);

	}

}