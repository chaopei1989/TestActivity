package com.zero.test.overlay;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.zero.R;

public class TestOverlayActivity extends Activity {

    View mViewToRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay);
        mViewToRemove = findViewById(R.id.view_to_remove);
        mViewToRemove.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("TestOverlayActivity", "onClick");
                final ViewGroup container = (ViewGroup) mViewToRemove.getParent();
                container.getOverlay().add(mViewToRemove);
                ObjectAnimator anim = ObjectAnimator.ofFloat(mViewToRemove,
                        "translationX", container.getRight());
                anim.addListener(new AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animator arg0) {
                        container.getOverlay().remove(mViewToRemove);
                    }

                    @Override
                    public void onAnimationCancel(Animator arg0) {
                        container.getOverlay().remove(mViewToRemove);
                    }
                });
                anim.start();
            }
        });

    }

}
