//Tyler Ballance

package Lab4;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * View: Contains everything about graphics and images
 * Know size of world, which images to load etc
 *
 * has methods to
 * provide boundaries
 * use proper images for direction
 * load images for all direction (an image should only be loaded once!!! why?)
 **/

public class View{

	private int picNum;
	private int imageNum;
	private BufferedImage[] pics;
	private final int frameCount;
	private final int frameWidth;
	private final int frameHeight;
	private final int imgWidth;
	private final int imgHeight;
	private JFrame frame;
	private ViewHelper helper;
	//Initializes properties and builds frame object to view animation
	public View(){
		picNum = 0;
		imageNum = 0;
		frameCount = 10;
		frameWidth = 500;
		frameHeight = 300;
		imgWidth = 165;
		imgHeight = 165;
		buildPics();
		helper = new ViewHelper();
		frame = new JFrame();
		frame.getContentPane().add(helper);
		frame.setBackground(Color.gray);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameWidth, frameHeight);
		frame.setVisible(true);
	}
	//Returns width of frame object
	public int getWidth() { return frameWidth; }
	//Returns height of frame object
	public int getHeight() { return frameHeight; }
	//Returns width of animation images
	public int getImageWidth() { return imgWidth; }
	//Returns height of animation images
	public int getImageHeight() { return imgHeight; }
	//Updates view based on new x and y coordinates and direction from model
	public void update(int xloc, int yloc, Direction direction) {
		//Passes values from model to helper class
		helper.setX(xloc);
		helper.setY(yloc);
		helper.setDirect(direction);
		//Updates graphical representation and stalls for 100 ms
		frame.repaint();
		try { Thread.sleep(100); }
		catch (InterruptedException e) { e.printStackTrace(); }
	}
	//Cuts image source files into animation frames
	private void buildPics(){
		//Retrieves images from folder and creates image objects
		BufferedImage[] images = new BufferedImage[8];
		images[0] = createImage("orc_forward_southeast.png");
		images[1] = createImage("orc_forward_northeast.png");
		images[2] = createImage("orc_forward_southwest.png");
		images[3] = createImage("orc_forward_northwest.png");
		images[4] = createImage("orc_forward_east.png");
		images[5] = createImage("orc_forward_west.png");
		images[6] = createImage("orc_forward_south.png");
		images[7] = createImage("orc_forward_north.png");
		//Cuts image files into animation frames for pics, where the ith image becomes 10 frames from (i-1)*10 to (i-1)*10+9 inclusive
		pics = new BufferedImage[frameCount*8];
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < frameCount; j++){
				pics[j + frameCount*i] = images[i].getSubimage(imgWidth*j, 0, imgWidth, imgHeight);
			}
		}
	}
	//Reads image from file system and returns it as an image object
	private BufferedImage createImage(String file){
		BufferedImage bufferedImage;
		try {
			String path = "src/images/orc/";
			path += file;
			bufferedImage = ImageIO.read(new File(path));
			return bufferedImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private class ViewHelper extends JPanel{
		private int xloc;
		private int yloc;
		private Direction direction;
		//Initializes properties with default values to avoid error
		private ViewHelper(){
			xloc = 0;
			yloc = 0;
			direction = Direction.SOUTH;
		}
		//Allows x coordinate from model to be passed to this object
		private void setX(int xloc) { this.xloc = xloc; }
		//Allows y coordinate from model to be passed to this object
		private void setY(int yloc) { this.yloc = yloc; }
		//Allows direction from model to be passed to this object
		private void setDirect(Direction direction) { this.direction = direction; }
		//Updates graphical representation based on information from model
		public void paint(Graphics g) {
			//Sets which animation frame to display by incrementing from past frame or changing to new image set if needed
			picNum = ((picNum + 1) % frameCount) + (frameCount * imageNum);
			//Checks if imageNum should be updated so that the animation frames can be chosen from a new image set, i.e. changes graphical direction of orc
			updateImage();
			//Draws image to update graphical representation
			g.drawImage(pics[picNum], xloc, yloc, Color.gray, this);
		}
		//Updates which image set to pull from based on direction from model
		private void updateImage() {
			switch(direction) {
			case SOUTHEAST:
				imageNum = 0;
				break;
			case NORTHEAST:
				imageNum = 1;
				break;
			case SOUTHWEST:
				imageNum = 2;
				break;
			case NORTHWEST:
				imageNum = 3;
				break;
			case EAST:
				imageNum = 4;
				break;
			case WEST:
				imageNum = 5;
				break;
			case SOUTH:
				imageNum = 6;
				break;
			case NORTH:
				imageNum = 7;
				break;
			}
		}
	}
}