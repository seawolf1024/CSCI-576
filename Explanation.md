# CSCI-576: Assignment 1

Zhanshan Zhang, id = 7527742899

### Explanation

The implementation adds a new method called `resampleImage()` to create a new resampled image. Based on the input parameters, there are 4 methods to do the corresponding resampling.

Command line format:

```
java ImageDisplay [imgPath] [width] [height] [resamplingMethod] [targetImgSize]
// example
java ImageDisplay ../hw1_1_high_res.rgb 4000 3000 1 O1
java ImageDisplay ../hw1_1_high_res.rgb 4000 3000 2 O1
java ImageDisplay ../hw1_1_low_res.rgb 400 300 3 O1
java ImageDisplay ../hw1_1_low_res.rgb 400 300 4 O1
```

There are 4 methods for `[resamplingMethod]`:

`resamplingMethod = 1`: down-sample, specific sampling 

`resamplingMethod = 2`: down-sample, average smoothing

`resamplingMethod = 3`: up-sample, nearest neighbor

`resamplingMethod = 4`: up-sample, bilinear Interpolation

#### Method 1

down-sample, specific sampling 

This method find the pixel in the original image proportionally and fill it to the target image.

targetImage(x, y) =  originalImage(x * oldW / newW, y * oldH / newH)

**Output**: This outputs a reduced image.

#### Method 2

down-sample, average smoothing

This method find the blocks of pixels in the original image, calculate their average value, and fill it to the target image.

**Output**: This outputs a reduced image, but the image is smoother.

#### Method 3

up-sample, nearest neighbor

This method find the pixel in the original image proportionally and fill it to the target image. Adjacent pixels in the target image might have the same value because they are mapped into the same pixel in the original image.

**Output**: This outputs a enlarged image, but the image is unclear and jagged.

#### Method 4

up-sample, bilinear Interpolation

This method do bilinear interpolation for pixels in original image to calculate the pixels in the target image. 

- For each pixel (x, y) in the target image.
- The pixel is mapped into position between (x1, y1), (x2, y2), (x3, y3), (x4, y4) in the original image.
- xDiff = x * oldW / newW - x1, yDiff = y * oldH / newH - y1
- The value of the pixel (x, y) in the target image follow this formula: 
- (x, y) = (x1, y1) * (1 - xDiff)(1 - yDiff) + (x2, y1) * (1 - xDiff) * yDiff + (x1, y2) * xDiff * (1 - yDiff) + (x2, y2) * xDiff * yDiff

**Output**: This outputs a enlarged image. Due to bilinear interpolation, the image is clearer than the one in Method 3.



### Discussion

#### Pixel Aspect Ratio (PAR) changes while down sampling

Method: 



#### Seam Carving as a solution

Method:



#### Up sampling image quality

Method:

