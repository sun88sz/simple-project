package com.sun.evolution;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description: <br/>
 * Date: 2017-01-06
 *
 * @author Sun
 */
public class PicEvolution {

	public static String Dir = "evolution/src/resources";

	/**
	 * * 自然淘汰<br/>
	 * 相似度越高, 存活几率越大<br/>
	 * 最多不能超过N代
	 * 
	 * @param toEvo
	 * @param standard
	 * @param maxSize
	 *            种群数量(繁衍后被自然淘汰后保持的数量)
	 * @param maxAge
	 *            最大繁衍代数
	 * @param xSize
	 * @param ySize
	 * @return
	 */
	public List<Pic> eliminate(List<Pic> toEvo, Pic standard, int maxSize, int maxAge, int xSize, int ySize) {

		ArrayList<Pic> iconsElied = new ArrayList<>();

		double similarMin = Double.MAX_VALUE;

		for (int i = 0; i < toEvo.size(); i++) {
			Pic pic = toEvo.get(i);

			// 如果繁衍超过[age]代,则直接被淘汰
			if (pic.age > maxAge) {
				pic.similarAbs = Double.MAX_VALUE;
			} else {
				pic.similarAbs = similar(pic.picArr, standard.picArr, xSize, ySize);
				if (pic.similarAbs < similarMin) {
					similarMin = pic.similarAbs;
				}
			}
			// 进过了一代
			pic.age = pic.age + 1;
		}
		for (Pic pic : toEvo) {
			// 用相对差异的方式放大种群之间差异
			// 并不是simil小就一定能存活下来
			pic.similarRev = (pic.similarAbs - similarMin) * (1 + Math.random());
		}

		Collections.sort(toEvo);

		for (int i = 0; i < maxSize; i++) {
			iconsElied.add(toEvo.get(i));
		}

		return iconsElied;
	}

