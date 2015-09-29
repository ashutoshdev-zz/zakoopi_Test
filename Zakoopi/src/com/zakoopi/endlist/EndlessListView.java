package com.zakoopi.endlist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;

public class EndlessListView extends ListView {
	@SuppressWarnings("unused")
	private final static String TAG = "EndlessListView";
	protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
	protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";
	private ImageLoader imageLoader;
	protected AbsListView listView;

	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;

	public interface OnLoadMoreListener {
		public boolean onLoadMore();
	}

	private boolean mIsLoading;
	private ProgressBar progressBar;
	private OnLoadMoreListener onLoadMoreListener;
	Context ctx;

	public EndlessListView(Context context) {
		super(context);
		ctx = context;
		init();
	}

	public EndlessListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		this.onLoadMoreListener = onLoadMoreListener;
	}

	public void loadMoreCompleat() {
		mIsLoading = false;
		progressBar.setVisibility(View.GONE);
		

	}

	private void init() {
		mIsLoading = false;

		progressBar = new ProgressBar(getContext(), null,
				android.R.attr.progressBarStyleInverse);
		LinearLayout.LayoutParams progressBarParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		progressBar.setLayoutParams(progressBarParams);
		progressBar.setPadding(6, 6, 6, 6);
		progressBar.setVisibility(View.GONE);

		LinearLayout footerLinearLayout = new LinearLayout(getContext());
		LayoutParams layoutParams = new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		footerLinearLayout.setGravity(Gravity.CENTER);
		footerLinearLayout.setLayoutParams(layoutParams);
		footerLinearLayout.addView(progressBar);

		addFooterView(footerLinearLayout);

		super.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), pauseOnScroll, pauseOnFling));
	}

	// private class ELScrollChangedListener implements OnScrollListener {
	//
	// @Override
	// public void onScrollStateChanged(AbsListView view, int scrollState) {
	//
	// }
	//
	// @Override
	// public void onScroll(AbsListView view, int firstVisibleItem,
	// int visibleItemCount, int totalItemCount) {
	//
	// boolean loadMore;
	// loadMore = (0 != totalItemCount)
	// && ((firstVisibleItem + visibleItemCount) >= (totalItemCount));
	//
	// if (false == mIsLoading && true == loadMore) {
	//
	// if (null != onLoadMoreListener) {
	// if (onLoadMoreListener.onLoadMore()) {
	// mIsLoading = true;
	// progressBar.setVisibility(View.VISIBLE);
	//
	// }
	// }
	// }
	// }
	// }

	public class PauseOnScrollListener implements OnScrollListener {

	

		private final boolean pauseOnScroll;
		private final boolean pauseOnFling;
		//private final OnScrollListener externalListener;

		/**
		 * Constructor
		 *
		 * @param imageLoader
		 *            {@linkplain ImageLoader} instance for controlling
		 * @param pauseOnScroll
		 *            Whether {@linkplain ImageLoader#pause() pause ImageLoader}
		 *            during touch scrolling
		 * @param pauseOnFling
		 *            Whether {@linkplain ImageLoader#pause() pause ImageLoader}
		 *            during fling
		 */
		public PauseOnScrollListener(ImageLoader imageLoader,
				boolean pauseOnScroll, boolean pauseOnFling) {
			this(imageLoader, pauseOnScroll, pauseOnFling, null);
		}

		/**
		 * Constructor
		 *
		 * @param imageLoader
		 *            {@linkplain ImageLoader} instance for controlling
		 * @param pauseOnScroll
		 *            Whether {@linkplain ImageLoader#pause() pause ImageLoader}
		 *            during touch scrolling
		 * @param pauseOnFling
		 *            Whether {@linkplain ImageLoader#pause() pause ImageLoader}
		 *            during fling
		 * @param customListener
		 *            Your custom {@link OnScrollListener} for
		 *            {@linkplain AbsListView list view} which also will be get
		 *            scroll events
		 */
		public PauseOnScrollListener(ImageLoader imageLoader1,
				boolean pauseOnScroll, boolean pauseOnFling,
				OnScrollListener customListener) {
			imageLoader = imageLoader1;
			this.pauseOnScroll = pauseOnScroll;
			this.pauseOnFling = pauseOnFling;
			//externalListener = customListener;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				imageLoader.resume();
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				if (pauseOnScroll) {
					imageLoader.pause();
				}
				break;
			case OnScrollListener.SCROLL_STATE_FLING:
				if (pauseOnFling) {
					imageLoader.pause();
				}
				break;
			}
//			if (externalListener != null) {
//				externalListener.onScrollStateChanged(view, scrollState);
//			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			//if (externalListener != null) {

				boolean loadMore;
				loadMore = (0 != totalItemCount)
						&& ((firstVisibleItem + visibleItemCount) >= (totalItemCount));

				if (false == mIsLoading && true == loadMore) {

					if (null != onLoadMoreListener) {
						if (onLoadMoreListener.onLoadMore()) {
							mIsLoading = true;
							progressBar.setVisibility(View.VISIBLE);

						}
					}
				}

//				externalListener.onScroll(view, firstVisibleItem,
//						visibleItemCount, totalItemCount);
			//}
		}
	}

}
