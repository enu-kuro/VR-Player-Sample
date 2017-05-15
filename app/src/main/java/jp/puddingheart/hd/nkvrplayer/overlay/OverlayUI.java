/* Copyright 2015 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.puddingheart.hd.nkvrplayer.overlay;

import android.media.MediaPlayer;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRRenderData;
import org.gearvrf.GVRSceneObject;

import jp.puddingheart.hd.nkvrplayer.R;
import jp.puddingheart.hd.nkvrplayer.focus.OnClickListener;
import jp.puddingheart.hd.nkvrplayer.movie.MovieManager;

public class OverlayUI extends GVRSceneObject {

    private static final String TAG = "OverlayUI";

    private GVRSceneObject mButtonBoard = null;
    private Button mPlayButton = null;
    private Button mPauseButton = null;
//    private Button mFrontButton = null;
//    public Button mBackButton = null;

    public Button mGoTopButton = null;

    private Seekbar mSeekbar = null;
    private MovieManager mMovieManager = null;

    public Boolean isVisible = true;

    private static final float BUTTON_POSITION_Y = -1.2f;

    private static final int SEEK_TIME_LENGTH = 1000;

    public OverlayUI(GVRContext context, final MovieManager movieManager) {
        super(context);
        mMovieManager = movieManager;

        // button board
        mButtonBoard = new GVRSceneObject(context, context.createQuad(8.2f, 1.35f),
                context.getAssetLoader().loadTexture(new GVRAndroidResource(context, R.drawable.button_board)));
        mButtonBoard.getTransform().setPosition(-0.1f, BUTTON_POSITION_Y + 0.2f, -8.0f);
        mButtonBoard.getRenderData().setRenderingOrder(
                GVRRenderData.GVRRenderingOrder.TRANSPARENT);
        addChildObject(mButtonBoard);

        // play
        mPlayButton = new Button(context, context.createQuad(0.7f, 0.7f),
                context.getAssetLoader().loadTexture(new GVRAndroidResource(context, R.drawable.play_active)),
                context.getAssetLoader().loadTexture(new GVRAndroidResource(context, R.drawable.play_inactive)));
        mPlayButton.setPosition(0.0f, BUTTON_POSITION_Y, -8.0f);
        mPlayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                if(playVideo()) {
                    mPlayButton.hide();
                    mPauseButton.show();
                }
            }
        });
        addChildObject(mPlayButton);

        // pause
        mPauseButton = new Button(context, context.createQuad(0.7f, 0.7f),
                context.getAssetLoader().loadTexture(new GVRAndroidResource(context, R.drawable.pause_active)),
                context.getAssetLoader().loadTexture(new GVRAndroidResource(context, R.drawable.pause_inactive)));
        mPauseButton.setPosition(0.0f, BUTTON_POSITION_Y, -8.0f);
        mPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                if(pauseVideo()) {
                    mPauseButton.hide();
                    mPlayButton.show();
                }
            }
        });
        addChildObject(mPauseButton);

        // 挙動がイマイチなのでコメントアウト
        /*
        // next
        mFrontButton = new Button(context, context.createQuad(0.7f, 0.7f),
                context.getAssetLoader().loadTexture(new GVRAndroidResource(context, R.drawable.front_active)),
                context.getAssetLoader().loadTexture(new GVRAndroidResource(context, R.drawable.front_inactive)));
        mFrontButton.setPosition(1.2f, BUTTON_POSITION_Y, -8.0f);
        mFrontButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                if (movieManager.getMediaPlayer() != null) {
                    movieManager.playerSeekTo(movieManager.playerGetCurrentPosition() + SEEK_TIME_LENGTH);
                }
            }
        });
        addChildObject(mFrontButton);

        // prev
        mBackButton = new Button(context, context.createQuad(0.7f, 0.7f),
                context.getAssetLoader().loadTexture(new GVRAndroidResource(context, R.drawable.back_active)),
                context.getAssetLoader().loadTexture(new GVRAndroidResource(context, R.drawable.back_inactive)));
        mBackButton.setPosition(-1.2f, BUTTON_POSITION_Y, -8.0f);
        mBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                if (movieManager.getMediaPlayer() != null) {
                    movieManager.playerSeekTo(movieManager.playerGetCurrentPosition() - SEEK_TIME_LENGTH);
                }
            }
        });
        addChildObject(mBackButton);
*/

        mSeekbar = new Seekbar(context);
        addChildObject(mSeekbar);



        mGoTopButton = new Button(context, context.createQuad(0.7f , 0.7f ),
                context.getAssetLoader().loadTexture(new GVRAndroidResource(context, R.drawable.home_active)),
                context.getAssetLoader().loadTexture(new GVRAndroidResource(context, R.drawable.home_active)));
        mGoTopButton.setPosition(0.0f, 1.2f, -8.0f);
        addChildObject(mGoTopButton);


        ((MediaPlayer)mMovieManager.getMediaPlayer()).setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                show();
            }
        });
    }

    public boolean playVideo() {
        android.util.Log.d(TAG, "pressed Play button");
        if (!mMovieManager.isPlaying()) {
            mMovieManager.playerStart();
            return true;
        }
        return false;
    }

    public boolean pauseVideo() {
        android.util.Log.d(TAG, "pressed Pause button");
        if (mMovieManager.isPlaying()) {
            mMovieManager.playerPause();
            return true;
        }
        return false;
    }

    public void show() {
        mButtonBoard.getRenderData().setRenderMask(GVRRenderData.GVRRenderMaskBit.Left
                | GVRRenderData.GVRRenderMaskBit.Right);
        if (mMovieManager.isPlaying()) {
            mPlayButton.hide();
            mPauseButton.show();
        } else {
            mPauseButton.hide();
            mPlayButton.show();
        }
//        mFrontButton.show();
//        mBackButton.show();
        mSeekbar.setRenderMask(GVRRenderData.GVRRenderMaskBit.Left
                | GVRRenderData.GVRRenderMaskBit.Right);
        mGoTopButton.show();
        isVisible = true;
    }

    public void hide() {
        //mHeadTracker.getRenderData().setRenderMask(0);
        mButtonBoard.getRenderData().setRenderMask(0);
        mPlayButton.hide();
        mPauseButton.hide();
//        mFrontButton.hide();
//        mBackButton.hide();
        mSeekbar.setRenderMask(0);
        mGoTopButton.hide();
        isVisible = false;
    }

    public void updateOverlayUI(GVRContext context) {
        Float seekBarRatio = mSeekbar.getRatio(context.getMainScene().getMainCameraRig().getLookAt());
        if (seekBarRatio != null) {
            mSeekbar.glow();
        } else {
            mSeekbar.unglow();
        }
        if (mMovieManager.getMediaPlayer() != null) {
            mSeekbar.setTime(context, mMovieManager.playerGetCurrentPosition(),
                      mMovieManager.playerGetDuration());
        }


    }

    public boolean isOverlayPointed(GVRContext context) {
        return mSeekbar.getRatio(context.getMainScene().getMainCameraRig().getLookAt()) != null ? true : false;
    }

    public void processTouch(GVRContext context) {
        Float seekBarRatio = mSeekbar.getRatio(context.getMainScene().getMainCameraRig().getLookAt());
        if (seekBarRatio != null && mMovieManager.getMediaPlayer() != null) {
            long current = (long) (mMovieManager.playerGetDuration() * seekBarRatio);
            mMovieManager.playerSeekTo(current);
            mSeekbar.setTime(context, current, mMovieManager.playerGetDuration());
        }
    }
}
