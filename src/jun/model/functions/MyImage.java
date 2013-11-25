package jun.model.functions;

import java.awt.Image;

import javax.swing.ImageIcon;

import comp310f13.rmiChat.IImage;

public class MyImage implements IImage {

	private static final long serialVersionUID = -5974503273697259588L;
	private ImageIcon imgicon;
	
	public MyImage(){}
	public MyImage(Image myImg){
		this.imgicon = new ImageIcon(myImg);
	}

	public ImageIcon getImg() {
		return this.imgicon;
	}
}
