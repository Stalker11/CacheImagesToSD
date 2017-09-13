# CacheImagesToSD
Module for download and cache pictures from url to sd card.<br/>  

For using this module add in your gradle this code:
```
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```
 Add the dependency:
 ```
 dependencies {
	        compile 'com.github.Stalker11:CacheImagesToSD:r.r.r'
	}
  ```
  Where r.r.r last release version.<br/>
  For implement this module in your project you have to create class **CacheLoader** where you can set the required parameters, for instance:
  ```
 CacheLoader loader = new CacheLoader.Builder().setContextBuilder(context).setUrl("ImageURL",
                "MyPictureName.jpg").setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setQuality(100).build();
```
If you do not need resize image you can set container for saving image:
```
CacheLoader loader = new CacheLoader.Builder().setInto(imageView);
loader.saveImageFromURL();
```
If you need to change image before set into container you can use interface **CacheLoaderCallBack**:
```
loader.saveImageFromURL(new CacheLoaderCallBack() {
            @Override
            public void onSuccess() {                       
                                loader.getPictureFromCache();
                               
            }

            @Override
            public void onError(String s) {
                loader.getPictureFromCache();
            }
        });
 ```
        

