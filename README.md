# AndroidTool
主要是积累一些常用的工具类、view等。
## 工具类（common）
工具类主要是在 `common` 这个module下：
1. `ImageUtils` : 有关图片的工具类；
2. `ScreenUtils` : 有关屏幕尺寸、获取宽高（包括给定文字内容及大小来获取其宽）的工具类；
3. `TimeUtils` : 有关时间转化及获取的工具类；
4. `Worker` : 线程切换，即异步操作、切换回主线程的工具类；
5. ......

## 主程序（app）
主程序中包括了一些基类、常用的view等。
#### 基类
1. `BaseActivity` :
2. `BaseApplication` :
3. `BaseFragment` :
#### `RecyclerView` 的封装
##### 适配器（Adapter）的封装：
通过 `BaseItemData` 、 `BaseItemType` 、 `BaseItemView` 的组合，只要我们继承 `BaseRecyclerViewAdapter` 
就可以很容易的实现多种item的列表视图结构；具体使用方法请查看项目中的使用例子。
##### 特定的item不被 `onDetachedFromWindow()` 的recyclerView：`SpecificRecyclerView`
**需求**: 有时候我们需要在列表的顶部放一个播放视频的item，如果按照正常的写法来完成，当此item离开手机的可视范围，然后再回来，视频就得重新加载，就会出现闪一下的效果，这样的体验是过不了**pm**这关的.

**原因**: 这是由于RecyclerView的回收机制导致的，当item离开手机的可视范围，就会调用此 `itemView` 的 `onDetachedFromWindow()` 方法,回收此view, 当此 `itemView`
 再次回到可视范围后，就得调用此 `itemView` 的 `onAttachedToWindow()` 这个方法；加载视频并初始化视频的播放器本来就是一个耗时的过程，在加上 `itemView` 脱离 `window` 
 了，这些时间加起来，就会导致带有视频的 `itemView` 再此回来会闪一下。
 
**解决方案**: 想办法不让 `itemView` 脱离 `window`，即不调用 `onDetachedFromWindow()` 方法；

1. 用 `ListView` 替换 `RecyclerView`, 这是因为 `ListView` 回收机制不会导致 `itemView` 调用 `onDetachedFromWindow()` 方法；
2. 在 `RecyclerView` 上盖一层 `view`,来替换带有视频的`itemView`, 然后让此 `view` 跟随 `RecyclerView` 一起滚动。（***详见 `SpecificRecyclerView` 
的使用及实现***）
> `ListView` 与 `RecyclerView`的区别：
> 1. `ListView` 的回收机制不会调用 `itemView` 的 `onDetachedFromWindow()` 方法，而 `RecyclerView` 的回收机制会调用此方法.
##### `RecyclerView` 下拉刷新与加载更多？？？

#### 常用的自定义View
##### `ShapeImageView`
1. `ShapeImageView` 是对图片形状的处理，而且是**抗锯齿的**，包括圆角矩形（设置各个圆角）`RoundImageView`、圆形 `CircleImageView`、椭圆形 `OvalImageView`; 
2. `ShapeImageView` 还支持加边框，边框的颜色、宽度可以自己设置。
##### `RoundByClipImageView`
1. `RoundByClipImageView` 可以实现圆角矩形，但是不能抗锯齿；
2. 其原理是使用canvas.clipPath()方法来剪切画布的，此方不能用Region.Op.REPLACE 作为参数，因为如果使用了Region.Op
.REPLACE，且此 `RoundByClipImageView` 在一个可滚动的列表中， 就是导致在Vivo Y67 API 23等机子上出现此`RoundByClipImageView` 把上层View替换掉的情况,
如：[Region.Op.REPLACE导致的问题](image/clip_round_view_icon.JPG)