	/**
	 * 判断最是否颜色是否相近, 先用最简单的实现<br/>
	 * 越小越好
	 * 
	 * @param now
	 * @param standard
	 * @param xSize
	 * @param ySize
	 * @return
	 */
	private double similar(int[][] now, int[][] standard, int xSize, int ySize) {
		double similar = 0;
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				int colorX = now[i][j];
				int colorS = standard[i][j];

				int rDiff = Math.abs(((colorX & 0xff0000) >> 16) - ((colorS & 0xff0000) >> 16));
				int gDiff = Math.abs(((colorX & 0x00ff00) >> 8) - ((colorS & 0x00ff00) >> 8));
				int bDiff = Math.abs((colorX & 0x0000ff) - (colorS & 0x0000ff));

				similar = similar + (rDiff + gDiff + bDiff);
			}
		}
		return similar;
	}

	/**
	 * 生孩子<br/>
	 * 取父本的各一部分值
	 * 
	 * @param dad
	 * @param mon
	 * @param xSize
	 * @param ySize
	 * @return
	 */
	public Pic birth(Pic dad, Pic mon, int xSize, int ySize) {
		int[][] childPicArr = new int[xSize][ySize];
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				if (Math.random() > 0.5) {
					childPicArr[x][y] = dad.picArr[x][y];
				} else {
					childPicArr[x][y] = mon.picArr[x][y];
				}
			}
		}
		Pic child = new Pic();
		child.picArr = childPicArr;
		return child;
	}

	/**
	 * 繁衍后代<br/>
	 * 
	 * @param pics
	 *            已有的种群
	 * @param evoSize
	 *            繁殖的种群数量
	 * @param mutationLevel
	 *            突变的数量级(越大突变的基因就越少)
	 * @param xSize
	 * @param ySize
	 * @return
	 */
	public List<Pic> reproduce(List<Pic> pics, int evoSize, int mutationLevel, int xSize, int ySize) {
		int size = pics.size();
		for (int i = 0; i < evoSize; i++) {
			int x = (int) (Math.random() * size);
			int y = (int) (Math.random() * size);

			Pic dad = pics.get(x);
			Pic mom = pics.get(y);
			if (dad != mom) {
				// 遗传
				Pic child = birth(dad, mom, xSize, ySize);
				// 突变
				child = mutation(child, mutationLevel, xSize, ySize);
				pics.add(child);
			}
		}
		return pics;
	}

	/**
	 * 突变
	 *
	 * @return
	 */
	private Pic mutation(Pic pic, int mutationLevel, int xSize, int ySize) {
		int[][] picArr = pic.picArr;

		int num = xSize * ySize;
		int times = (int) (num / mutationLevel * Math.random());

		// 1/4的细胞会突变
		for (int i = 0; i < times; i++) {
			int x = (int) (Math.random() * xSize);
			int y = (int) (Math.random() * ySize);

			picArr[x][y] = colorRandom();
		}

		return pic;
	}

	/**
	 * 种群诞生<br/>
	 * 随机生成size个图片
	 *
	 * @return
	 */
	public List<Pic> init(int size, int xSize, int ySize) {

		List<Pic> pics = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			Pic pic = new Pic();
			pic.age = 1;
			int[][] ints = new int[xSize][ySize];
			for (int x = 0; x < xSize; x++) {
				for (int y = 0; y < ySize; y++) {
					ints[x][y] = colorRandom();
				}
			}
			pic.picArr = ints;
			pics.add(pic);
		}
		return pics;
	}

	/**
	 * 随机生成rgb颜色
	 *
	 * @return
	 */
	private int colorRandom() {
		// 随机生成rgb颜色
		int r = (int) (Math.random() * 257);
		int g = (int) (Math.random() * 257);
		int b = (int) (Math.random() * 257);
		int rgb = (r << 16) + (g << 8) + b;
		return rgb;
	}

	/**
	 * 创造
	 * 
	 * @throws Exception
	 */
	public static void create(Pic standard, int width, int height) throws Exception {

		PicEvolution ev = new PicEvolution();
		List<Pic> pics = ev.init(200, width, height);

		// 代
		int gen = 1;
		int i = 0;
		// 数量级
		int magnitude = 1;
		int level = ((int) Math.sqrt(Math.sqrt(gen))) * 50;

		while (true) {
			// 每提升一个数量级
			if (gen % Math.pow(10, magnitude) == 0) {
				level = ((int) Math.sqrt(Math.sqrt(gen))) * 50;
				if (level < Math.sqrt(width) + 1) {
					level = (int) Math.sqrt(width) + 1;
				}
				magnitude++;
			}

			// 繁殖
			pics = ev.reproduce(pics, pics.size() * 2, level, width, height);
			// 自然淘汰
			pics = ev.eliminate(pics, standard, 200, 4, width, height);

			System.out.println("第" + gen + "代\t\t" + pics.get(0).similarAbs + "\t" + pics.get(99).similarAbs);

			File dir = new File(Dir, "evo");
			if (!dir.exists()) {
				dir.mkdirs();
			}

			if (gen % Math.pow(2, i) == 0) {
				i++;
				File fileW = new File(Dir, "evo/firefox" + gen + ".jpg");
				int[][] picArr = pics.get(0).picArr;
				System.out.println(picArr[0][0]);
				ImagePixelUtils.printImageByPixel(width, height, pics.get(0).picArr, "jpg", fileW);
			}

			gen++;
		}
	}

	public static void main(String[] args) throws Exception {
		File file = new File(Dir, "/firefox64.jpg");
		int[][] pic = ImagePixelUtils.getImagePixel(file);
		Pic standard = new Pic(pic);

		int width = pic.length;
		int height = pic[0].length;

		PicEvolution.create(standard, width, height);

		// int i = ((int) Math.sqrt(137215)) * 10;
		// System.out.println(i);

	}
}

class Pic implements Comparable<Pic> {
	public int[][] picArr;
	public int age = 1;
	public double similarAbs = 0;
	public double similarRev = 0;

	public Pic(int[][] picArr) {
		this.picArr = picArr;
	}

	public Pic() {
	}

	@Override
	public int compareTo(Pic o) {
		if (this.similarRev < o.similarRev)
			return -1;
		else if (this.similarRev > o.similarRev)
			return 1;
		else
			return 0;
	}
}
