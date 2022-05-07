package com.sealed.stream;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import java.util.List;

public class StreamView extends SurfaceView
        implements SurfaceHolder.Callback, View.OnTouchListener {
    private final String LOG_TAG = StreamView.class.getSimpleName();

    private NativeBindings bindings;
    private String sessionURL;
    private List<StunServer> stunServers;
    private boolean useInsecureTLS;

    public StreamView(Context context, NativeBindings bindings, String sessionURL, List<StunServer> stunServers, boolean useInsecureTLS) {
        super(context);
        this.bindings = bindings;
        this.sessionURL = sessionURL;
        this.stunServers = stunServers;
        this.getHolder().addCallback(this);
        this.useInsecureTLS = useInsecureTLS;

        setOnTouchListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) { }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (!this.bindings.startStreaming(sessionURL, stunServers.toArray(), holder.getSurface(), width, height, useInsecureTLS)) {
            Toast.makeText(this.getContext(), "Failed to start streaming", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (!this.bindings.stopStreaming())
            Toast.makeText(this.getContext(), "Failed to stop streaming", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onCapturedPointerEvent(MotionEvent event) {
        super.onCapturedPointerEvent(event);

        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            this.bindings.sendMouseMove(x, y, 0, 0);
        } else if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
            this.bindings.sendMouseButton(event.getActionButton(), true);
        } else if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE) {
            this.bindings.sendMouseButton(event.getActionButton(), false);
        }

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int idx = -1;
        int action = event.getActionMasked();
        final int pointerCount = event.getPointerCount();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                for (idx = 0; idx < pointerCount; idx++) {
                    final int id = event.getPointerId(idx);
                    final int x = (int) event.getX(idx);
                    final int y = (int) event.getY(idx);
                    this.bindings.sendTouchMove(id, x, y);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_DOWN:
                idx = 0;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (idx == -1)
                    idx = event.getActionIndex();

                final int id = event.getPointerId(idx);
                final int x = (int) event.getX(idx);
                final int y = (int) event.getY(idx);

                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP)
                    this.bindings.sendTouchEnd(id);
                else if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN)
                    this.bindings.sendTouchStart(id, x, y);

                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                for (idx = 0; idx < pointerCount; idx++) {
                    final int id = event.getPointerId(idx);
                    this.bindings.sendTouchCancel(id);
                }
                break;
            }

            default:
                break;
        }

        return true;
    }
}
