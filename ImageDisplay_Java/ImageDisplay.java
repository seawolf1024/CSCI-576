
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;


public class ImageDisplay {

	JFrame frame;
	JLabel lbIm1;
	BufferedImage imgOne;

	// Modify the height and width values here to read and display an image with
  	// different dimensions. 
	// int width = 256;
	// int height = 512;

	/** Read Image RGB
	 *  Reads the image of given width and height at the given imgPath into the provided BufferedImage.
	 */
	private void readImageRGB(int width, int height, String imgPath, BufferedImage img)
	{
		try
		{
			int frameLength = width*height*3;

			File file = new File(imgPath);
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(0);

			long len = frameLength;
			byte[] bytes = new byte[(int) len];

			raf.read(bytes);

			int ind = 0;
			for(int y = 0; y < height; y++)
			{
				for(int x = 0; x < width; x++)
				{
					byte a = 0;
					byte r = bytes[ind];
					byte g = bytes[ind+height*width];
					byte b = bytes[ind+height*width*2]; 

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
					img.setRGB(x,y,pix);
					ind++;
				}
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	
	private BufferedImage resampleImage(BufferedImage img, int newWidth, int newHeight, String method){
		BufferedImage resampledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		System.out.println("newWidth = " + newWidth + ", newHeight = " + newHeight);
		switch (method) {
			case "1":
				System.out.println("method = " + method);
				downsampleSpecific(img, resampledImage); // down-sample, specific sampling 
				break;
			case "2": // down-sample, average smoothing
				downsampleAverage(img, resampledImage);
				break;
			case "3": // up-sample, nearest neighbor
                upsampleNearest(img, resampledImage);
                break;
            case "4": // up-sample, bilinear Interpolation
                upsampleBilinear(img, resampledImage);
                break;

			default:
				break;
		}
		return resampledImage;
	}

    private void downsampleSpecific(BufferedImage img, BufferedImage resampledImage) {
        int w = img.getWidth();
        int h = img.getHeight();
        int newW = resampledImage.getWidth();
        int newH = resampledImage.getHeight();

		System.out.println("w = " + w + ", h = " + h + ", newW = " + newW + ", newH = " + newH);
        for (int y = 0; y < newH; y++) {
            for (int x = 0; x < newW; x++) {
                int oldX = x * w / newW;
                int oldY = y * h / newH;
                resampledImage.setRGB(x, y, img.getRGB(oldX, oldY));
            }
        }
    }

    private void downsampleAverage(BufferedImage img, BufferedImage resampledImage) {
        int w = img.getWidth();
        int h = img.getHeight();
        int newW = resampledImage.getWidth();
        int newH = resampledImage.getHeight();

        for (int y = 0; y < newH; y++) {
            for (int x = 0; x < newW; x++) {
                int startX = x * w / newW;
                int startY = y * h / newH;
                int endX = Math.min((x + 1) * w / newW, w);
                int endY = Math.min((y + 1) * h / newH, h);
                
                int sumR = 0, sumG = 0, sumB = 0, count = 0;
                for (int yy = startY; yy < endY; yy++) {
                    for (int xx = startX; xx < endX; xx++) {
                        int rgb = img.getRGB(xx, yy);
                        sumR += (rgb >> 16) & 0xFF;
                        sumG += (rgb >> 8) & 0xFF;
                        sumB += rgb & 0xFF;
                        count++;
                    }
                }
                int avgR = sumR / count;
                int avgG = sumG / count;
                int avgB = sumB / count;
                int avgColor = (0xFF << 24) | (avgR << 16) | (avgG << 8) | avgB;
                resampledImage.setRGB(x, y, avgColor);
            }
        }
    }

    private void upsampleNearest(BufferedImage img, BufferedImage resampledImage) {
        downsampleSpecific(img, resampledImage); // reuse
    }

	private void upsampleBilinear(BufferedImage img, BufferedImage resampledImage) {
		int oldWidth = img.getWidth();
		int oldHeight = img.getHeight();
		int newWidth = resampledImage.getWidth();
		int newHeight = resampledImage.getHeight();

		for (int y = 0; y < newHeight; y++) {
			for (int x = 0; x < newWidth; x++) {
				float srcX = (float) x * oldWidth / newWidth;
				float srcY = (float) y * oldHeight / newHeight;

				int x1 = (int) Math.floor(srcX);
				int y1 = (int) Math.floor(srcY);
				int x2 = Math.min(x1 + 1, oldWidth - 1);
				int y2 = Math.min(y1 + 1, oldHeight - 1);

				float xDiff = srcX - x1;
				float yDiff = srcY - y1;

				int c00 = img.getRGB(x1, y1);
				int c10 = img.getRGB(x2, y1);
				int c01 = img.getRGB(x1, y2);
				int c11 = img.getRGB(x2, y2);

				int r00 = (c00 >> 16) & 0xFF;
				int g00 = (c00 >> 8) & 0xFF;
				int b00 = c00 & 0xFF;

				int r10 = (c10 >> 16) & 0xFF;
				int g10 = (c10 >> 8) & 0xFF;
				int b10 = c10 & 0xFF;

				int r01 = (c01 >> 16) & 0xFF;
				int g01 = (c01 >> 8) & 0xFF;
				int b01 = c01 & 0xFF;

				int r11 = (c11 >> 16) & 0xFF;
				int g11 = (c11 >> 8) & 0xFF;
				int b11 = c11 & 0xFF;

				int r = (int) ((r00 * (1 - xDiff) * (1 - yDiff)) +
							(r10 * xDiff * (1 - yDiff)) +
							(r01 * (1 - xDiff) * yDiff) +
							(r11 * xDiff * yDiff));

				int g = (int) ((g00 * (1 - xDiff) * (1 - yDiff)) +
							(g10 * xDiff * (1 - yDiff)) +
							(g01 * (1 - xDiff) * yDiff) +
							(g11 * xDiff * yDiff));

				int b = (int) ((b00 * (1 - xDiff) * (1 - yDiff)) +
							(b10 * xDiff * (1 - yDiff)) +
							(b01 * (1 - xDiff) * yDiff) +
							(b11 * xDiff * yDiff));

				int newColor = (0xFF << 24) | (r << 16) | (g << 8) | b;
				resampledImage.setRGB(x, y, newColor);
			}
		}
	}


	public void showIms(String[] args){
		// Read in the specified image
		String imgPath = args[0];
		int width = Integer.parseInt(args[1]);
		int height = Integer.parseInt(args[2]);
		String resamplingMethod = args[3];
		String outputFormat = args[4];
		
		int targetWidth = 0;
		int targetHeight = 0;

		switch (outputFormat) {
            case "O1":
                targetWidth = 1920;
                targetHeight = 1080;
                break;
            case "O2":
                targetWidth = 1280;
                targetHeight = 720;
                break;
            case "O3":
                targetWidth = 640;
                targetHeight = 480;
                break;
            default:
                System.out.println("Invalid output format. Please use O1, O2, or O3.");
                return;
        }

		imgOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		readImageRGB(width, height, imgPath, imgOne);

		// resample image
        BufferedImage resampledImage = resampleImage(imgOne, targetWidth, targetHeight, resamplingMethod);

		// Use label to display the image
        // frame = new JFrame();
        // frame.setLayout(new BorderLayout());
        // lbIm1 = new JLabel(new ImageIcon(resampledImage));
        // frame.add(lbIm1, BorderLayout.CENTER);
        // frame.pack();
        // frame.setVisible(true);

		frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);

		lbIm1 = new JLabel(new ImageIcon(resampledImage));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		frame.getContentPane().add(lbIm1, c);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		ImageDisplay ren = new ImageDisplay();
		ren.showIms(args);
	}

}
