package com.cam.imagedatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zakoopi.R;


public class PublishListAdapter extends BaseAdapter{
	ArrayList<byte[]>imglist=new ArrayList<byte[]>();
	Context ctyx;
	LayoutInflater inf;
	
    
	public PublishListAdapter(Context ctx,ArrayList<byte[]>list){
		this.ctyx=ctx;
		imglist=list;
		inf=(LayoutInflater)ctyx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imglist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		View view=arg1;
		if(view==null){
			holder=new ViewHolder();
			view=inf.inflate(R.layout.publish_list_item, null);
			holder.img=(ImageView)view.findViewById(R.id.imageView1);
			view.setTag(holder);
			
		}else{
			
			holder=(ViewHolder)view.getTag();
		}
		
		
		byte[] outImage=imglist.get(arg0);
		ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
		Bitmap theImage = BitmapFactory.decodeStream(imageStream);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		theImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		new BlurImagesTask(arg0, holder.img).execute(theImage);
		
		return view;
	}
	
    class ViewHolder{
    	
    	ImageView img;
    }
    
    
    public class BlurImagesTask extends AsyncTask<Bitmap, Void, Bitmap>{

		private ImageView roundedImageView;
		int position;
		
		public BlurImagesTask(int pos, ImageView imageView){
			this.position = pos;
			
			this.roundedImageView = imageView;
		}
		
		@Override
		protected Bitmap doInBackground(Bitmap... param) {
			Bitmap bitmap = param[0];
			
		    
			return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			
				roundedImageView.setImageBitmap(result);
			
			
		}
		
	}

    
}
