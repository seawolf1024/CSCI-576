# CSCI-576: Assignment 1

#### Run

```cmd
java ImageDisplay [imgPath] [width] [height] [resamplingMethod] [targetImgSize]
// example
java ImageDisplay ../hw1_1_high_res.rgb 4000 3000 1 O1
java ImageDisplay ../hw1_1_high_res.rgb 4000 3000 2 O1
java ImageDisplay ../hw1_1_low_res.rgb 400 300 3 O1
java ImageDisplay ../hw1_1_low_res.rgb 400 300 4 O1
```

#### Resampling Method

There are 4 methods for `[resamplingMethod]`:

`resamplingMethod = 1`: down-sample, specific sampling 

`resamplingMethod = 2`: down-sample, average smoothing

`resamplingMethod = 3`: up-sample, nearest neighbor

`resamplingMethod = 4`: up-sample, bilinear Interpolation
