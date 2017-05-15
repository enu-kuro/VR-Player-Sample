package jp.puddingheart.hd.nkvrplayer;

import org.gearvrf.GVRActivity;
import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRPicker;
import org.gearvrf.GVRRenderData;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;

import jp.puddingheart.hd.nkvrplayer.focus.FocusableController;

/**
 * Created by Nobuhito Kurose on 2017/04/19.
 */


public class Main extends GVRMain {

    private GVRContext mGVRContext = null;
    private GVRActivity mActivity = null;

    private FocusableController mFocusableController = null;
    private GVRSceneObject mHeadTracker = null;

    private GVRPicker mPicker = null;

    private GVRScene mMainScene = null;

    private TopSceneObject mTopSceneObject = null;
    private VideoSceneObject mVideoSceneObject = null;

    Main(GVRActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onInit(GVRContext gvrContext) {
        mGVRContext = gvrContext;

        mMainScene = new GVRScene(mGVRContext);
        mMainScene.getMainCameraRig().getTransform().setPosition(0.0f, 0.0f, 0.0f);

        mHeadTracker = new GVRSceneObject(mGVRContext, mGVRContext.createQuad(0.5f, 0.5f),
                mGVRContext.getAssetLoader().loadTexture(new GVRAndroidResource(mGVRContext, R.drawable.head_tracker)));
        mHeadTracker.getTransform().setPositionZ(-9.0f);
        mHeadTracker.getRenderData().setRenderingOrder(GVRRenderData.GVRRenderingOrder.OVERLAY);
        mHeadTracker.getRenderData().setDepthTest(false);
        mHeadTracker.getRenderData().setRenderingOrder(100000);
        mMainScene.getMainCameraRig().addChildObject(mHeadTracker);

        mFocusableController = new FocusableController();

        mPicker = new GVRPicker(gvrContext, mMainScene);
        mMainScene.getEventReceiver().addListener(mFocusableController);


        mTopSceneObject = new TopSceneObject(mGVRContext, this);

//        for (int i = 0; i < mTopSceneObject.mVideoButtons.size(); i++) {
//            Button videoButton = mTopSceneObject.mVideoButtons.get(i);
//            videoButton.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick() {
//                    showMovieSceneObject(i);
//                }
//            });
//        }

        mMainScene.addSceneObject(mTopSceneObject);
        mGVRContext.setMainScene(mMainScene);
    }


    public void showMovieSceneObject(String trackName) {
        if (mVideoSceneObject == null) {
            mVideoSceneObject = new VideoSceneObject(mGVRContext, mActivity, this);
        }
        mVideoSceneObject.init(mGVRContext, trackName);
        mMainScene.removeSceneObject(mTopSceneObject);
        mMainScene.addSceneObject(mVideoSceneObject);
    }

    public void showTopSceneObject() {
        mMainScene.removeSceneObject(mVideoSceneObject);
        mMainScene.addSceneObject(mTopSceneObject);
    }

    @Override
    public void onStep() {
        for(GVRSceneObject sceneObject : mMainScene.getSceneObjects()) {
            if (sceneObject instanceof VideoSceneObject) {
                ((VideoSceneObject) sceneObject).onStep(mGVRContext);
            }
        }
    }

    public void onTap() {
        for(GVRSceneObject sceneObject : mMainScene.getSceneObjects()) {
            if (sceneObject instanceof VideoSceneObject) {
                ((VideoSceneObject) sceneObject).onTap(mGVRContext, mFocusableController);
                return;
            }
        }
        mFocusableController.processClick(mGVRContext);
    }

    public void onTouch() {
        for(GVRSceneObject sceneObject : mMainScene.getSceneObjects()) {
            if (sceneObject instanceof VideoSceneObject) {
                ((VideoSceneObject) sceneObject).onTouch(mGVRContext);
            }
        }
    }

    public void onPause() {
        if (mMainScene == null) {return;}
        for(GVRSceneObject sceneObject : mMainScene.getSceneObjects()) {
            if (sceneObject instanceof VideoSceneObject) {
                ((VideoSceneObject) sceneObject).onPause();
            }
        }
    }

}
