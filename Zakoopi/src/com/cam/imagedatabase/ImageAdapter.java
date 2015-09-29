//package com.cam.imagedatabase;
//
//import java.io.ByteArrayInputStream;
//import java.util.ArrayList;
//
//import com.image.effects.PixelBuffer;
//import com.mycam.ImageDetail;
//import com.mycam.R;
//import com.mycam.GalleryImageAdapter.BlurImagesTask;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//
//public class ImageAdapter extends BaseAdapter {
//	ArrayList<byte[]> imglist = new ArrayList<byte[]>();
//	Context ctyx;
//	LayoutInflater inf;
//	ArrayList<String> id = new ArrayList<String>();
//	private SQLiteDatabase db;
//	public static final String DBTABLE = "lookbook";
//
//	public ImageAdapter(Context ctx, ArrayList<byte[]> list,
//			ArrayList<String> id) {
//		this.ctyx = ctx;
//		imglist = list;
//		this.id = id;
//		inf = (LayoutInflater) ctyx
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return imglist.size();
//	}
//
//	@Override
//	public Object getItem(int arg0) {
//		// TODO Auto-generated method stub
//		return arg0;
//	}
//
//	@Override
//	public long getItemId(int arg0) {
//		// TODO Auto-generated method stub
//		return arg0;
//	}
//
//	@Override
//	public View getView(int arg0, View arg1, ViewGroup arg2) {
//		// TODO Auto-generated method stub
//		ViewHolder holder;
//		View view = arg1;
//		if (view == null) {
//			holder = new ViewHolder();
//			view = inf.inflate(R.layout.image_detail_list_item, null);
//			holder.img = (ImageView) view.findViewById(R.id.imageView1);
//			holder.del = (ImageView) view.findViewById(R.id.imageView2);
//			view.setTag(holder);
//
//		} else {
//
//			holder = (ViewHolder) view.getTag();
//		}
//
//		holder.del.setTag(arg0);
//		byte[] outImage = imglist.get(arg0);
//		ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
//		Bitmap theImage = BitmapFactory.decodeStream(imageStream);
//		new BlurImagesTask(arg0, holder.img).execute(theImage);
//
//		holder.del.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Integer index = (Integer) arg0.getTag();
//				imglist.remove(index.intValue());
//				notifyDataSetChanged();
//
//				DBHelper hp = new DBHelper(ctyx);
//				db = hp.getWritableDatabase();
//				db.delete(DBTABLE, "id = "+id.get(index), null);
//                Log.e("lllll", id.get(index));
//			}
//		});
//
//		return view;
//	}
//
//	class ViewHolder {
//
//		ImageView img;
//		ImageView del;
//	}
//
//	public class BlurImagesTask extends AsyncTask<Bitmap, Void, Bitmap> {
//
//		private ImageView roundedImageView;
//		int position;
//
//		public BlurImagesTask(int pos, ImageView imageView) {
//			this.position = pos;
//
//			this.roundedImageView = imageView;
//		}
//
//		@Override
//		protected Bitmap doInBackground(Bitmap... param) {
//			Bitmap bitmap = param[0];
//
//			return bitmap;
//		}
//
//		@Override
//		protected void onPostExecute(Bitmap result) {
//			super.onPostExecute(result);
//
//			roundedImageView.setImageBitmap(result);
//		}
//
//	}
//
//}
