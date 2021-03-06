
package _19;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import _19.Tank.Direction;

import java.util.ArrayList;
/*
 *  void setResizable(boolean resizable) 
          设置此窗体是否可由用户调整大小。 
    
    public abstract Color getColor()获取此图形上下文的当前颜色。
    
    public abstract void setColor(Color c)将此图形上下文的当前颜色设置为指定颜色。使用此图形上下文的所有后续图形操作均使用这个指定的颜色。 
    
    public void repaint()重绘此组件。 
	如果此组件是轻量级组件，则此方法会尽快调用此组件的 paint 方法。否则此方法会尽快调用此组件的 update 方法。
	
	abstract  boolean drawImage(Image img, int x, int y, ImageObserver observer) 
          绘制指定图像中当前可用的图像。 

 */
public class TankClient extends Frame{
	private Image offScreamImage = null;
	public static int WIDTH,HEIGHT;
	public int times = 0;//用来表示发了几发炮弹
	public List<Bullets> myBullets = new ArrayList<Bullets>();//代表还有几发炮弹
	public List<Explode> explodes = new ArrayList<Explode>();
	public List<Tank> enemyTank = new ArrayList<Tank>();
	private Tank myTank = new Tank(50,500,true,Direction.STOP,this);//把TankClient对象传进去，使Tank可以操作TankClient里面的部分数据
	
	int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	Wall wall1 = new Wall(100,100,20,200,this);
	Wall wall2 = new Wall(300,450,60,60,this);
	
	public void paint(Graphics g) {
		myTank.drawTank(g);
		wall1.draw(g);
		wall2.draw(g);
		
		for(int i=0 ;i<enemyTank.size();i++){//画出敌人坦克
			Tank t = enemyTank.get(i);
			t.hitWall(wall1);
			t.hitWall(wall2);
			t.drawTank(g);
			t.hitTank(enemyTank);
		}
		for(int i = 0;i<explodes.size();i++){//画出爆炸
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		g.setFont(new Font("", Font.BOLD, 15));//设置字体
		if(times>=20){
			g.drawString("还有多少发炮弹:"+0, 10, 50);
			g.setColor(Color.red);
//			g.setFont(new Font("", Font.BOLD, 20));
			g.drawString("没炮弹了(充钱)", 20, 75);
		}
		else{
			g.drawString("还有多少发炮弹:"+(20-times), 10, 50);
			g.drawString("敌方坦克："+enemyTank.size(), 10, 70);
		}
		
		if(myBullets.size()!=0)
		for(int i=0;i<myBullets.size();i++){
			Bullets b = myBullets.get(i);//创建这一发炮弹	这一发子弹里面有坐标有方向
			b.hitTanks(enemyTank);//判断是否击中坦克（击中了就设置live标志为false）
			b.hitTank(myTank);
			b.hitWall(wall1);//设置子弹撞墙时的动作
			b.hitWall(wall2);//设置子弹撞墙时的动作
			b.drawBullets(g);//当击中坦克，子弹自己就不再画自己了（通过坦克自己内部的live标志）
		}
	}

	//添加双缓冲消除闪烁现象
	public void update(Graphics g) {
		if(offScreamImage == null){
			offScreamImage = this.createImage(WIDTH,HEIGHT);//把所有东西都画在这张图片上
		}
			
		Graphics gOffScream = offScreamImage.getGraphics();//获取这张图片的画笔
		//下面是重画屏幕，使他恢复原来的颜色
		Color c = g.getColor();
		gOffScream.setColor(Color.gray);
		gOffScream.fillRect(0, 0, WIDTH, HEIGHT);
		gOffScream.setColor(c);
		
		paint(gOffScream);//调用paint方法。使这张图片也画上   paint方法里面的图片
		
		g.drawImage(offScreamImage, 0, 0, null);
	}

	
	public void lauchFrame(){
		//获取屏幕信息
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension size = kit.getScreenSize();
		WIDTH = size.width/2+size.width/4;
		HEIGHT = size.height/2+size.height/4;
		this.setBounds(size.width/8,size.height/8,WIDTH,HEIGHT);
		
		Image image = kit.getImage("1.jpg");
		setIconImage(image);
		
//		this.setResizable(false);//设置不可以调整大小
		this.setBackground(Color.gray);
//		setUndecorated(true);//把标题栏去掉
//		setUndecorated(true);//全屏
//		getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
		
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addKeyListener(myTank.new MyKeyEvent());
		
		for(int i = 1;i<15;i++){
			enemyTank.add(new Tank(50+50*i,50,false,Direction.STOP,this));
		}
		
		setVisible(true);
		
		new Thread(new PaintThread()).start();//创建一个线程，重画
	}
	
	public static void main(String[] args) {
		new TankClient().lauchFrame();
	}
	
	//内部类
	private class PaintThread implements Runnable{
		public void run() {
			while(true){
				repaint();
				try {
					Thread.sleep(28);//每80毫秒重画一次
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
/*//获取屏幕信息
   Toolkit kit = Toolkit.getDefaultToolkit();
   Dimension size = kit.getScreenSize();
   int screenWidth = size.width;
   int screenHeight = size.height;
   //设置窗口宽高
   jf.setSize(screenWidth/4, screenHeight/4);
   jf.setLocation(screenWidth/4, screenHeight/4);
   //设置图标
   Image image = kit.getImage("smile.gif");
   jf.setIconImage(image);
 */
