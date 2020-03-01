package com.builtclean.android.livewallpapers.androidrobot;

import com.builtclean.android.livewallpapers.androidrobot.R;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class AndroidRobot extends WallpaperService {

	private final Handler mHandler = new Handler();

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new OceanEngine();
	}

	class OceanEngine extends Engine {

		public int offsetX = -200;
		public int offsetY = 0;
		public int height;
		public int width;
		public int visibleWidth;

		public int nextImage;
		public int currentImage = 0;

		public String[] images = { "image0", "image1", "image2", "image3",
				"image4", "image5", "image6", "image7", "image8", "image9",
				"image10", "image11", "image12", "image13", "image14",
				"image15", "image16", "image17", "image18", "image19",
				"image20", "image21", "image22", "image23", "image24",
				"image25", "image26", "image27", "image28", "image29",
				"image30", "image31", "image32", "image33", "image34",
				"image35" };

		private final Runnable mDrawOcean = new Runnable() {
			public void run() {
				drawFrame();
			}
		};
		private boolean mVisible;

		private MediaPlayer androidrobotPlayer;

		OceanEngine() {
			Resources res = getResources();
			nextImage = res.getIdentifier("image0", "drawable",
					"com.builtclean.android.livewallpapers.androidrobot");
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);

			setTouchEventsEnabled(true);

			androidrobotPlayer = MediaPlayer.create(getApplicationContext(),
					R.raw.androidrobot);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mDrawOcean);

			androidrobotPlayer.release();
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				drawFrame();
			} else {
				mHandler.removeCallbacks(mDrawOcean);
			}
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mDrawOcean);
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {

			this.height = height;
			this.width = width;
			this.visibleWidth = width;

			drawFrame();

			super.onSurfaceChanged(holder, format, width, height);
		}

		@Override
		public Bundle onCommand(String action, int x, int y, int z,
				Bundle extras, boolean resultRequested) {

			Bundle bundle = super.onCommand(action, x, y, z, extras,
					resultRequested);

			if (action.equals("android.wallpaper.tap")) {
				playOceanSound();
			}

			return bundle;
		}

		void drawFrame() {
			final SurfaceHolder holder = getSurfaceHolder();

			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					drawOcean(c);
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}

			mHandler.removeCallbacks(mDrawOcean);
			if (mVisible) {
				mHandler.post(mDrawOcean);
			}
		}

		void drawOcean(Canvas c) {

			Resources res = getResources();

			if (++currentImage == images.length) {
				currentImage = 0;
			}

			nextImage = res.getIdentifier(images[currentImage], "drawable",
					"com.builtclean.android.livewallpapers.androidrobot");

			c.drawBitmap(BitmapFactory.decodeResource(res, nextImage),
					this.offsetX, this.offsetY, null);
		}

		void playOceanSound() {
			androidrobotPlayer.seekTo(0);
			androidrobotPlayer.start();
		}
	}
}