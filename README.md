# EasyImageLoader
轻量级图片加载库。具有图片加载，图片缓存、图片压缩功能。
将内存缓存、本地缓存、网络请求、图片压缩、线程池封装，做到模块独立，降低耦合度，只给外层提供ImagLoader的bindBitmap方法加载图片解决数据错乱问题，能够在加载图片时添加loadding图片，加载失败时自动加载error图片



##示例
![EasyImageLoaderSample.gif](EasyImageLoaderSample.gif)

##依赖 
该库依赖以下常见库

compile 'com.jakewharton:disklrucache:2.0.2' //Google推荐的本地缓存库

compile 'com.google.code.gson:gson:2.3.1'    //解析json库，用于封装的网络请求库自动解析json

##EasyImageLoader的使用
   
    //根据图片url给imageView加载图片，自动本地缓存、内存缓存
    EasyImageLoader.getInstance(context).bindBitmap(imageUrl, imageView);
    
    //重载方法加载图片并根据需求宽高压缩图片
    EasyImageLoader.getInstance(context).bindBitmap(imageUrl, imageView,reqWidth,reqHeight);
    
    //根据url自动从内存缓存、本地缓存、网络获取bitmap，并回调
    EasyImageLoader.getInstance(context).getBitmap(imageUrl, new EasyImageLoader.BitmapCallback() {
            @Override
            public void onResponse(Bitmap bitmap) {
                //保存bitmap到本地
                saveBitmap(bitmap);
            }
        });
    
    


列如:

    public class ImageItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        private Context context;
        public ImageItemViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_image,parent,false));
            imageView =(ImageView)itemView.findViewById(R.id.itemview_img);
            context =parent.getContext();
        }
        //绑定数据
        public void setData(NetImage netImage){
                //加载图片
                EasyImageLoader.getInstance(context).bindBitmap(netImage.getImageUrl, imageView);
            }
  }


**详细用法请看demo**
